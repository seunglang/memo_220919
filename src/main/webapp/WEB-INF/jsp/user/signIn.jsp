<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="login-box">
		<h1 class="mb-4">로그인</h1>
		
		<%-- 키보드 Enter키로 로그인이 될 수 있도록 form 태그를 만들어준다.(submit 타입의 버튼이 동작됨) --%>
		<form id="loginForm" action="/user/sign_in" method="post">
			<div class="input-group mb-3">
				<%-- input-group-prepend: input box 앞에 ID 부분을 회색으로 붙인다. --%>
				<div class="input-group-prepend">
					<span class="input-group-text">ID</span>
				</div>
				<input type="text" class="form-control" id="loginId" name="loginId">
			</div>
	
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text">PW</span>
				</div>
				<input type="password" class="form-control" id="password" name="password">
			</div>
			
			<%-- btn-block: 로그인 박스 영역에 버튼을 가득 채운다. --%>
			<input type="submit" class="btn btn-block btn-primary" value="로그인">
			<a class="btn btn-block btn-dark" href="/user/sign_up_view">회원가입</a>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		$('#loginForm').on('submit' , function(e) {
			// 서브밋 기능 중단
			e.preventDefault();
			
			// validation
			// resturn false;를 하게 되면 그냥 빠져나가게 되고
			let loginId = $('input[name=loginId]').val().trim();
			let password = $('#password').val().trim();
			
			if(loginId.length <= 0) {
				alert("아이디를 입력해주세요");
				return false; // 부모가 on click이면 return 해도되는데 서브밋이기 때문에 false를 붙여줘야한다.
			}
			if (password == "") {
				alert("비밀번호를 입력해주세요");
				return false;
			}
			
			// 여기로 도달하면 자동 submit이 된다.
			// ajax or 서브밋 => 이번엔 ajax로 호출을 할것이다.
			
			// ajax
			let url = $(this).attr('action'); // 이 폼 태그의 action의 값이 들어간다
			console.log(url);
			let params = $(this).serialize(); // 키와 밸류로 - 쿼리스트링 request 함수로 구성이 된다.
			console.log(params);
			
			$.post(url, params) // request
			.done(function(data) { // response
				if (data.code == 1) { // 1번일 때 성공
					location.href = "/post/post_list_view"; // 안되면 document.붙이자, 화면 이동할 땐 항상 view쪽으로 - 글 목록으로 이동
				} else { // 로그인 실패
					alert(data.errorMessage);
				}
			}); // success 함수와 똑같은 기능
		});
	});
</script>