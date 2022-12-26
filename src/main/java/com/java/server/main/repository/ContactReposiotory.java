package com.java.server.main.repository;

import java.util.List;

import com.java.server.main.entity.Contact;
import com.java.server.main.entity.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ContactReposiotory extends JpaRepository<Contact,Integer>{
    
	@Query("select u From Conatct u where u.user_id=:user_id")
	
	//currentpage page
	//contact per page=5
	public  Page<Contact> findByUser(User user,Pageable pegeable);
	
	
	public List<Contact> findByNameContainingAndUser(String name,User user);
}
