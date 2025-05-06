package com.example.app.domain;

import com.example.app.validation.LoginGroup;
import com.example.app.validation.RegisterGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Member {
	
	@NotBlank(groups= {RegisterGroup.class, LoginGroup.class})
	private String username;

	@NotBlank(groups= {RegisterGroup.class})
	@Email
	private String email;

	@NotBlank(groups= {RegisterGroup.class, LoginGroup.class})
	private String password;
	
	private Integer id;
	public Integer getId() {
		return id;
	}

}