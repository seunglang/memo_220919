<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 쓰기</h1>

		<input type="text" class="form-control" id="subject"
			placeholder="제목을 입력하세요">
		<textarea class="form-control" id="content" placeholder="내용을 입력하세요"
			rows="15"></textarea>

		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg,.jpeg,.png,.gif">
		</div>


		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>

			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두
					지우기</button>
				<button type="button" id="postCreateBtn" class="btn btn-primary">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// 목록 버튼 클릭 => 글 목록으로 이동
		$('#postListBtn').on('click', function() {
			location.href = "/post/post_list_view";
		});

		// 모두 지우기 버튼 클릭
		$('#clearBtn').on('click', function() {
			// val안에 "" 을 넣음으로써 input 값의 벨류들을 다 빈칸으로 비워버린다.
			$('#subject').val("");
			$('#content').val("");
		});

		// 글 저장 버튼
		$('#postCreateBtn').on('click', function() {

			let subject = $('#subject').val().trim();
			let content = $('#content').val();

			// validation
			if (subject == "") {
				alert("제목을 입력하세요");
				return;
			}
			// console.log(content);

			let file = $('#file').val(); //C:\fakepath\more-icon.png 이런식으로 저장이 되어있음 - 실질적인 파일은 아니며 파일명만 들고온 것

			// 파일이 업로드 된 경우에만 확장자 체크
			if (file != '') {
				//alert(file.split('.').pop().toLowerCase()); // 배열의 마지막 방에 있는 칸을 가져오는 함수 - lowerCase하면 소문자가 나옴
				let ext = file.split('.').pop().toLowerCase();
				if ($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) { // 이 배열에 있냐라는걸 묻는 뜻 inArray - 위에 값이 없으면 -1을 리턴함
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val(""); // 파일을 비운다 - 왜냐면 사용자가 정해진 확장자 이 외에 파일을 선택 했기 때문.
					return;
				}
			}
			
			// 서버 - ajax 통신으로 보낼 것이다.
			
			// 이미지를 업로드 할 때는 form 태그가 있어야 한다.(자바스크립트에서 만듬)
			// append로 넣는 값은 폼태그에 name으로 넣는 것과 같다.(request parameter)
			let formData = new FormData();
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]); // file태그 중 0번째 배열 방에 있는 사진을 가져오는 구문 - 플젝할땐 배열 이용 검색해서 해보자
			
			// ajax 통신으로 formData에 있는 데이터 전송
			$.ajax({
				// request
				type:"POST"
				, url:"/post/create"
				, data:formData  // 폼객체를 통째로
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false  // 파일 업로드를 위한 필수 설정
				, contentType:false  // 파일 업로드를 위한 필수 설정
				
				// response
				, success:function(data) {
					if (data.code == 1) {
						// 성공
						alert("메모가 저장되었습니다.");
						location.href = "/post/post_list_view";
					} else {
						// 실패
						alert(data.errorMessage);
					}
				}
				, error:function(e) {
					alert("메모 저장에 실패했습니다.");
				}
			});
		});
	});
</script>