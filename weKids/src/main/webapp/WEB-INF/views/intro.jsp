<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>We-Kids</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/intro.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div class="intro-page">
        <header class="intro-header">
            <div class="logo-wrap">
                <i class="fa-solid fa-book-open logo-icon"></i>
                <span class="logo-text">We-Kids</span>
            </div>
        </header>

        <main class="intro-main">
            <h1 class="intro-title">
                우리 학교의 새로운<br class="mobile-break">
                <span class="brand-text">온라인 교실</span>
            </h1>

            <p class="intro-desc">
                교사, 학생, 학부모가 함께 만드는 안전하고 즐거운 교육 커뮤니티입니다.
                지금 바로 시작해보세요.
            </p>

            <a href="${pageContext.request.contextPath}/auth/login" class="start-btn">
                <span>시작하기</span>
                <i class="fa-solid fa-arrow-right btn-icon"></i>
            </a>

            <p class="intro-subtext">
                학생 · 학부모 · 교사 · 관리자 모두 로그인 페이지에서 시작합니다
            </p>

            <div class="intro-card-section">
                <div class="intro-card student-card">
                    <div class="card-badge badge-student">학</div>
                    <h3 class="card-title">학생을 위한 공간</h3>
                    <p class="card-desc">
                        학교에서 발급받은 아이디로 로그인하여 과제를 제출하고 학습 자료를 확인합니다.
                    </p>
                </div>

                <div class="intro-card teacher-card">
                    <div class="card-badge badge-teacher">교</div>
                    <h3 class="card-title">교사를 위한 관리</h3>
                    <p class="card-desc">
                        클래스를 개설하고 학생들의 학습 현황과 과제를 효율적으로 관리합니다.
                    </p>
                </div>

                <div class="intro-card parent-card">
                    <div class="card-badge badge-parent">부</div>
                    <h3 class="card-title">학부모의 참여</h3>
                    <p class="card-desc">
                        자녀의 연결 코드로 보호자 등록 후 학습 상태와 가정통신문을 확인합니다.
                    </p>
                </div>
            </div>
        </main>
    </div>
</body>
</html>