package com.gc.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class EmailGenerator {
 
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
 
   /* public static void main(String args[]) throws AddressException, MessagingException {
        generateAndSendEmail(null);
        System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
    }*/
 
    public static void generateAndSendEmail(String orgEmail, String[] emailAddresses) throws AddressException, MessagingException {
    	for(int i = 0; i < emailAddresses.length; ++i) {
        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");
 
        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        
        
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddresses[i]));
        
       // generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("Lena.L.Hand@gmail.com"));
        generateMailMessage.setSubject("Greetings from Outings");
        String emailBody = "Test email by your boi Outings " + "<br><br>Nah just kidding it's your friendly neighborhood"
                + " Jimmy sending you <br> some spicey emails from the comfort of his PC" + "<br><br> Regards, <br>Outings by Somebody";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");
 
        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");
 
        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", "grandcircusoutings@gmail.com", "JavaOctober2017");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
        }
        
    }
}