<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="sign-up-box">
		<h1 class="mb-4">회원가입</h1>
		<form id="signUpForm" method="post" action="/user/sign_up">
			<table class="sign-up-table table table-bordered">
				<tr>
					<th>* 아이디(4자 이상)<br></th>
					<td>
						<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId"
								class="form-control" placeholder="아이디를 입력하세요.">
							<button type="button" id="loginIdCheckBtn"
								class="btn btn-success">중복확인</button>
							<br>
						</div> <%-- 아이디 체크 결과 --%> <%-- d-none 클래스: display none (보이지 않게) --%>
						<div id="idCheckLength" class="small text-danger d-none">ID를
							4자 이상 입력해주세요.</div>
						<div id="idCheckDuplicated" class="small text-danger d-none">이미
							사용중인 ID입니다.</div>
						<div id="idCheckOk" class="small text-success d-none">사용 가능한
							ID 입니다.</div>
					</td>
				</tr>
				<tr>
					<th>* 비밀번호</th>
					<td><input type="password" id="password" name="password"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 비밀번호 확인</th>
					<td><input type="password" id="confirmPassword"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이름</th>
					<td><input type="text" id="name" name="name"
						class="form-control" placeholder="이름을 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이메일</th>
					<td><input type="text" id="email" name="email"
						class="form-control" placeholder="이메일 주소를 입력하세요."></td>
				</tr>
			</table>
			<br>

			<button type="submit" id="signUpBtn"
				class="btn btn-primary float-right">회원가입</button>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		$('#loginIdCheckBtn').on('click', function() {
			// 초기화
			$('#idCheckLength').addClass('d-none'); // 숨김으로 초기화 하는 작업 (누를 때 마다 경고문이 뜨는걸 방지함)
			$('#idCheckDuplicated').addClass('d-none'); // 숨김
			$('#idCheckOk').addClass('d-none'); //숨김

			let loginId = $('input[name=loginId]').val().trim();

			if (loginId.length < 4) {
				$('#idCheckLength').removeClass('d-none'); // 경고문구 노출
				return;
			}

			// AJAX 통신 - 중복확인
			$.ajax({
				// request - type을 안쓰면 자동 GET 방식
				url : "/user/is_duplicated_id",
				data : {
					"loginId" : loginId
				}

				// response
				,
				success : function(data) { // json 스트링으로 넘어왔지만 제이쿼리가 오브젝트 함수로 변경해줌
					if (data.code == 1) {
						// 성공
						if (data.result) {
							// 중복
							$('#idCheckDuplicated').removeClass('d-none'); // 중복일 때 문구 띄우기
						} else {
							// 사용 가능
							$('#idCheckOk').removeClass('d-none');
						}
					} else {
						// 실패
						alert(data.errorMessage);
					}
				},
				error : function(e) {
					alert("중복확인을 실패했습니다.");
				}
			});
		});
		
		// 회원가입
		$('#signUpForm').on('submit', function(e) {
			e.preventDefault(); // 서브밋 중단 구문 - 눌러도 submit 되지 않음
			
			// 로그인 ,비밀번호, 등등 비어있지 않은지 validation 추가
			/* loginId
			password
			confirmPassword
			name
			email */
			let loginId = $('#loginId').val().trim();
			let password = $('#password').val().trim();
			let confirmPassword = $('#confirmPassword').val().trim();
			let name = $('#name').val().trim()
			let email = $('#email').val().trim();
			
			if (loginId == "") {
				alert("아이디를 입력해주세요");
				return false;
			}
			if(password == "") {
				alert("비밀번호를 입력해주세요");
				return false;
			}
			if(confirmPassword == "") {
				alert("비밀번호를 입력해주세요");
				return false;
			}
			if (password != confirmPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return false;
			}
			if(name == "") {
				alert("이름을 입력해주세요");
				return false;
			}
			if(email == "") {
				alert("이메일을 입력해주세요");
				return false;
			}
			
			// 아이디 중복확인 완료 됐는지 확인 -> idCheckOk d-none을 가지고 있으면 확인하라는 alert 띄워야 함
			if($('#idCheckOk').hasClass('d-none')) {
				alert("아이디 중복확인을 다시 해주세요");
				return false;
			}
			
			// 서버로 보내는 첫 번째 방법
			// 1) submit을 동작시킨다 
			// $(this)[0].submit(); // 첫번째 폼태그를 작동시킨다는 뜻이다. - sumit이기 때문에 화면이 넘어간다. view 페이지 이동
			
			// 2) ajax 
			let url = $(this).attr('action'); // action에 들어있는 키 값이 들어가면서 url 들어간다.
			let params = $(this).serialize(); // name이 반드시 있어야 함 - form 태그에 있는 name으로 파라미터를 구성
			//console.log(params);
			
			// 이것도 이름은 post이지만 ajax이다 결국 - jquery post 함수 이런식으로 찾아보기
			// (어디로 보낼거냐, 뭘로 보낼거냐) - 파라미터
			// requestparmeter로 데이터들 넘겨서 디비에 값을 넣어보자 - 가입은 restController에 넣자
			$.post(url, params)
			.done(function(data) { //완료 되었을 때 ()안에 뭐할거냐를 넣는거임
				// response
				if(data.code == 1) { // 성공
					alert("가입을 환영합니다! 로그인 해주세요.");
					location.href = "/user/sign_in_view";
				} else { // 실패
					alert(data.errorMessage);
				}
			}); 
			
		});
		
	});
</script>