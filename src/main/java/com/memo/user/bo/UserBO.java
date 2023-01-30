package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.dao.UserDAO;
import com.memo.user.model.User;

@Service
public class UserBO {
	
	@Autowired  // dependency injection 과정 - 스프링빈을 주입해서 BO에 껴놓기
	private UserDAO userDAO;
	
	// 아이디 중복 확인
	public boolean existLoginId(String loginId) {
		return userDAO.existLoginId(loginId);
	}
	
	// 회원가입 정보 db에 insert
	public void addUser(String loginId, String password, String name, String email) {
		userDAO.insertUser(loginId, password, name, email);
	}
	
	// 로그인 정보 있는지 확인
	public User getUserByLoginIdPassword(String loginId, String password) {
		return userDAO.getUserByLoginIdPassword(loginId, password);
	}
}
