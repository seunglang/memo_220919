package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {

	@Autowired
	private UserBO userBO;

	/**
	 * 아이디 중복확인 api
	 * 
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is_duplicated_id")
	public Map<String, Object> isDuplicatedId(@RequestParam("loginId") String loginId) {
		
		/*
		 * boolean isDuplicated = false; // 이걸 넣으면 signUp 페이지에서 굳이 else 구문으로 에러 메세지를 띄워줄
		 * 필요가 없다 try { isDuplicated = userBO.existLoginId(loginId); } catch(Exception
		 * e) {
		 * 
		 * }
		 */
		boolean isDuplicated = userBO.existLoginId(loginId);
		Map<String, Object> result = new HashMap<>();
		if (isDuplicated) { // 중복일 때
			result.put("code", 1);
			result.put("result", true);
		} else { // 사용 가능할 때
			result.put("code", 1);
			result.put("result", false);
		}

		return result;
	}

	/**
	 * 회원가입 api
	 * 
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("sign_up")
	public Map<String, Object> signUp(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password, @RequestParam("name") String name,
			@RequestParam("email") String email) {

		// 비밀번호 해싱(hashing) 혹은 (암호화) - mb5
		// static이 붙어있으면 new를 붙여서 새로운 hip 공간에 굳이 저장을 하지 않아도 사용이 가능하다.
		String hashedPassword = EncryptUtils.md5(password);

		// db insert
		userBO.addUser(loginId, hashedPassword, name, email);

		Map<String, Object> result = new HashMap<>();
		result.put("code", 1);
		result.put("result", "데이터 입력 성공");

		return result;
	}

	@PostMapping("/sign_in")
	public Map<String, Object> signIn(@RequestParam("loginId") String loginId,
			@RequestParam("password") String password, 
			HttpServletRequest request) { // request를 들고와서 세션을 여기에 심는다.

		// 비밀번호 해싱해야 됨 - 디비에 해싱된 문자로 들어가있기 때문
		String hashedPassword = EncryptUtils.md5(password); // breaking point로 암호 제대로 넘어오는지 디버깅

		// db select
		User user = userBO.getUserByLoginIdPassword(loginId, hashedPassword); // breaking point 걸고 log가 잘 찍히는지 봐야한다.

		// 행이 있으면 로그인
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // null이 아니면 로그인 정보가 디비값이랑 일치
			result.put("code", 1);
			result.put("result", "성공");	
			
		// 세션에 유저 정보를 담는다. (로그인 상태 유지)
		// 세션이라는건 이 프로젝트가 들고 다닌다고 생각하면 됨 - 일반적으론 컨트롤로와 뷰에서 사용을 많이한다.
		HttpSession session = request.getSession(); // 이걸 주머니라고 생각하자, 또 로그인을 성공한 사람의 정보를 넣어주자
		session.setAttribute("userId", user.getId()); // id나 name 혹은 loginId를 담는다. password는 보안땜에 x
		session.setAttribute("userLoginId", user.getLoginId()); // 방금 로그인 된 사람의 정보를 담는다.
		session.setAttribute("userName", user.getName()); // session을 너무 남발하지말자.
		// model 처럼 ${}로 사용하면 된다.
		
		} else { // 행이 없으면 로그인 실패
			result.put("code", 500);
			result.put("errorMessage", "존재하지 않는 사용자입니다.");
		}

		// 행이 없으면 로그인 실패

		// return map
		return result;
	}
}
