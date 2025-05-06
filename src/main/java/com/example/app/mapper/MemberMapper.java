package com.example.app.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.Member;

@Mapper
public interface MemberMapper {
	
	@Select("SELECT * FROM member WHERE username = #{username}")
	Member findByUsername(@Param("username") String username);
	
	@Select("SELECT * FROM member WHERE email = #{email}")
	Member findByEmail(@Param("email") String email);
	
	@Insert("INSERT INTO member(username,password,email) VALUES (#{username},#{password},#{email})")
	void insert(Member member);
}
