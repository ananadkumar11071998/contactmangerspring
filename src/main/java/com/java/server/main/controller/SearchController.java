package com.java.server.main.controller;

import java.security.Principal;
import java.util.List;

import com.java.server.main.entity.Contact;
import com.java.server.main.entity.User;
import com.java.server.main.repository.ContactReposiotory;
import com.java.server.main.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
  
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactReposiotory contactRepo;
	//search handler
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
	{
		String name = principal.getName();
		
		User user = this.userRepo.getUserByEmail(name);
		List<Contact> contact= this.contactRepo.findByNameContainingAndUser(query,user);
		
		return ResponseEntity.ok(contact);
	}
}
