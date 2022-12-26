package com.java.server.main.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import com.java.server.main.entity.User;
import com.java.server.main.repository.UserRepository;
import com.java.server.main.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgetControlller {
	
	
	
	
	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//open email handler
	@GetMapping("/forget_email")
	public String openEmailHandler()
	{
		return"forget_email_form";
	}
	
	@PostMapping("/forget_email")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		System.out.println("otp email"+email);
		
		int otp=random.nextInt(999999);
		
		
		String subject="OTP From SCM";
		
		String message=""+"<div style='border:1px solid #001a33; padding:20px'>"
		                 +"<h1>"
				         +"OTP -"
		                 +"<b>"+otp
				         +"</n>"
		                 +"</h>"
				         +"</div>"
				         +"</h1>";
		
		String to=email;	
		
	  boolean flag=	this.emailService.sendEmail(subject, message, to);
	  
	  if(flag)
	  {
		  session.setAttribute("myOtp", otp);
		  session.setAttribute("email", email);
		  return"verify_otp";
	  }else
	  {
		  session.setAttribute("message", "check your email id");
		  return "forget_email_form";
	  }
		
	}
	
	@PostMapping("/verify_otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myOtp");
		String email=(String)session.getAttribute("email");
		if(myOtp==otp)
		{
			User user= this.userRepo.getUserByEmail(email);
			
			if(user==null)
			{
				//send error message
				 session.setAttribute("message", "User don't exist this email");
				  return "forget_email_form";
				
			}else
			{
				//password change
				return "password_change_form";	
			}
			
			
		}else
		{
			session.setAttribute("message", "you have entered wrong otp");
			return"verify_otp";
		}
	}
	
	//change password
	@PostMapping("/change_password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
	{
	  	String email = (String)session.getAttribute("email");
	  	  User user=  this.userRepo.getUserByEmail(email);
	  	  user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
	  
	  	  this.userRepo.save(user);
	  	  return"redirect:/signin";
	}
	

}
