package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;
import com.memo.interceptor.PermissionInterceptor;

@Configuration // 누락 시키지말자 - config내에 파일들은 이 어노테이션을 붙여놔야 함
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Autowired
	private PermissionInterceptor interceptor;

	// 여기 과정을 절대로 누락시키면 안된다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/images/**") // 웹 이미지 주소 http://localhost:8080/images/aaaa_16536/sun.png 이런식
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 실제 파일 위치를 여기에 써주면 된다. 위 구문과 맵핑이 될 수 있도록.
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
		.addPathPatterns("/**")       //      /**는 아래 디렉토리까지 확인
		.excludePathPatterns("/favicon.ico", "/error", "/static/**", "/user/sign_out");
	}
}
