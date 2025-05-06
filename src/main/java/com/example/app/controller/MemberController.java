package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Member;
import com.example.app.service.DuplicateMemberException;
import com.example.app.service.MemberService;
import com.example.app.validation.RegisterGroup;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;

    // 会員登録フォーム表示
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    // 会員登録処理
    @PostMapping("/register")
    public String register(
            @Validated(RegisterGroup.class) @ModelAttribute("member")Member member,
            Errors errors,Model model) {

        if (errors.hasErrors()) {
            return "register";
        }
        
        try {
        	memberService.register(member);
        } catch(DuplicateMemberException e) {
        	model.addAttribute("duplicateError", e.getMessage());
        	return "register";
        }

         return "redirect:/"; // 登録後トップページにリダイレクト
    }

}
