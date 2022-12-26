package com.java.server.main.config;

import com.java.server.main.entity.User;
import com.java.server.main.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImp implements  UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	User  user =	userRepository.getUserByEmail(username);
	
	if(user==null)
	{
		throw new UsernameNotFoundException("could not found");
	}
	CustemerDetails customerDetails = new CustemerDetails(user);
		return customerDetails;
	}

}
