<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<aside class="parent-sidebar sidebar">
    <div class="sidebar-logo">
        <a href="${pageContext.request.contextPath}/parent">We-Kids</a>
    </div>

    <div class="sidebar-user-box">
        <div class="sidebar-user-icon">
            <span class="user-symbol parent-user-symbol"></span>
        </div>

        <div class="sidebar-user-info">
            <strong class="sidebar-user-name">${empty userName ? '김학부모' : userName}</strong>
            <span class="sidebar-user-role">${empty roleName ? '학부모 계정' : roleName}</span>
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
                    <a href="${pageContext.request.contextPath}/parent/classes" class="back-to-list-link">
                        ‹ 클래스 목록으로
                    </a>

                    <p class="current-class-label">현재 클래스</p>
                    <strong class="current-class-name">${className}</strong>
                </div>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}"
                   class="sidebar-menu ${currentUri eq '/parent/classes/'.concat(classId) ? 'active' : ''}">
                    <span class="menu-icon home-icon"></span>
                    <span>클래스 홈</span>
                </a>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}/bulletins"
                   class="sidebar-menu ${currentUri eq '/parent/classes/'.concat(classId).concat('/bulletins') ? 'active' : ''}">
                    <span class="menu-icon bell-mini-icon"></span>
                    <span>가정통신문</span>
                </a>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}/assignments"
                   class="sidebar-menu ${currentUri eq '/parent/classes/'.concat(classId).concat('/assignments') ? 'active' : ''}">
                    <span class="menu-icon calendar-mini-icon"></span>
                    <span>과제</span>
                </a>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}/reports"
                   class="sidebar-menu ${currentUri eq '/parent/classes/'.concat(classId).concat('/reports') ? 'active' : ''}">
                    <span class="menu-icon chart-mini-icon"></span>
                    <span>리포트</span>
                </a>
            </c:when>

            <c:otherwise>
                <p class="sidebar-nav-title">메인 메뉴</p>

                <a href="${pageContext.request.contextPath}/parent"
                   class="sidebar-menu ${currentUri eq '/parent' ? 'active' : ''}">
                    <span class="menu-icon home-icon"></span>
                    <span>학부모 홈</span>
                </a>

                <a href="${pageContext.request.contextPath}/parent/classes"
                   class="sidebar-menu ${currentUri eq '/parent/classes' ? 'active' : ''}">
                    <span class="menu-icon class-icon"></span>
                    <span>클래스 목록</span>
                </a>

                <a href="${pageContext.request.contextPath}/parent/children"
                   class="sidebar-menu ${currentUri eq '/parent/children' ? 'active' : ''}">
                    <span class="menu-icon users-mini-icon"></span>
                    <span>자녀 목록</span>
                </a>
            </c:otherwise>
        </c:choose>
    </nav>
</aside>