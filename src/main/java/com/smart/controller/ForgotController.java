package com.smart.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.service.EmailService;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		System.out.println(">>>"+email);
		
		Random random = new Random();
       	int Otp = random.nextInt(999999);
       	System.out.println(">>>"+Otp);
       	String subject="OtP Of SCM";
       	String message="<h1> OTP ="+Otp+"</h1>";
       	String to=email;
       	
       	boolean flag = this.emailService.sendEmail(subject, message, to);
       	
       	if(flag)
       	{
       		System.out.println("if>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
       		return "verify_otp";
       	}
       	else {
       		System.out.println("else????????????????????????");
       		session.setAttribute(message, "check your email id !!");
       		return "forgot_email_form";
       	}
		
		
	}

}
