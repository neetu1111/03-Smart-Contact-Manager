package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	public boolean sendEmail(String subject,String message,String to)
	{
		boolean f=false;
		/*
		 * String message="hello"; String subject="codeArea:"; String
		 * to="neetu.jdeveloper@gmail.com";
		 */
		String from="nandninik@gmail.com";
		//variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties=System.getProperties();
		System.out.println("properties"+properties);
		
		//setting important information to properties object
		
		//host set
		properties.put("mail.smtp.host",host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//get the session object
		Session session = Session.getInstance(properties, new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("nandninik@gmail.com","nandniosho12");
			}
		});
		//compose msg
		System.out.println("??session"+session);
		session.setDebug(true);
		
		MimeMessage m = new MimeMessage(session);
		 try {
				
				 m.setFrom(from); 
				  m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				  m.setText(message);
				  System.out.println();
				  Transport.send(m);
				 
			System.out.println("send successfully");
			f=true;
		} catch (Exception e) {
			System.out.println("fail>>>>>>>>>");
			e.printStackTrace();
	        
		}
		return f;
	}


}
