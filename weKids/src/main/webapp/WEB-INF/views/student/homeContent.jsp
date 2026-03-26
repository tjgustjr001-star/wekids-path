<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-home.css">
<section class="student-home-page">
    <div class="welcome-banner">
        <span class="term-badge">${termLabel}</span>
        <h1 class="welcome-title">${greetingTitle}</h1>
        <p class="welcome-desc">${greetingMessage}</p>
    </div>

    <div class="student-home-grid">
        <div class="student-home-main">
            <section class="card dashboard-card">
                <div class="section-title-row">
                    <h2 class="section-title">
                        <span class="section-icon book-icon green"></span>
                        오늘 학습 현황
                    </h2>
                </div>

                <div class="stats-row">
                    <div class="stat-box">
                        <span class="stat-label">전체 학습</span>
                        <strong class="stat-value">${learningStats.total}</strong>
                    </div>
                    <div class="stat-box stat-box-progress">
                        <span class="stat-label">진행 중</span>
                        <strong class="stat-value">${learningStats.inProgress}</strong>
                    </div>
                    <div class="stat-box stat-box-complete">
                        <span class="stat-label">완료</span>
                        <strong class="stat-value">${learningStats.completed}</strong>
                    </div>
                </div>

                <div class="progress-wrap">
                    <div class="progress-label-row">
                        <span>진행률</span>
                        <strong>${learningStats.progressPercent}%</strong>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width:${learningStats.progressPercent}%;"></div>
                    </div>
                </div>

                <div class="weekly-chart-wrap">
                    <h3 class="mini-section-title">
                        <span class="section-icon chart-icon green"></span>
                        주간 학습 시간 (시간)
                    </h3>

                    <div class="weekly-chart">
                        <c:forEach var="dayData" items="${weeklyStudyHours}">
                            <div class="chart-item">
                                <div class="chart-bar-area">
                                    <div class="chart-bar" style="height:${dayData.heightPercent}%;" title="${dayData.hours}시간"></div>
                                </div>
                                <span class="chart-day">${dayData.day}</span>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="card-action-row">
                    <a href="${pageContext.request.contextPath}/student/classes/1/learns" class="primary-action-btn">
                        이어서 학습하기
                    </a>
                </div>
            </section>

            <section class="card todo-card">
                <div class="section-title-row between">
                    <h2 class="section-title">
                        <span class="section-icon calendar-icon green"></span>
                        이번 주 해야 할 일
                    </h2>
                    <a href="${pageContext.request.contextPath}/student/classes/1/assignments" class="text-link">전체보기</a>
                </div>

                <div class="todo-list">
                    <c:forEach var="assignment" items="${assignments}">
                        <a href="${pageContext.request.contextPath}/student/classes/1/assignments/${assignment.id}" class="todo-item">
                            <div class="todo-left">
                                <div class="todo-status-icon
                                    <c:choose>
                                        <c:when test="${assignment.status eq '완료'}">done</c:when>
                                        <c:when test="${assignment.status eq '진행중'}">progress</c:when>
                                        <c:otherwise>pending</c:otherwise>
                                    </c:choose>
                                "></div>

                                <div class="todo-text-box">
                                    <div class="todo-title-row">
                                        <span class="subject-badge">${assignment.subject}</span>
                                        <strong class="todo-title">${assignment.title}</strong>
                                    </div>
                                    <p class="todo-deadline">${assignment.deadline} 마감</p>
                                </div>
                            </div>

                            <span class="todo-status-badge
                                <c:choose>
                                    <c:when test="${assignment.status eq '완료'}">done</c:when>
                                    <c:when test="${assignment.status eq '진행중'}">progress</c:when>
                                    <c:otherwise>pending</c:otherwise>
                                </c:choose>
                            ">
                                ${assignment.status}
                            </span>
                        </a>
                    </c:forEach>
                </div>
            </section>
        </div>

        <div class="student-home-side">
            <section class="card news-card">
                <div class="section-title-row between">
                    <h2 class="section-title">
                        <span class="section-icon bell-icon green"></span>
                        최근 소식
                    </h2>
                    <a href="#" class="text-link">더보기</a>
                </div>

                <div class="news-list">
                    <c:forEach var="news" items="${recentNews}">
                        <a href="#" class="news-item">
                            <span class="news-dot ${news.dotColor}"></span>
                            <div class="news-content">
                                <strong class="news-title">${news.title}</strong>
                                <p class="news-desc">${news.content}</p>
                                <span class="news-time">${news.timeText}</span>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </section>

            <section class="card current-class-card">
                <div class="current-class-icon">
                    <span class="section-icon book-icon green large"></span>
                </div>
                <h3>현재 참여중인 클래스</h3>
                <p>${currentClass.className} ${currentClass.teacherName} 선생님의 클래스입니다.</p>
                <a href="${pageContext.request.contextPath}/classes/${currentClass.classId}" class="primary-action-btn full">
                    클래스 입장하기
                </a>
            </section>
        </div>
    </div>
</section>
<script src="${pageContext.request.contextPath}/resources/js/student/student-home.js"></script>
