package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component // logic은 들어가는데 bo, dao, controller 에 속하지 않을 때 쓰는 구문 - bo와 dao, controller의 부모격이다. 그렇기 때문에 common에 저장됨 - 일반적인 스프링빈
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 실제 이미지가 저장될 경로(서버)
	// 상수를 저장할땐 대문자로 써주면 된다.
	public static final String FILE_UPLOAD_PATH = "D:\\spring_project_6\\memo\\workspace\\images/"; // 마지막엔 꼭 슬래시를 넣어줘야함
	
	// input: MultiPartFile이 통째로 넘어옴, userLoginId
	// output: image Path
	public String saveFile(String userLoginId, MultipartFile file) {
		// 파일 디렉토리 - 로그인한 유저의 id로 폴더관리를 해준다. 예) aaaa_16536/sun.png 이런식의 랜덤 숫자로 timestamp의 시간을 저장해준다(중복 방지)
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; //currentTime 함수는 밀리세컨 단위로 시간 저장해줌
		String filePath = FILE_UPLOAD_PATH + directoryName; // D:\\spring_project_6\\memo\\workspace\\images/aaaa_16536/sun.png
		// filePath가 폴더가 만들어지는 위치이다.
		
		File directory = new File(filePath); // 폴더 만들 준비
		if (directory.mkdir() == false) { //directory.mkdir(); // 성공했는지에 대한 여부 체크
			return null; // 폴더 만드는데 실패 시 이미지 패스는 null이 된다.
		}
		
		
		// 파일 업로드 : byte 단위로 업로드 된다.
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename()); //filePath + "fileName" 나중에 이런식으로 파일 이름 뒤에 추가해주면 됨 (영문자로 될 수있도록) - 확장자 분해 후 영문자로 업로드 될 수 있게 해야함
									  // getOriginalFilename은 사용자가 올린 파일명이다.
			Files.write(path, bytes); // 이 구문은 진짜 업로드 하는 순간이다.
		} catch (IOException e) { // 실패 시 catch로 넘어와서 에러를 잡는다.
			e.printStackTrace();
			return null;
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴한다.
		// http://localhost:8080/images/aaaa_16536/sun.png 이런식의 path를 만들 것이다.
		return "/images/" + directoryName + file.getOriginalFilename(); // 나중에 영문자로 수정해주면 여기도 수정해주면 된다.
	}
	
	public void deleteFile(String imagePath) { // imagePath: /images/aaaa_16536/sun.png 이런식으로 넘어올 것
		//     \\images/        imagePath에 있는 겹치는 /images/ 이 부분 제거해줘야 함
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		if(Files.exists(path)) {
			// 이미지 삭제
			try {
				Files.delete(path);
			} catch (IOException e) {
				//e.printStackTrace();
				logger.error("[이미지 삭제] 이미지 삭제 실패. imagePath:{}", imagePath);
			}
			
			// 디렉토피(폴더) 삭제
			path = path.getParent();
			if (Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					//e.printStackTrace();
					logger.error("[이미지 삭제] 디렉토리 삭제 실패. imagePath:{}", imagePath);
				}
			}
		}
	}
}
