package com.gc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dao.AttendeesDao;
import com.gc.dao.AttendeesDaoImpl;
import com.gc.dao.CurrentScoreDao;
import com.gc.dao.CurrentScoreDaoImpl;
import com.gc.dao.OutingDao;
import com.gc.dao.OutingDaoImpl;
import com.gc.dao.PersonDao;
import com.gc.dao.PersonDaoImpl;
import com.gc.dao.SurveyDao;
import com.gc.dao.SurveyDaoImpl;
import com.gc.dto.CurrentScoreDto;
import com.gc.dto.SurveyDto;
import com.gc.util.GeolocationAPI;
import com.gc.util.Outing;
import com.gc.util.Person;
import com.gc.util.Survey;

@Controller
public class HomeController {

	@RequestMapping({ "/", "index" })
	public ModelAndView homepage(Model model) {
		CurrentScoreDto dto = new CurrentScoreDto();
		CurrentScoreDao dao = new CurrentScoreDaoImpl();
		AttendeesDao adao = new AttendeesDaoImpl();
		OutingDao odao = new OutingDaoImpl();
		PersonDao pdao = new PersonDaoImpl();
		SurveyDao sdao = new SurveyDaoImpl();
		model.addAttribute("displayPreference", "\"display:none;\"");

		return new ModelAndView("index", "result", "");

	}
	//Serhiy add @RequestParam("password") String password
	@RequestMapping(value = "voting", method = RequestMethod.POST)
	public ModelAndView votingGeneration(@RequestParam("organizerEmail") String organizerEmail,
			@RequestParam("emailAddress") String emailAddress, @RequestParam String userPassword, @RequestParam("street") String street,
			@RequestParam("city") String city, @RequestParam("state") String state, @RequestParam("outingName") String outingName, @RequestParam("date") String date, Model model)
/* @RequestParam("votingWindow") String votingWindow, */
			throws ParseException, AddressException, MessagingException {
		//creating the daoImpl to write to the database
		PersonDao pdao = new PersonDaoImpl();
		OutingDao outDao = new OutingDaoImpl();

		// Changes input java date into sql date
		String[] formatDate = date.split("-");
		Date eventDate = new Date(Integer.parseInt(formatDate[0]), Integer.parseInt(formatDate[1]),
				Integer.parseInt(formatDate[2]));
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate = formatter.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());

		//Adding people coming from the form into relevant databases
		pdao.addPerson(organizerEmail, userPassword);// we need the id of this organizer for the next push to the database
		int organizerId = pdao.searchByEmail(organizerEmail).get(0).getUserID();//we need to be able to search a person
		String surveyID = outingName + "," + date.toString() + "," + organizerId;//syntax for key

		String[] emailAddresses = emailAddress.split(",");
		ArrayList<Person> attendees = new ArrayList<>(emailAddresses.length + 1);// when can from here search the
		System.out.println(Arrays.toString(emailAddresses));
		/*for (int i = 0; i < emailAddress.length(); ++i) {
			//pdao.addPerson(emailAddresses[i], "3R5S");
			//System.out.println("first email" + emailAddresses[0]);	
		}*/

		Person organizer = new Person(organizerEmail, "nope", null);// we may want the organizer's name
		attendees.add(organizer);

		for (int i = 0; i < emailAddresses.length; i++) {
			attendees.add(new Person(emailAddresses[i], null, null));
			// we can drop the name req from the constructor OR get their name for oAuth OR
			// get it from the database
		}

		GeolocationAPI location = new GeolocationAPI(street, city, state);
		// passing location to create and return survey
		Outing constructingOuting = new Outing(outingName, sqlDate, organizer, attendees, location, surveyID);
		
		//this gets the list of potiential Restaurants
		Survey mySurvey = constructingOuting.getPotentialEvent();
		
		System.out.println();
		outDao.addOuting(outingName, surveyID, eventDate, " ", organizerId);
		//this builds the HTML OBJ table for voting
		String outingObjHTML = "<h2> " + outingName + "</h2>";
	    outingObjHTML += "<h4> " + date + "</h4>";
	    //this method builds the voting form we need to tell it the SurveyID 
		outingObjHTML += mySurvey.buildVotingeRestaurantTable(surveyID);
		
		

		//Creates email generator object and sends the emails upon clicking submit on the preferences page.
		/*EmailGenerator email = new EmailGenerator();
		for(int i =0; i < emailAddresses.length; ++i) {
		email.generateAndSendEmail(organizerEmail, emailAddresses[i]);
		}
	*/

		return new ModelAndView("voting", "result", outingObjHTML);
	}
	
	//TODO This method receives the clickable link
	@RequestMapping(value ="/voting", method=RequestMethod.GET)
	public ModelAndView recordVoteFromLink(Model model, @RequestParam("voterEmail") String voterEmail, @RequestParam("surveyID") String surveyID) {
		
		System.out.println("in beginning of method");
		//we should search the database for the surveyID
		SurveyDaoImpl surveyDB = new SurveyDaoImpl();
		//LINK HAS TO BE FORMATTED WITH NO QUOTES :O 
		SurveyDto surveyDto = surveyDB.searchSurvey(surveyID).get(0);  //this should be filled from the database
		//we build the survey object from the ID
		Survey mySurvey = new Survey(surveyDto);
		
		//TODO get the Outing information: Event Name, Organizer, Date from the outing object, if we are searching by ID by doing a join on the table
		//I tried some SQL queries but we will need help
		
		
		String outingObjHTML = "<h2> Thank you " + voterEmail +" </h2> <h3> Please vote below: " + surveyID + "</h3>";
		outingObjHTML = mySurvey.buildVotingeRestaurantTable(surveyID);//when we have the object built we may not need to pass an array 
		//TODO call a method to set the email address
		
		return new ModelAndView("voting", "result", outingObjHTML);
	}
	
	//TODO needs to be working -- we may have to push a outing variable in a hidden field // we need to make another hidden field to record who is voting
	@RequestMapping("/recordVote")
	public ModelAndView recordVote(Model model, @RequestParam("rstrnt") String[] restaurantVote, @RequestParam("surveyID") String surveyID) {
		
		//surveyID should be filled from the database- is not right now.
		SurveyDaoImpl surveyDB = new SurveyDaoImpl();
		// we have to know who voter is
		String userEmail = "jenna.otto@gmail.com";
		System.out.println("Survey ID " + surveyID);
		
		SurveyDto surveyDto = surveyDB.searchSurvey(surveyID).get(0);  //this should be filled from the database
		System.out.println(" Survey DTO  restaurant ID" + surveyDto.getOptVenueID1() + " vote count " +surveyDto.getVoteCount1());
		
		Survey mySurvey = new Survey(surveyDto);
		String outingObjHTML = mySurvey.buildResultRestaurantTable(restaurantVote);//when we have the object built we may not need to pass an array 
		// get survey object (from Outing object)
		
		// update the object
		// let the person know they have voted

		return new ModelAndView("voting", "result", outingObjHTML);
	}

	
	@RequestMapping("preferences")
	public String viewPreferencesPage() {
		//System.out.println("Here");

		return "preferences";
	}

/*
	@RequestMapping("voting")
	public ModelAndView voting() {
		return new ModelAndView("voting", "", "");
	}*/
	
}