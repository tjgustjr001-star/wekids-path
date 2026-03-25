<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/admin/admin.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<div class="admin-wrap">

    <!-- 사이드바 -->
    <aside class="sidebar">
        <div class="sidebar-logo">AdminCenter</div>

        <nav class="sidebar-nav">
            <div class="nav-title">시스템 관리</div>

            <a href="${pageContext.request.contextPath}/admin/home"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/home') || fn:endsWith(pageContext.request.requestURI, '/admin') ? 'active' : ''}">
                관리자 홈
            </a>

            <a href="${pageContext.request.contextPath}/admin/teachers"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/teachers') ? 'active' : ''}">
                교사 관리
            </a>

            <a href="${pageContext.request.contextPath}/admin/classes"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/classes') ? 'active' : ''}">
                클래스 관리
            </a>

            <a href="${pageContext.request.contextPath}/admin/users"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/users') ? 'active' : ''}">
                사용자 관리
            </a>

            <a href="${pageContext.request.contextPath}/admin/support"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/support') ? 'active' : ''}">
                고객지원
            </a>

            <a href="${pageContext.request.contextPath}/admin/logs"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/logs') ? 'active' : ''}">
                로그
            </a>

            <a href="${pageContext.request.contextPath}/admin/stats"
               class="nav-item ${fn:contains(pageContext.request.requestURI, '/admin/stats') ? 'active' : ''}">
                통계
            </a>
        </nav>

        <div class="sidebar-bottom">
            <form action="${pageContext.request.contextPath}/auth/logout" method="post">
                <button type="submit" class="logout-btn">로그아웃</button>
            </form>
        </div>
    </aside>

    <!-- 메인 영역 -->
    <div class="main-area">
        <header class="top-header">
            <div class="top-header-right">
                <span class="admin-badge">최고 관리자</span>
            </div>
        </header>

        <main class="content-area">
            <jsp:include page="${contentPage}" />
        </main>
    </div>

</div>

<script src="${pageContext.request.contextPath}/resources/js/admin/admin.js"></script>
</body>
</html>