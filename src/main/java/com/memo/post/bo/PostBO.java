package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	//private Logger logger = LoggerFactory.getLogger(PostBO.class); // 원하는 클래스마다 복붙하자
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 페이징
	private static final int POST_MAX_SIZE = 3;
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 글쓰기 내용 db에 insert 구문 - 글 추가
	public int addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		
		// multipartFile을 그대로 dao에 보낼 수 없다.
		// 때문에 파일 업로드를 먼저 bo에서 해야함
		// 파일 업로드 => 경로
		String imagePath = null;
		if (file != null) { // 파일이 있으면 순차적으로 기능 수행됨 - 파일 있을때만 수행하고 이미지 경로를 얻어낸다.
			imagePath = fileManagerService.saveFile(userLoginId, file); 
		}
		
		// dao insert
		return postDAO.insertPost(userId, subject, content, imagePath);
	}
	
	// 글 수정
	public void updatePost(int userId, String userLoginId,
			 int postId, String subject, String content, MultipartFile file) {
		
		// 기존 글을 가져온다. (이미지가 교체될 때 기존 이미지 제거를 위해서) - 보통 BO가 가공을 하는 역할을 하기 때문에 BO를 불러주는게 안전하다.
		Post post = getPostByPostIdUserId(postId, userId);
		if (post == null) {
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId:{}, userId:{}", postId, userId); // 본인이 원하는 방향으로 error, warn, ingo 등 선택해서 사용해주자
			return; // 수정 할 글이 없다면 그대로 return 해주자.
		}
		
		// 멀티파일이 비어있지 않다면 업로드 후 imagePath를 받아와야 함 - 만약에 업로드가 성공하면 기존 이미지 제거
		String imagePath = null;
		if (file != null) {
			// 업로드
			imagePath = fileManagerService.saveFile(userLoginId, file);
			
			// 업로드 성공하면 기존 이미지 제거 => 업로드가 실패할 수 있으므로 업로드가 성공한 후 제거할 것
			// imagePath가 널이 아니고, 기존 글에 이미지 패스가 널이 아닐 경우
			if(imagePath != null && post.getImagePath() != null) { // 업로드 성공 && 기존 이미지 있을 때 실행
				// 이미지 제거
				fileManagerService.deleteFile(post.getImagePath()); // 여기서 imagePath를 그대로 넘기면 방금 업로드를 한 사진이 삭제 되기 때문에 post.getImagePath()으로 수정해줘야함
			}
		}
		
		// db 업데이트
		postDAO.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
	}
	
	// 글 삭제
	public int deletePostByPostIdUserId(int postId, int userId) {
		// dao가 삭제를 그대로 하면 이미지가 남아 있기 때문에
		// 기존 글 가져오기 - null체크 무조건 해줘야 함. 아니면 npe mpe 오류가 발생
		Post post = getPostByPostIdUserId(postId, userId);
		if (post == null) {
			logger.warn("[글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return 0;
		}
		
		// 업로드 된 이미지가 있으면 파일 삭제
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath()); // 이미지 패스만 주면 사진을 삭제 해준다.
		}
		
		// DB delete문 실행
		return postDAO.deletePostByPostIdUserId(postId, userId);
	}
	
	// 글 목록 가져오기 (하나하나의 행들을 가져옴) - 로그인 된 사람의 것만 가져온다. - 페이징 구문 추가
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호:  10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 4 3 2 페이지에 있을 때
		// 1) 이전을 눌렀을 때 : 정방향(ASC) 4보다 큰 3개 (5, 6, 7) => List reverse (7, 6, 5)
		// 2) 다음을 눌렀을 때 : 2보다 작은 3개 DESC
		// 3) 첫페이지 (이전, 다음 없음) DESC 3개
		String direction = null; // 방향
		Integer standardId = null; // 기준 postId (첫페이지)
		if (prevId != null) { // 이전
			direction = "prev";
			standardId = prevId;
			
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList); // 뒤집어주는 문법
			return postList;
			
		} else if (nextId != null) { // 다음
			direction ="next";
			standardId = nextId;
		}
		
		// 첫페이지일 때 (페이징X) - standardId, direction이 null
		// 다음일 때 standardId, direction 채워져서 넘어감
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 이전 페이지 없애는 구문
	public boolean isPrevLastPage(int prevId, int userId) {
		int maxPostId = postDAO.selectPostIdByuserIdSort(userId, "DESC");
		return maxPostId == prevId ? true : false;
	}
	
	// 다음 페이지 없애는 구문
	public boolean isNextLastPage(int nextId, int userId) {
		int minPostId = postDAO.selectPostIdByuserIdSort(userId, "ASC");
		return minPostId == nextId ? true : false;
	}
	
	public Post getPostByPostIdUserId(int postId, int userId) {
		
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}
}
