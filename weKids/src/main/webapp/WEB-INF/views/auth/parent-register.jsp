<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>보호자 등록 - We-Kids</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/theme.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/auth/parent-register.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
	<div class="parent-register-page">

		<header class="parent-register-header">
			<a href="${pageContext.request.contextPath}/auth/login"
				class="back-link"> <i class="fa-solid fa-arrow-left"></i> <span>로그인으로
					돌아가기</span>
			</a>

			<div class="header-logo">
				<i class="fa-solid fa-book-open"></i> <span>We-Kids</span>
			</div>
		</header>

		<div class="parent-register-container">
			<div class="title-section">
				<div class="title-icon">
					<i class="fa-solid fa-heart"></i>
				</div>
				<h1>보호자 등록</h1>
				<p>자녀의 연결 코드로 간편하게 등록하고 학습 현황을 확인하세요</p>
			</div>

			<div class="step-indicator">
				<div class="step active">
					<span class="step-circle">1</span> <span class="step-text">자녀
						확인</span>
				</div>
				<span class="step-line"></span>
				<div class="step">
					<span class="step-circle">2</span> <span class="step-text">정보
						입력</span>
				</div>
				<span class="step-line"></span>
				<div class="step">
					<span class="step-circle"><i class="fa-solid fa-sparkles"></i></span>
					<span class="step-text">완료</span>
				</div>
			</div>

			<c:if test="${not empty error}">
				<div class="page-alert error">
					<i class="fa-solid fa-circle-exclamation"></i> <span>${error}</span>
				</div>
			</c:if>

			<c:if test="${param.success == '0'}">
				<div class="page-alert error">
					<i class="fa-solid fa-circle-exclamation"></i> <span>보호자 등록에
						실패했습니다. 입력값을 다시 확인해주세요.</span>
				</div>
			</c:if>

			<form class="parent-register-form"
				action="${pageContext.request.contextPath}/auth/parent-register"
				method="post">

				<!-- Step 1 -->
				<section class="form-card highlight-card">
					<div class="card-header pink">
						<div class="card-title-wrap">
							<i class="fa-solid fa-key"></i>
							<h3>자녀 연결 코드</h3>
						</div>
					</div>

					<div class="card-body">
						<div class="connection-code-row">
							<input type="text" id="parent_link_code" name="parent_link_code"
								value="${param.parent_link_code}" class="connection-code-input"
								placeholder="ABCD1234" maxlength="8">

							<button type="button" class="verify-btn"
								onclick="verifyParentLinkCode();">코드 확인</button>
						</div>

						<p class="form-guide">자녀의 학생 계정 설정에서 발급된 8자리 보호자 연결 코드를
							입력해주세요.</p>

						<div id="codeCheckResult" class="inline-check-result"></div>
					</div>
				</section>

				<!-- Step 2 Account -->
				<section class="form-card">
					<div class="card-header gray">
						<div class="card-title-wrap">
							<i class="fa-regular fa-user"></i>
							<h3>보호자 계정 정보</h3>
						</div>
					</div>

					<div class="card-body">
						<div class="form-group">
							<label for="login_id">로그인 아이디 *</label>
							<div class="inline-input-row">
								<div class="input-icon-wrap">
									<span class="left-icon"><i class="fa-regular fa-user"></i></span>
									<input type="text" id="login_id" name="login_id"
										value="${param.login_id}" placeholder="사용할 아이디를 입력해주세요">
								</div>

								<button type="button" class="sub-btn dark"
									onclick="checkLoginId();">중복 확인</button>
							</div>
							<div id="idCheckResult" class="inline-check-result"></div>
						</div>

						<div class="form-group">
							<label for="pwd">비밀번호 *</label>
							<div class="input-icon-wrap">
								<span class="left-icon"><i class="fa-solid fa-lock"></i></span>
								<input type="password" id="pwd" name="pwd"
									placeholder="8자 이상 영문, 숫자, 특수문자 포함">
							</div>
						</div>

						<div class="form-group">
							<label for="pwd_confirm">비밀번호 확인 *</label>
							<div class="input-icon-wrap">
								<span class="left-icon"><i class="fa-solid fa-lock"></i></span>
								<input type="password" id="pwd_confirm" name="pwd_confirm"
									placeholder="비밀번호를 한번 더 입력해주세요">
							</div>
						</div>
					</div>
				</section>

				<!-- Step 2 Profile -->
				<section class="form-card">
					<div class="card-header gray">
						<div class="card-title-wrap">
							<i class="fa-solid fa-heart"></i>
							<h3>프로필 정보</h3>
						</div>
					</div>

					<div class="card-body">
						<div class="form-group">
							<label for="parent_name">이름 (실명) *</label>
							<div class="input-icon-wrap">
								<span class="left-icon"><i class="fa-regular fa-user"></i></span>
								<input type="text" id="parent_name" name="parent_name"
									value="${param.parent_name}" placeholder="홍길동">
							</div>
						</div>

						<div class="form-group">
							<label for="phone">휴대폰 번호 *</label>
							<div class="input-icon-wrap">
								<span class="left-icon"><i class="fa-solid fa-phone"></i></span>
								<input type="tel" id="phone" name="phone" value="${param.phone}"
									placeholder="010-1234-5678">
							</div>
						</div>

						<div class="form-group">
							<label for="email">이메일 <span class="optional">(선택)</span></label>
							<div class="input-icon-wrap">
								<span class="left-icon"><i class="fa-regular fa-envelope"></i></span>
								<input type="email" id="email" name="email"
									value="${param.email}" placeholder="example@email.com">
							</div>
						</div>
					</div>
				</section>

				<button type="submit" class="submit-btn"
					onclick="return validateParentRegisterForm();">
					<i class="fa-solid fa-users"></i> <span>보호자 등록 완료</span> <i
						class="fa-solid fa-angle-right"></i>
				</button>
			</form>

			<div class="page-footer">
				<p>
					이미 계정이 있으신가요? <a
						href="${pageContext.request.contextPath}/auth/login">로그인하기</a>
				</p>
			</div>
		</div>
	</div>

	<script>
        function validateParentRegisterForm() {
            const parentLinkCode = document.getElementById('parent_link_code').value.trim();
            const loginId = document.getElementById('login_id').value.trim();
            const pwd = document.getElementById('pwd').value;
            const pwdConfirm = document.getElementById('pwd_confirm').value;
            const parentName = document.getElementById('parent_name').value.trim();
            const phone = document.getElementById('phone').value.trim();

            if (!parentLinkCode) {
                alert('자녀 연결 코드를 입력해주세요.');
                document.getElementById('parent_link_code').focus();
                return false;
            }

            if (!loginId) {
                alert('로그인 아이디를 입력해주세요.');
                document.getElementById('login_id').focus();
                return false;
            }

            if (!pwd) {
                alert('비밀번호를 입력해주세요.');
                document.getElementById('pwd').focus();
                return false;
            }

            if (!pwdConfirm) {
                alert('비밀번호 확인을 입력해주세요.');
                document.getElementById('pwd_confirm').focus();
                return false;
            }

            if (pwd !== pwdConfirm) {
                alert('비밀번호가 일치하지 않습니다.');
                document.getElementById('pwd_confirm').focus();
                return false;
            }

            if (!parentName) {
                alert('이름을 입력해주세요.');
                document.getElementById('parent_name').focus();
                return false;
            }

            if (!phone) {
                alert('휴대폰 번호를 입력해주세요.');
                document.getElementById('phone').focus();
                return false;
            }

            return true;
        }

        function checkLoginId() {
            const loginId = document.getElementById('login_id').value.trim();
            const resultBox = document.getElementById('idCheckResult');

            if (!loginId) {
                alert('아이디를 입력해주세요.');
                document.getElementById('login_id').focus();
                return;
            }

            fetch('${pageContext.request.contextPath}/auth/id-check?login_id=' + encodeURIComponent(loginId))
                .then(response => response.text())
                .then(data => {
                    resultBox.className = 'inline-check-result show ' + (data === 'available' ? 'success' : 'error');
                    resultBox.innerText = data === 'available'
                        ? '사용 가능한 아이디입니다.'
                        : '이미 사용 중인 아이디입니다.';
                })
                .catch(() => {
                    resultBox.className = 'inline-check-result show error';
                    resultBox.innerText = '아이디 확인 중 오류가 발생했습니다.';
                });
        }

        function verifyParentLinkCode() {
            const parentLinkCode = document.getElementById('parent_link_code').value.trim();
            const resultBox = document.getElementById('codeCheckResult');

            if (!parentLinkCode) {
                alert('연결 코드를 입력해주세요.');
                document.getElementById('parent_link_code').focus();
                return;
            }

            fetch('${pageContext.request.contextPath}/auth/parent-link-code-check?parent_link_code=' + encodeURIComponent(parentLinkCode))
                .then(response => response.text())
                .then(data => {
                    resultBox.className = 'inline-check-result show ' + (data === 'valid' ? 'success' : 'error');
                    resultBox.innerText = data === 'valid'
                        ? '자녀 연결 코드가 확인되었습니다.'
                        : '유효하지 않은 연결 코드입니다.';
                })
                .catch(() => {
                    resultBox.className = 'inline-check-result show error';
                    resultBox.innerText = '연결 코드 확인 중 오류가 발생했습니다.';
                });
        }
    </script>
</body>
</html>