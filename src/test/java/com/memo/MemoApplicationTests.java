package com.memo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

@SpringBootTest
class MemoApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void 널체크() {
		String a = null;
		if (ObjectUtils.isEmpty(a)) {
			System.out.println("비어있다");
		}
	}

}
