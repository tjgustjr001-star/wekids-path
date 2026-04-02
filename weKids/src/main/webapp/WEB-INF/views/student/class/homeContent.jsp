<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/class/class-home-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-class-home.css">

<section class="class-home-page student-class-home">
    <div class="class-hero-card class-hero-card--plain">
        <div class="class-hero-overlay"></div>
        <div class="class-hero-content">
            <h1>${className}</h1>
            <p>안녕하세요, ${studentName}님! 우리들의 즐거운 온라인 배움터입니다.</p>
        </div>
    </div>

    <section class="class-home-panel student-summary-grid">
        <a href="${pageContext.request.contextPath}/student/classes/${classId}/learns" class="summary-card summary-blue summary-card--link">
            <div class="summary-icon summary-icon-book" aria-hidden="true"></div>
            <div class="summary-title">이번 주 학습 진행률</div>
            <div class="summary-value summary-value--green">${weeklyProgress}%</div>
        </a>

        <a href="${pageContext.request.contextPath}/student/classes/${classId}/assignments" class="summary-card summary-red summary-card--link">
            <div class="summary-icon summary-icon-clipboard" aria-hidden="true"></div>
            <div class="summary-title">미제출 과제</div>
            <div class="summary-value summary-value--red">${pendingAssignmentCount}건</div>
            <c:if test="${not empty latestAssignmentTitle}">
                <div class="summary-caption">${latestAssignmentTitle}</div>
            </c:if>
        </a>

        <a href="${pageContext.request.contextPath}/student/classes/${classId}/reports" class="summary-card summary-purple summary-card--link">
            <div class="summary-icon summary-icon-chart" aria-hidden="true"></div>
            <div class="summary-title">새 리포트 및 코멘트</div>
            <div class="summary-value summary-value--purple">${newReportMessage}</div>
            <c:if test="${hasReport and not empty latestReportTitle}">
                <div class="summary-caption">${latestReportTitle}</div>
            </c:if>
        </a>
    </section>

    <section class="class-home-section">
        <div class="section-header section-header-inline">
            <h2>최근 가정통신문</h2>
            <a class="detail-link" href="${pageContext.request.contextPath}/student/classes/${classId}/bulletins">전체보기</a>
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
