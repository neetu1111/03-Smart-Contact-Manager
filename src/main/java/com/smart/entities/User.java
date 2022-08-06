package com.smart.entities;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message="Name is required")
	@Size(min=3,max=12,message="User name must be between 3 to 12 characters!")
	private String name;
	
	private String  password;
	
	@Column(unique = true)
	private String email;
	
	private String role;
	private Boolean enabled;
	private String about;
	private String imgUrl;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user")
	private List<Contact> contacts= new ArrayList<>();

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", role=" + role
				+ ", enabled=" + enabled + ", about=" + about + ", imgUrl=" + imgUrl + ", contacts=" + contacts
				+ ", getId()=" + getId() + ", getName()=" + getName() + ", getPassword()=" + getPassword()
				+ ", getEmail()=" + getEmail() + ", getRole()=" + getRole() + ", getEnabled()=" + getEnabled()
				+ ", getAbout()=" + getAbout() + ", getImgUrl()=" + getImgUrl() + ", getContacts()=" + getContacts()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}


}
