package com.ks.suate01.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ks.suate.dto.UserDTO;
import com.ks.suate01.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final UserService userService;
	
	@GetMapping("/login")
	public String main(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
	@PostMapping("/signin")
	public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) {
		int login_result = userService.find(userDTO.getUser_id(), userDTO.getUser_pw());

		if (login_result > 0) {
			session.setAttribute("user_id", userDTO.getUser_id());
			return "redirect:/game";

			
		} else {
			session.invalidate();
			model.addAttribute("resultMessage", "登録されていない会員です。");
			return "login";
		}
	}
}
