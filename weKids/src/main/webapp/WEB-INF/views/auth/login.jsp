<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인 - We-Kids</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/common/theme.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/auth/login.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
	<div class="login-page">
		<div class="login-container">
			<div class="login-card">
		
				<div class="login-header">
					<a href="${pageContext.request.contextPath}/"
						class="login-logo-link"> <span class="login-logo-circle">
							<i class="fa-solid fa-book-open"></i>
					</span>
					</a>
					<h1 class="login-title">환영합니다!</h1>
					<p class="login-subtitle">아이디와 비밀번호를 입력해주세요.</p>
				</div>
				
				<c:if test="${param.error == '1'}">
					<div class="login-alert login-alert-error">
						<div class="alert-icon">
							<i class="fa-solid fa-circle-exclamation"></i>
						</div>
						<div class="alert-content">
							<strong>로그인 실패</strong>
							<p>아이디 혹은 비밀번호가 일치하지 않습니다.</p>
						</div>
					</div>
				</c:if>
				<c:if test="${not empty msg}">
					<div class="login-alert login-alert-info">
						<div class="alert-icon">
							<i class="fa-solid fa-circle-check"></i>
						</div>
						<div class="alert-content">
							<strong>안내</strong>
							<p>${msg}</p>
						</div>
					</div>
				</c:if>
				<c:if test="${param.logout == '1'}">
					<div class="login-alert login-alert-info">
						<div class="alert-icon">
							<i class="fa-solid fa-circle-info"></i>
						</div>
						<div class="alert-content">
							<strong>로그아웃 완료</strong>
							<p>정상적으로 로그아웃되었습니다.</p>
						</div>
					</div>
				</c:if>

				<c:if test="${param.locked == '1'}">
					<div class="login-alert login-alert-warn">
						<div class="alert-icon">
							<i class="fa-solid fa-lock"></i>
						</div>
						<div class="alert-content">
							<strong>계정 잠금</strong>
							<p>로그인 실패가 누적되어 계정이 잠겼습니다. 잠시 후 다시 시도해주세요.</p>
						</div>
					</div>
				</c:if>

				<form class="login-form"
					    action="${pageContext.request.contextPath}/auth/login"
					    method="post">

					<div class="form-group">
						<label for="login_id">아이디</label>
						<div class="input-wrap">
							<span class="input-icon"> <i class="fa-regular fa-user"></i>
							</span> <input type="text" id="login_id" name="login_id"
								value="${param.login_id}" placeholder="아이디를 입력해주세요"
								autocomplete="username">
						</div>
					</div>

					<div class="form-group">
						<label for="pwd">비밀번호</label>
						<div class="input-wrap">
							<span class="input-icon"> <i class="fa-solid fa-lock"></i>
							</span> <input type="password" id="pwd" name="pwd"
								placeholder="비밀번호를 입력해주세요" autocomplete="current-password">
						</div>
					</div>

					<button type="submit" class="login-btn">로그인</button>
				</form>

				<div class="login-notice-box">
					<div class="notice-row">
						<span class="notice-icon"><i
							class="fa-solid fa-circle-info"></i></span>
						<div class="notice-text">
							<p>
								<strong>학생</strong>: 학교에서 발급한 아이디로 로그인합니다.
							</p>
							<p>
								<strong>보호자</strong>: 학생의 연동코드로 회원등록한 뒤 로그인합니다.
							</p>
							<!-- <p>
								<strong>최초 로그인</strong>: 비밀번호 변경 이력이 없으면 로그인 후 계정 설정 페이지로 이동합니다.
							</p> -->
						</div>
					</div>
				</div>

				<div class="parent-register-section">
					<a href="${pageContext.request.contextPath}/auth/parent-register"
						class="parent-register-link">
						<div class="parent-register-left">
							<div class="parent-register-icon">
								<i class="fa-solid fa-user-plus"></i>
							</div>
							<div class="parent-register-text">
								<p class="parent-register-title">보호자 등록</p>
								<p class="parent-register-desc">학부모님은 여기서 계정을 만드세요</p>
							</div>
						</div> <span class="parent-register-arrow">등록하기 <i
							class="fa-solid fa-angle-right"></i></span>
					</a>
				</div>

			</div>
		</div>
	</div>
</body>
</html>