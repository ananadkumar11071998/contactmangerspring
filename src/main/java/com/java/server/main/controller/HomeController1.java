package com.java.server.main.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.java.server.main.entity.User;
import com.java.server.main.helper.Message;
import com.java.server.main.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController1 {
 
	
	@Autowired
	 private UserRepository userReposiotry;
	
	@Autowired
	 private  BCryptPasswordEncoder passwordEncoder;
	 
	@GetMapping("/home")
	public String home(Model model)
	{
		model.addAttribute("name","kumar");
		return"Home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("name", "raju");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("tittle","Resgister-smart contact manager");
		model.addAttribute("user", new User());
		
		return "signup";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,@RequestParam(value="agreement" ,defaultValue="false") boolean agreement,Model model,HttpSession session)
	{
		
		
		try {
		if(!agreement)
		{
			System.out.println("you not hava agree");
			throw new Exception("you have not agreed");
		}
		

        if (bindingResult.hasErrors()) {
        	
        	System.out.println("error"+bindingResult.toString());
        	model.addAttribute("user",user);
            return "signup";
        }

  
		user.setRole("ROLE_USER");  
		user.setEnable(true);
		System.out.println("agreement is"+agreement);
		System.out.println("User"+user);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		model.addAttribute("user",new User());
		User result = this.userReposiotry.save(user);
		
		 System.out.println(result);
		 session.setAttribute("message", new Message("Sucessfull Register","alert-success"));
		 return "signup";
		
		}catch(Exception e)
	
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("some went wrong"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
		//handler 	form custom login
		@GetMapping("/signin")
		public String customlogin(Model model)
		{
			model.addAttribute("tittle","Login Page"); 
			return"login";
		}
		
		
	
}
