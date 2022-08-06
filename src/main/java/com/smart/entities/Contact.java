package com.smart.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;

	private String name;
	private String secondName;
	private String work;
	private String email;
	private String phone;
	private String image;
	private String Description;
	@ManyToOne
	@JsonIgnore
	private User user;
	
	/*
	 * @Override public String toString() { return "Contact [cid=" + cid + ", name="
	 * + name + ", secondName=" + secondName + ", work=" + work + ", email=" + email
	 * + ", phone=" + phone + ", image=" + image + ", Description=" + Description +
	 * ", user=" + user + "]"; }
	 */



}
