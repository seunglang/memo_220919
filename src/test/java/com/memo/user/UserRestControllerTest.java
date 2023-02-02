package com.memo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

@SpringBootTest
class UserRestControllerTest {
	
	@Autowired
	UserBO userBO;

	//@Test
	void test() {
		User user = userBO.getUserByLoginIdPassword("aaaa", "74b8733745420d4d33f80c4663dc5e5");
		assertNotNull(user); // user가 null이 아니면 올바르게 가져온다
	}
	
	@Transactional // rollback
	@Test
	void 유저추가테스트() {
		userBO.addUser("cccc333", "cccc333", "테스트333", "mail@gmail.com");
	}

}
