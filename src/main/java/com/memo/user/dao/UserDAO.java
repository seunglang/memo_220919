package com.memo.user.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.user.model.User;

@Repository
public interface UserDAO {
	
	// 아이디 중복 확인 구문
	public boolean existLoginId(String loginId);
	
	// db 데이터 저장
	public void insertUser(
			@Param("loginId") String loginId,
			@Param("password") String password,
			@Param("name") String name,
			@Param("email") String email);
	
	// 로그인 정보 있는지 확인
	public User getUserByLoginIdPassword(
			@Param("loginId") String loginId,
			@Param("password") String password);
}
