package com.example.app.service;

//会員登録時すでに使用されてるユーザー名（またはメールアドレスなど）をエラーにするクラス

public class DuplicateMemberException extends RuntimeException {
	
	public DuplicateMemberException(String message) {
		super(message);
	}

}
