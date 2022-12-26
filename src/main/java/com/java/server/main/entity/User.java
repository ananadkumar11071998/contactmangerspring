package com.java.server.main.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@Entity
@Table(name="User_Details")
@AllArgsConstructor
public class User {
   
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", role=" + role
				+ ", enable=" + enable + ", imageurl=" + imageurl + ", about=" + about + ", contact=" + contact + "]";
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotEmpty(message="Name field is required !!")
	@Size(min=2,max=20,message="min=2  and  max =20 character required are allowed")
	private String name;
	 
	//@NotEmpty(message="password field is must required")
	
	private String password;
	
	@Column(unique=true)
	private String email;
	
	@Column(name="manager_role")
	private String role;
	
	private boolean enable;
	
	private String imageurl;
	
	@Column(length=500)
	private String about;
	
	@OneToMany(cascade=CascadeType.ALL ,fetch=FetchType.LAZY,mappedBy="user")
	private List<Contact> contact = new ArrayList<Contact>();
	

    
   
	
}
