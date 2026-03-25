<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/class/class-home-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-class-home.css">

<section class="class-home-page student-class-home">
    <div class="class-hero-card">
        <div class="class-hero-overlay"></div>
        <div class="class-hero-content">
            <h1>${className}</h1>
            <p>안녕하세요, ${studentName}님! 우리들의 즐거운 온라인 배움터입니다.</p>
        </div>
    </div>

    <section class="class-home-panel">
        <div class="summary-card summary-blue">
            <div class="summary-icon">📘</div>
            <div class="summary-title">이번 주 학습 진행률</div>
            <div class="summary-value">${weeklyProgress}%</div>
        </div>

        <div class="summary-card summary-red">
            <div class="summary-icon">📋</div>
            <div class="summary-title">미제출 과제</div>
            <div class="summary-value">${pendingAssignmentCount}건</div>
        </div>

        <div class="summary-card summary-purple">
            <div class="summary-icon">📊</div>
            <div class="summary-title">새 리포트 및 코멘트</div>
            <div class="summary-value">${newReportMessage}</div>
        </div>
    </section>

    <section class="class-home-section">
        <div class="section-header">
            <h2>최근 가정통신문</h2>
        </div>

        <div class="notice-list">
            <c:forEach var="notice" items="${recentBulletins}">
                <a class="notice-item" href="${pageContext.request.contextPath}/student/classes/${classId}/bulletins/${notice.id}">
                    <div class="notice-left">
                        <c:if test="${notice.important}">
                            <span class="notice-badge">필독</span>
                        </c:if>
                        <span class="notice-title">${notice.title}</span>
                    </div>
                    <div class="notice-date">${notice.date}</div>
                </a>
            </c:forEach>

            <c:if test="${empty recentBulletins}">
                <div class="empty-box">등록된 가정통신문이 없습니다.</div>
            </c:if>
        </div>
    </section>
</section>