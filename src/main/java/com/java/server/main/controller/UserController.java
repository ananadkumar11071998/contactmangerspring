package com.java.server.main.controller;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.java.server.main.entity.Contact;
import com.java.server.main.entity.User;
import com.java.server.main.helper.Message;
import com.java.server.main.repository.ContactReposiotory;
import com.java.server.main.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactReposiotory contactRepo;
	
	@ModelAttribute
	public void addCommonData(Model model ,Principal principal)
	{
		String userName= principal.getName();
		//System.out.println("UserName"+userName);
	 User user   = 	userRepo.getUserByEmail(userName);
	//  System.out.println("user"+user);
	     model.addAttribute("user",user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
	
		return"normal/user_dashboard";
		
	}
	
	//open add form handler
	@GetMapping("/add_contact_form")
	private String openAddContactForm(Model model,Principal principal)
	{
	    model.addAttribute("title","add contact page");
	    
	    model.addAttribute("contact", new Contact());
	    return "normal/add_contact_form";
	}
	
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact,BindingResult bindresult,Model model,@RequestParam(value="agreement" ,defaultValue="false") boolean agreement ,@RequestParam("profileImage") MultipartFile file,Principal principal ,HttpSession session)
	{
     try {
	 
    	 if(!agreement)
 		{
 			System.out.println("you not hava agree");
 			throw new Exception("you have not agreed");
 		}
 		
	 
	 //user.getContact().add(contact);
	 
	// this.userRepo.save(user);
	
	 
	 
	 
		System.out.println("data"+contact);
		
		if(bindresult.hasErrors())
		{
			model.addAttribute("contact",contact);
			
			return "normal/add_contact_form";
		}
		
		
		//process and upload file
		
		if(file.isEmpty())
		{
			//if is empty then print our image
			contact.setImage("imagelogo.jpg");
		}else
		{
			//file to the folder update
			contact.setImage(file.getOriginalFilename());
		   
			File savefile = new ClassPathResource("static/img").getFile();	
			
			Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is uplaoded");
			
		}
		
		String name=	principal.getName();
		  
		 User user = userRepo.getUserByEmail(name);
		 contact.setUser(user);
		 this.contactRepo.save(contact);
		  
		  //message of success
		  
		 model.addAttribute("contact",new Contact());
			session.setAttribute("message", new Message("your contact is added","alert-secondary"));
			
			  return "normal/add_contact_form";
		  
		  
     }catch(Exception e)
     {
    	 model.addAttribute("contact", contact);
    	 System.out.println("Error"+e.getMessage());
    	 e.printStackTrace();
    	 
    	 //message of error
    	 System.out.println("fsfs");
    	 
    	 
    	 session.setAttribute("message", new Message("something went wrong","alert-danger"));
    	
    	  return "normal/add_contact_form";
    }
    
    	}
	
	//show contacts handler
	
	@GetMapping("/show_contacts/{page}")
	public String showcontacts(  @PathVariable("page") Integer page,Model m,Principal principal)
	{
		String userName= principal.getName();
	User user=	this.userRepo.getUserByEmail(userName);
	
	   Pageable pegeable  =PageRequest.of(page, 5);
    
	
	Page<Contact> contacts = 	this.contactRepo.findByUser(user,pegeable);
      
    
	/* Iterator itr=contacts.iterator();
      
      for(Contact c:contacts)
      {
    	  System.out.println(c.getCid());
      }
      */
	
      System.out.println(contacts);
      
      m.addAttribute("contacts", contacts);
      m.addAttribute("currentPage",page);
      m.addAttribute("totalPages",contacts.getTotalPages());
		return "normal/show_contacts";
	}
	
	//show contact details
	@GetMapping("/{cid}/contact")
	public String showcontactsDetails(@PathVariable("cid") int cid,Model model,Principal principal)
	{
		System.out.println("cid"+cid);
		
		Optional<Contact> contactOptional = this.contactRepo.findById(cid);
		
		Contact contact = contactOptional.get();
		  
		String user = principal.getName();
		
		User userdata = userRepo.getUserByEmail(user);
		
		if(userdata.getId()==contact.getUser().getId())
		{	
		
	        model.addAttribute("contact",contact);
		}
		return "normal/contact_detials";
	}
	
	//delete contacts
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid,Model model,HttpSession session)
	{
		Contact contact = this.contactRepo.findById(cid).get();
	    
	   
	    contact.setUser(null);
	    this.contactRepo.delete(contact);
	    
	    session.setAttribute("message", new Message("contact delete sucessful...","success"));
	    
	    return"redirect:/user/show_contacts/0";
	}
	
	
	// open update form handler
	@PostMapping("/open_contact/{cid}")
	 public String updateForm(@PathVariable("cid") Integer cid,Model model)
	 {
		Contact contact= this.contactRepo.findById(cid).get();
		
		  model.addAttribute( "contact",contact);
		  System.out.println(contact.getDescription());
		 return"normal/update_form";
	 }
	
	
	//update form handler
	@PostMapping("/process_update")
	public String updatehandler(@ModelAttribute("contact") Contact contact,@RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,Principal principal)
	{
	   	try
	   	{
	   	 Contact oldcontactDetails=	this.contactRepo.findById(contact.getCid()).get();
	   		//image..
	   		if(!file.isEmpty() )
	   		{
	   			System.out.print("file is empty");
	   		}
	   		
	   	  
	   	User user = this.userRepo.getUserByEmail(principal.getName());
	   	contact.setUser(user);
	   	
	   	this.contactRepo.save(contact);
	   	}catch(Exception e)
	   	{
	   		e.printStackTrace();
	   	}
	   	System.out.println("update user name"+contact.getName());
		System.out.println("upadate user id"+contact.getCid());
	   	return"";
	}
		
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","Profile");
		return"normal/profile";
	}
	
	 // open setting handler
	@GetMapping("/setting")
	public String openSetting()
	{
		return"normal/setting";
	}
	
	
	//change Password handler
	
	@PostMapping("/change_password")
	public String changePasswordHandler(@RequestParam("oldpassword") String oldpassword,@RequestParam("newpassword") String newpassword,Principal principal,HttpSession session)
	{
		//String user = principal.getName();
		User currentuser = this.userRepo.getUserByEmail(principal.getName());
		
		
		System.out.println("old password"+oldpassword);
		System.out.println("old password"+newpassword);
		System.out.println("current user"+currentuser.getPassword());
		System.out.println("current user");
		
		if(this.bCryptPasswordEncoder.matches(oldpassword,currentuser.getPassword() ))
		{
			
			//change the password
			currentuser.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userRepo.save(currentuser);
			session.setAttribute("message", new Message("Your password is successfully changed", "alert-success"));
		}else
		{
			session.setAttribute("message", new Message("Your password is successfully changed", "alert-danger"));
			return"redirect:/user/setting";
		}
		return "normal/user_dashboard";
	}
	
}
