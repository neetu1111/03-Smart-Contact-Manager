package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.razorpay.*;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	//adding comman data to
	@ModelAttribute
	public void addCommanData(Model m,Principal principal)
	{
		String useremail = principal.getName();
		User userdata = userRepo.getUserByUserName(useremail);
		
		m.addAttribute("userdata",userdata);
	}
	
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title", "add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form"; 
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Principal principal,HttpSession session)
	{  try {
		String name=principal.getName();
		User user=this.userRepo.getUserByUserName(name);
		
		//proccessing uploading image
		
		if(file.isEmpty()) {
			System.out.println("file is empty");
		}else
		{
			contact.setImage(file.getOriginalFilename());
			File saveFile= new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath(), File.separator, file.getOriginalFilename());
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("image Uploaded");
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepo.save(user);
		
		session.setAttribute("message", new Message("Your Contact is Added", "success"));
	}catch(Exception e)
		{
		session.setAttribute("message", new Message("some thing want wrong", "denger"));
		}
		return "normal/add_contact_form";
	}
	
	@GetMapping("/show-contacts/{page}")
	public String showContact(@PathVariable("page") Integer page,Model m,Principal principal)
	{
		System.out.println("hii view");
		m.addAttribute("title","Show Contact");
		
		String name = principal.getName();
		User user=this.userRepo.getUserByUserName(name);
		
		PageRequest pageable = PageRequest.of(page,3);
		
		Page<Contact> contacts = this.contactRepo.findContactByUser(user.getId(),pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId,Model model,HttpSession session)
	{
		Optional<Contact> contactOptional = this.contactRepo.findById(cId);
		Contact contact = contactOptional.get();
		contact.setUser(null);
		this.contactRepo.delete(contact);
		
		session.setAttribute("message", new Message("contact deleted successfully","success"));
		
		return "redirect:/user/show-contacts/0";
		
		
	}
	
	@PostMapping("/update-contact/{cid}")
	public String updateForm(Model model,@PathVariable("cid") Integer cId)
	{
		Contact contact=this.contactRepo.findById(cId).get();
		model.addAttribute("contact", contact);
		
		return "normal/updateform";
	}
	
	@PostMapping("/process-updatecontact")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model m,HttpSession session,Principal principal)
	{
		
		try {
			  
				String name = principal.getName();
				User userByUserName = this.userRepo.getUserByUserName(name);
				contact.setUser(userByUserName);
				this.contactRepo.save(contact);
				session.setAttribute("message",new Message("Sucessfully Contact Update !!!", "success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message",new Message(" not update !!!", "denger"));
		}
		
		return "redirect:/user/"+contact.getCid()+"/contact-detail";
	}
	
     
	@RequestMapping("/{cId}/contact-detail")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model m,Principal principal)
	{
	           Optional<Contact> contactOptional = this.contactRepo.findById(cId);
	           Contact contact=contactOptional.get();
	           
	           String UserName=principal.getName();
	           User user = this.userRepo.getUserByUserName(UserName);
	           
	           if(user.getId()==contact.getUser().getId())
	           {
	        	   m.addAttribute("ucontact", contact);
	        	   m.addAttribute("title", contact.getName());
	           }
	           return "normal/contact_detail";
		
		
	}
	
	@GetMapping("/settings")
	public String openSettings()
	{
		return "normal/settings";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session)
	{
		String name = principal.getName();
		User currentUser = this.userRepo.getUserByUserName(name);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepo.save(currentUser);
			session.setAttribute("message", new Message("your password successfully changed!!!","success"));
			
		}
		else
		{
			session.setAttribute("message", new Message("wrong password","denger"));
			
		}
		
		
		return"redirect:/user/index";
	}

	
	
	@PostMapping("/create_order")
	@ResponseBody
	public String orderCreate(@RequestBody Map<String, Object> data) throws RazorpayException
	{
		
		System.out.println("data>>>>>"+data);
		   int amt = Integer.parseInt(data.get("amount").toString());
		   var razorpayClient = new RazorpayClient("rzp_test_wiMdjGDSgLmGpJ", "O74V0HiIDLc28Cbw0MykbP4v");
		   
		   JSONObject options = new JSONObject();
		   options.put("amount", amt*100);
		   options.put("currency", "INR");
		   options.put("receipt", "txn_123456");
		   Order order = razorpayClient.orders.create(options);
		   System.out.println("order"+order);
		   
		   //if you want to save order information in your database then save it
		  return order.toString();
	}
	
	

}















