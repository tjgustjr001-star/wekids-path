<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<aside class="teacher-sidebar sidebar">
    <div class="sidebar-logo">
        <a href="${pageContext.request.contextPath}/teacher">We-Kids</a>
    </div>

    <div class="sidebar-user-box">
        <div class="sidebar-user-icon">
            <span class="user-symbol"></span>
        </div>

        <div class="sidebar-user-info">
            <strong class="sidebar-user-name">${empty userName ? '김교사' : userName}</strong>
            <span class="sidebar-user-role">${empty roleName ? '교사 계정' : roleName}</span>
        </div>

        <form action="${pageContext.request.contextPath}/auth/logout" method="post" class="sidebar-logout-form">
            <button type="submit" class="sidebar-logout-btn">
                <span class="logout-icon" aria-hidden="true"></span>
                <span class="logout-text">로그아웃</span>
            </button>
        </form>
    </div>

    <nav class="sidebar-nav">
        <c:choose>
            <c:when test="${isClassDetail}">
                <div class="class-nav-top">
                    <a href="${pageContext.request.contextPath}/teacher/classes" class="back-to-list-link">
                        ‹ 클래스 목록으로
                    </a>

                    <p class="current-class-label">현재 클래스</p>
                    <strong class="current-class-name">${className}</strong>
                </div>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId) ? 'active' : ''}">
                    <span class="menu-icon home-icon"></span>
                    <span>클래스 홈</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/manage"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/manage') ? 'active' : ''}">
                    <span class="menu-icon settings-mini-icon"></span>
                    <span>클래스 설정</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/students"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/students') ? 'active' : ''}">
                    <span class="menu-icon users-mini-icon"></span>
                    <span>학생 관리</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/bulletins"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/bulletins') ? 'active' : ''}">
                    <span class="menu-icon bell-mini-icon"></span>
                    <span>가정통신문</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/learns"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/learns') ? 'active' : ''}">
                    <span class="menu-icon book-mini-icon"></span>
                    <span>학습 관리</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/assignments"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/assignments') ? 'active' : ''}">
                    <span class="menu-icon calendar-mini-icon"></span>
                    <span>과제 관리</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/reports"
                   class="sidebar-menu ${currentUri eq '/teacher/classes/'.concat(classId).concat('/reports') ? 'active' : ''}">
                    <span class="menu-icon chart-mini-icon"></span>
                    <span>리포트 관리</span>
                </a>
            </c:when>

            <c:otherwise>
                <p class="sidebar-nav-title">메인 메뉴</p>

                <a href="${pageContext.request.contextPath}/teacher"
                   class="sidebar-menu ${currentUri eq '/teacher' ? 'active' : ''}">
                    <span class="menu-icon home-icon"></span>
                    <span>교사 홈</span>
                </a>

                <a href="${pageContext.request.contextPath}/teacher/classes"
                   class="sidebar-menu ${currentUri eq '/teacher/classes' ? 'active' : ''}">
                    <span class="menu-icon class-icon"></span>
                    <span>클래스 목록</span>
                </a>
            </c:otherwise>
        </c:choose>
    </nav>
</aside>