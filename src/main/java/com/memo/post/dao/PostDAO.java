package com.memo.post.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
public List<Map<String, Object>> selectPostListTest();
	
//	글쓴 데이터 db에 insert 구문
	public int insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// 글 목록 가져오기 - 로그인 된 유저의 id로 - 페이징 추가 구문
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId, 
			@Param("direction") String direction,
			@Param("standardId") Integer standardId,
			@Param("limit") int limit);
	
	// 페이징 - 공용으로 사용 예정
	public int selectPostIdByuserIdSort(
			@Param("userId") int userId,
			@Param("sort") String sort);
	
	
	// 글 선택
	public Post selectPostByPostIdUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId);
	
	
	// 글 수정
	public void updatePostByPostIdUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// 글 삭제
	public int deletePostByPostIdUserId(
			@Param("postId") int postId,
			@Param("userId") int userId);
	
//	public List<Map<String, Object>> selectListsTest();
//	
//	// 글쓴 데이터 db에 insert 구문
//	public int insertPost(
//			@Param("userId") int userId,
//			@Param("subject") String subject,
//			@Param("content") String content,
//			@Param("imagePath") String imagePath);
//	
//	// 글 목록 가져오기 - 로그인 된 유저의 id로
//	public List<Post> selectPostListByUsedId(int userId);
//	
//	// 글 선택
//	public Post getPostByPostIdUserId(
//			@Param("postId") int postId,
//			@Param("userId") int userId);
	
}
