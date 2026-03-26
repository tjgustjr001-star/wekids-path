<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-class-home.css">

<section class="parent-class-home">

    <!-- 상단 히어로 -->
    <section class="parent-class-home-hero">
        <div class="parent-class-home-hero__bg"></div>
        <div class="parent-class-home-hero__content">
            <h1 class="parent-class-home-hero__title">
                ${className != null ? className : '2026학년도 3학년 2반'}
            </h1>
            <p class="parent-class-home-hero__desc">
                안녕하세요,
                ${loginUserName != null ? loginUserName : '김학부모'}님!
                우리들의 즐거운 온라인 배움터입니다.
            </p>
        </div>
    </section>

    <section class="parent-class-home-body">
        <!-- 요약 카드 -->
        <div class="parent-summary-grid">
            <!-- 학습 현황 -->
            <a href="${pageContext.request.contextPath}/parent/children/${classId}/learns" class="summary-card summary-card--progress">
                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">자녀 학습 현황 (${childName != null ? childName : '김민수'})</h3>
                        <p class="summary-card__value">이번 주 진도율 ${progressPercent != null ? progressPercent : 85}%</p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--blue">학습</div>
                </div>

                <div class="progress-bar">
                    <div class="progress-bar__fill" style="width: ${progressPercent != null ? progressPercent : 85}%;"></div>
                </div>

                <p class="summary-card__sub">
                    목표치 달성까지 ${remainPercent != null ? remainPercent : 15}% 남았습니다.
                </p>
            </a>

            <!-- 미제출 과제 -->
            <a href="${pageContext.request.contextPath}/parent/classes/${classId}/assignments" class="summary-card summary-card--danger">
                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">확인 필요한 과제</h3>
                        <p class="summary-card__value summary-card__value--danger">
                            미제출 ${pendingAssignmentCount != null ? pendingAssignmentCount : 1}건
                        </p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--red">과제</div>
                </div>

                <p class="summary-card__text">
                    · ${pendingAssignmentTitle != null ? pendingAssignmentTitle : '수학 3단원 형성평가'} 
                    (${pendingAssignmentDeadline != null ? pendingAssignmentDeadline : '내일까지'})
                </p>
            </a>

            <!-- 리포트 -->
            <a href="${pageContext.request.contextPath}/parent/classes/${classId}/reports" class="summary-card summary-card--report">
                <c:if test="${not empty hasUnreadReport && hasUnreadReport}">
                    <span class="summary-card__dot"></span>
                </c:if>

                <div class="summary-card__top">
                    <div>
                        <h3 class="summary-card__label">선생님 리포트</h3>
                        <p class="summary-card__value summary-card__value--purple">
                            <c:choose>
                                <c:when test="${not empty hasUnreadReport && hasUnreadReport}">
                                    주간 종합 리포트 도착!
                                </c:when>
                                <c:otherwise>
                                    확인 완료
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <div class="summary-card__icon summary-card__icon--purple">리포트</div>
                </div>

                <p class="summary-card__text summary-card__text--purple">
                    ${teacherName != null ? teacherName : '김선생'} 선생님의 코멘트가 포함되어 있습니다.
                </p>
            </a>
        </div>

        <!-- 최근 메시지 & 가정통신문 -->
        <section class="parent-home-section">
            <h2 class="parent-home-section__title">선생님 최근 메시지 &amp; 가정통신문</h2>

            <div class="message-card">
                <c:if test="${empty isNoticeRead || !isNoticeRead}">
                    <span class="message-card__dot"></span>
                </c:if>

                <a href="${pageContext.request.contextPath}/parent/classes/${classId}/bulletins" class="message-card__link">
                    <div class="message-card__header">
                        <div class="message-card__avatar">선</div>
                        <div>
                            <div class="message-card__author">${teacherName != null ? teacherName : '김선생'} 선생님</div>
                            <div class="message-card__time">${recentMessageTime != null ? recentMessageTime : '어제 오후 2:30'}</div>
                        </div>
                    </div>

                    <p class="message-card__body">
                        ${recentMessage != null ? recentMessage : '안녕하세요 학부모님! 민수가 이번 주 수학 단원평가 과제를 모두 제출하고 적극적으로 참여했습니다. 가정에서도 많은 칭찬 부탁드립니다.'}
                    </p>
                </a>
            </div>

            <c:if test="${not empty notices}">
                <div class="notice-list">
                    <c:forEach var="notice" items="${notices}" begin="0" end="0">
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