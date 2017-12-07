package com.gc.util;
import java.util.ArrayList;
import java.util.Date;

public class Outing {
	private Date dateOfEvent;
	private Restaurant finalLocation;
	private Person organizer;
	private ArrayList<Person> attendees;

	public Outing() {

	}

	public Outing(Date dateOfEvent, Restaurant finalLocation, Person organizer,
			ArrayList<Person> attendees) {
		super();
		this.dateOfEvent = dateOfEvent;
		this.finalLocation = finalLocation;
		this.organizer = organizer;
		this.attendees = attendees;
	}

	public Date getDateOfEvent() {
		return dateOfEvent;
	}

	public void setDateOfEvent(Date dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}

	public Restaurant getFinalLocation() {
		return finalLocation;
	}

	public void setFinalLocation(Restaurant finalLocation) {
		this.finalLocation = finalLocation;
	}

	public Person getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Person organizer) {
		this.organizer = organizer;
	}

	public ArrayList<Person> getAttendees() {
		return attendees;
	}

	public void setAttendees(ArrayList<Person> attendees) {
		this.attendees = attendees;
	}

	@Override
	public String toString() {
		return "Outing [dateOfEvent=" + dateOfEvent + ", finalLocation="
				+ finalLocation + ", organizer=" + organizer + ", attendees=" + attendees + "]";
	}

	public void setSearchRadius() {

	}

	public void setVotingTimeLimit() {
		// TODO create method that allows user to set time limit to end voting
	}

}