package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Favorite {

	private Integer id;
	private Integer memberId;
	private Integer dishId;
	private String dishName;
	private String dishType; // "meat", "fish", "other"
	private LocalDateTime createdAt;
}
