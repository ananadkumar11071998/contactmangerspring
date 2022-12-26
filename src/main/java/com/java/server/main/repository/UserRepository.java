package com.java.server.main.repository;

import com.java.server.main.entity.User;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository  extends JpaRepository<User,Integer	>{

	@Query("select u From User u where u.email=:email")
	public User getUserByEmail(@Param("email") String email);
}
