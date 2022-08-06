package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	
	@Autowired
	UserRepository userRepo;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "i m using thymeleaf");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("about", "i m using thymeleaf");
		return "about";
	}
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("signup", "i m using thymeleaf");
		model.addAttribute("user", new User());
		System.out.println("i m in signup");
		return "signup";
	}
	
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bresult,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session ) {
		
		try {
			
			if(!agreement)
			{
				System.out.println("you sre not selected term and condition!!");
				throw new Exception("you sre not selected term and condition!!");
			}
			
            if(bresult.hasErrors()) {
				
				System.out.println("error"+bresult.toString());
				model.addAttribute("user",user);
				return "signup";
				
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(PasswordEncoder.encode(user.getPassword()));
			
			User saveUser = this.userRepo.save(user);
			
			System.out.println("agreement"+agreement);
	        System.out.println("user>>"+saveUser);		
			System.out.println("i m in>>>>>>>>> signup");
			model.addAttribute("user", new User());
			session.setAttribute("message", new com.smart.helper.Message("Successfully Registered", "success-msg"));
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			
			session.setAttribute("message", new com.smart.helper.Message("something went worng !!"+e.getMessage(), "alert-denger"));
			return "signup";
		}
		
		
		
	}
	
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title", "login page");
		return "login";
	}

}
