package com.example.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.domain.Member;
import com.example.app.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	public void register(Member member) {
		
		if(memberMapper.findByUsername(member.getUsername()) !=null) {
			throw new DuplicateMemberException("そのユーザー名はすでに使用されています");
			
		}
		
		if(memberMapper.findByEmail(member.getEmail()) !=null) {
			throw new DuplicateMemberException("そのメールアドレスはすでに使用されています");
		}
		
		// パスワード暗号化して登録
		String encodedPassword=passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		memberMapper.insert(member);
	}
}
