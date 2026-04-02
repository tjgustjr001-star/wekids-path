<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-class-home.css">

<section class="parent-class-home">
    <section class="parent-class-home-hero">
        <div class="parent-class-home-hero__bg"></div>
        <div class="parent-class-home-hero__content">
            <h1 class="parent-class-home-hero__title">${className}</h1>
            <p class="parent-class-home-hero__desc">안녕하세요, ${loginUserName}님! 우리들의 즐거운 온라인 배움터입니다.</p>
        </div>
    </section>

    <section class="parent-class-home-body">
        <div class="parent-summary-grid">
            <a href="${pageContext.request.contextPath}/parent/classes/${classId}/reports?studentId=${selectedChildId}" class="summary-card summary-card--progress">
                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">자녀 학습 현황<c:if test="${not empty childName}"> (${childName})</c:if></h3>
                        <p class="summary-card__value">이번 주 진도율 ${progressPercent}%</p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--blue summary-card__icon--book" aria-hidden="true"><span class="sr-only">학습</span></div>
                </div>
                <div class="progress-bar">
                    <div class="progress-bar__fill" style="width: ${progressPercent}%;"></div>
                </div>
                <p class="summary-card__sub">목표치 달성까지 ${remainPercent}% 남았습니다.</p>
            </a>

            <a href="${pageContext.request.contextPath}/parent/classes/${classId}/assignments?childId=${selectedChildId}" class="summary-card summary-card--danger">
                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">확인 필요한 과제</h3>
                        <p class="summary-card__value summary-card__value--danger">미제출 ${pendingAssignmentCount}건</p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--red summary-card__icon--clipboard" aria-hidden="true"><span class="sr-only">과제</span></div>
                </div>
                <p class="summary-card__text">· ${pendingAssignmentTitle}<c:if test="${not empty pendingAssignmentDeadline}"> (${pendingAssignmentDeadline})</c:if></p>
            </a>

            <a href="${pageContext.request.contextPath}/parent/classes/${classId}/reports?studentId=${selectedChildId}" class="summary-card summary-card--report">
                <c:if test="${hasUnreadReport}">
                    <span class="summary-card__dot"></span>
                </c:if>
                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">선생님 리포트</h3>
                        <p class="summary-card__value summary-card__value--purple">
                            <c:choose>
                                <c:when test="${hasUnreadReport}">주간 종합 리포트 도착!</c:when>
                                <c:otherwise>확인 완료</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--purple summary-card__icon--chart" aria-hidden="true"><span class="sr-only">리포트</span></div>
                </div>
                <p class="summary-card__text summary-card__text--purple">${teacherName} 선생님의 코멘트가 포함되어 있습니다.</p>
            </a>
        </div>

        <section class="parent-home-section">
            <h2 class="parent-home-section__title">선생님 최근 메시지 &amp; 가정통신문</h2>

            <div class="message-card">
                <c:if test="${not isNoticeRead}">
                    <span class="message-card__dot"></span>
                </c:if>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}/bulletins" class="message-card__link">
                    <div class="message-card__header">
                        <div class="message-card__avatar">선</div>
                        <div>
                            <div class="message-card__author">${teacherName} 선생님</div>
                            <div class="message-card__time">${recentMessageTime}</div>
                        </div>
                    </div>
                    <p class="message-card__body">${recentMessage}</p>
                </a>
            </div>

            <c:if test="${not empty notices}">
                <div class="notice-list">
                    <c:forEach var="notice" items="${notices}">
                        <a href="${pageContext.request.contextPath}/parent/classes/${classId}/bulletins" class="notice-item">
                            <div class="notice-item__left">
                                <c:if test="${notice.important}">
                                    <span class="notice-badge">필독</span>
                                </c:if>
                                <span class="notice-item__title">${notice.title}</span>
                            </div>
                            <span class="notice-item__date">${notice.date}</span>
                        </a>
                    </c:forEach>
                </div>
            </c:if>
        </section>
    </section>
</section>
