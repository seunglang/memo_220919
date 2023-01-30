package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	// 글 목록 화면
	@GetMapping("/post_list_view")
	public String postListView(
			@RequestParam(value="prevId", required=false) Integer prevIdParam,
			@RequestParam(value="nextId", required=false) Integer nextIdParam,
			Model model, HttpSession session) {
		
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign_in_view";
			// 로그인이 안된 경우 로그인 창으로 강제 리다이렉트 시킴
		}
		
		
		int prevId = 0;
		int nextId = 0;
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		
		if (postList.isEmpty() == false) { // postList가 비었을 때 에러 방지 - 리스트는 postBO가 아무것도 가져오지 않아도 null 아니라 비게 된다.
			prevId = postList.get(0).getId(); // 이거 다시 한번 생각 가져온 리스트 중 가장 앞 쪽(큰 id) - DESC
			nextId = postList.get(postList.size() - 1).getId(); // 가져온 리스트 중 가장 뒷 쪽(작은 id)
			
			// 이전 방향의 끝인가?  postList의 [0] index에 있는 값과(prevId) post 테이블의 가장 큰 값이 같으면 마지막 페이지 prevId 
			if (postBO.isPrevLastPage(prevId, userId)) { // 마지막 페이지 일 때
				prevId = 0;
			}
			
			// 다음 방향의 끝인가?  postList의 마지막 index값(nextId)와 post 테이블의 가장 작은 값이 같으면 마지막 페이지
			if (postBO.isNextLastPage(nextId, userId)) {
				nextId = 0;
			}
		}
		
		
		model.addAttribute("prevId", prevId); // 가져온 리스트 중 가장 앞 쪽(큰 id) - DESC
		model.addAttribute("nextId", nextId); // 가져온 리스트 중 가장 뒷 쪽(작은 id)
		model.addAttribute("postList", postList);
		model.addAttribute("viewName", "post/postList");
		
		return "template/layout";
	}
	
	/**
	 * 글 쓰기 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/post_create_view")
	public String postCreateView(Model model) {
		model.addAttribute("viewName", "post/postCreate");
		
		return "template/layout";
	}
	
	@GetMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			HttpSession session,
			Model model) { // session으로 로그인 된 사람껄 가져오자
		
		Integer userId = (Integer)session.getAttribute("userId"); 
		if (userId == null) {
			return "redirect:/user/sign_in_view";
		}
		
		// DB select by- userId, postId - 내가 쓴 글 하나를 가져오는 구문
		Post post = postBO.getPostByPostIdUserId(postId, userId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/postDetail");
		return "template/layout";
	}
	
	// post가 아웃풋 ()가 인풋이다.
//	public Post getPostByPostIdUserId(int postId, int userId) {
//		
//		return postDAO
//	}
}
