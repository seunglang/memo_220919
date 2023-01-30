package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
	
	/**
	 * 회원가입 화면
	 * @return
	 * http://localhost:8080/user/sign_up_view
	 */ 
	@GetMapping("/sign_up_view")
	public String signUpView(Model model) {
		model.addAttribute("viewName", "user/signUp");
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * http://localhost:8080/user/sign_in_view
	 * @param model
	 * @return
	 */
	@GetMapping("/sign_in_view")
	public String signInView(Model model) {
		model.addAttribute("viewName", "user/signIn");
		return "template/layout";
	}
	
	/**
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("/sign_out") // 결과는 view화면으로 갈 것이기 때문에 String으로 해주면 된다.
	public String signOut(HttpSession session) { // 바로 HttpSession으로 받을 수 있다. - restController처럼 request 가져와서 할 수도 있음.
		// 로그아웃 => 세션에 있는 것들을 모두 비운다.
		session.removeAttribute("userId");
		session.removeAttribute("userLoginId");
		session.removeAttribute("userName");
		
		return "redirect:/user/sign_in_view"; // 로그아웃 후 로그인 페이지로 리다이렉트 - (현재 절대경로 사용)
	}
}
