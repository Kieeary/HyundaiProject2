package com.chysk5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chysk5.domain.MemberDTO;
import com.chysk5.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

/*
 * 신수진 작성 
 * */
@Controller
@Log4j
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {	
	
	private final MemberService service;
	
	// 회원가입 페이지로 이동
	@GetMapping("/join")
	public void joinForm() {}
	
	// 회원가입
	@PostMapping("/join")
	public String joinAction(MemberDTO member){
		
		log.info("join...." + member);
		
		service.join(member);
				
		return "redirect:/member/login";
	}
	
	// 아이디 중복 확인
	@PostMapping("/checkId")
	@ResponseBody
	public String checkId(String mem_id) {
		
		log.info("check id..... " + mem_id);
		
		String result = service.checkId(mem_id);
		
		log.info("check id result : " + result);
		
		return result;
	}
	
	// 로그인 페이지로 이동
	@GetMapping("/login")
	public void loginForm(String error, String logout, Model model) {
		
		log.info("error: " + error);
		log.info("logout: " + logout);
		
		if(error != null) {
			model.addAttribute("error", "Login Error Check Your Account");
		}
		
		if(logout != null) {
			model.addAttribute("logout", "Logout!!");
		}
	}
	
	
	// 아이디 찾기 페이지
	@GetMapping("/findId")
	public void findId() {}
	
	// 아이디 찾기 Action
	@PostMapping("/findId")
	public String findIdAction(MemberDTO member, Model model) {
		
		log.info("find id action.... " + member);
		
		MemberDTO result = service.findId(member);
		
		log.info("find id action result... " + result);
		
		model.addAttribute("member", result);
		
		return "member/findIdResult";
	}
	
	// 아이디 찾기 결과 페이지
	@GetMapping("/findIdResult")
	public void findIdResult() {}
	
	
	// 비밀번호 찾기 페이지
	@GetMapping("/findPwd")
	public String find_pwd() {
		 return "member/findPwd";
	}
	
	// 비밀번호 찾기 Action
	@PostMapping("/findPwd")
	public String findPwdAction(MemberDTO member, Model model) {
		
		log.info("find password action...." + member);
		
		MemberDTO result = service.findPwd(member);
		
		model.addAttribute("member", result);
		
		return "member/pwdModify";
	}
	
//	@GetMapping("/pwdModify")
//	public void 
	
}
