package com.example.app.domain;

import java.util.List;

import lombok.Data;

@Data
public class Meal {
	private Integer id;
	private String maindish;
	private String sidedish;
	private int category;// 1: meat, 2: fish, 3: other
	private String name; // ← ここに maindish を代入
	
	 // 献立に紐づく材料一覧（追加）
    private List<String> ingredients;
    private String imagePath;
}
