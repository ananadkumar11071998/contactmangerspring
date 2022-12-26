package com.java.server.main.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="user_contact")
public class Contact {

	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
	
	@Column(length=500)
	private String description;
	
	@Size(min=10,message="digit of  phone is  must be 10")
	@NotBlank(message="enter your phone number")
	//@Column(unique=true)
	private String phone;
	
	@NotEmpty(message="enter your name")
	private String name;
	
	@NotEmpty(message="enter your nickname")
	private String nickname;
	
	@NotEmpty(message="enter your eork")
	private String work;
	
	@NotEmpty(message="enter your our email")
	//@Column(unique=true)	
	private String email;
	
	
	//@message="choose your imgae")
	private String image;
	
	@ManyToOne
	@JsonIgnore
	private User user ; 
	
}
