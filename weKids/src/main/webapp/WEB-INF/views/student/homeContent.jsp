<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/student/student-home.css">
<c:set var="trimmedTermLabel" value="${fn:trim(termLabel)}" />
<section class="student-home-page">
    <div class="welcome-banner">
        <c:if test="${not empty trimmedTermLabel}"><span class="term-badge">${trimmedTermLabel}</span></c:if>
        <h1 class="welcome-title">${greetingTitle}</h1>
        <p class="welcome-desc">${greetingMessage}</p>
    </div>

    <div class="student-home-grid">
        <div class="student-home-main">
            <section class="card dashboard-card">
                <div class="section-title-row between student-dashboard-head">
                    <h2 class="section-title">
                        <span class="section-icon book-icon green icon-mask"></span>
                        오늘 학습 현황
                    </h2>
                    <form method="get" action="${pageContext.request.contextPath}/student" class="student-class-select-form">
                        <select name="classId" onchange="this.form.submit()">
                            <c:forEach var="classInfo" items="${studentClassOptions}">
                                <option value="${classInfo.classId}" <c:if test="${classInfo.classId eq selectedClassId}">selected</c:if>>${classInfo.className}</option>
                            </c:forEach>
                        </select>
                    </form>
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
                        <span>학습 진행률</span>
                        <strong>${learningStats.progressPercent}%</strong>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width:${learningStats.progressPercent}%;"></div>
                    </div>
                </div>

                <div class="mini-list-section first-section">
                    <div class="section-title-row between compact">
                        <h3 class="mini-section-title">
                            <span class="section-icon book-icon green icon-mask"></span>
                            최근 학습 자료
                        </h3>
                        <a href="${pageContext.request.contextPath}${learnListUrl}" class="text-link">바로가기</a>
                    </div>

                    <c:choose>
                        <c:when test="${not empty recentLearnList}">
                            <div class="mini-list-wrap">
                                <c:forEach var="learn" items="${recentLearnList}">
                                    <a href="${pageContext.request.contextPath}${learnListUrl}" class="mini-list-item">
                                        <div class="mini-list-main">
                                            <div class="mini-list-title-row">
                                                <span class="subject-badge">${empty learn.type ? '학습' : learn.type}</span>
                                                <strong class="mini-list-title">${learn.title}</strong>
                                            </div>
                                            <p class="mini-list-meta">
                                                <c:choose>
                                                    <c:when test="${not empty learn.deadline}">${learn.deadline} 마감</c:when>
                                                    <c:otherwise>학습 자료 확인</c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                        <span class="todo-status-badge ${learn.status eq '완료' ? 'done' : (learn.status eq '진행중' ? 'progress' : 'pending')}">${empty learn.status ? '미시작' : learn.status}</span>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state-box small">최근 학습 자료가 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <section class="card todo-card">
                <div class="section-title-row between">
                    <h2 class="section-title">
                        <span class="section-icon calendar-icon green icon-mask"></span>
                        과제
                    </h2>
                    <a href="${pageContext.request.contextPath}${assignmentListUrl}" class="text-link">전체보기</a>
                </div>

                <div class="todo-list">
                    <c:choose>
                        <c:when test="${not empty assignmentList}">
                            <c:forEach var="assignment" items="${assignmentList}">
                                <a href="${pageContext.request.contextPath}${assignmentListUrl}" class="todo-item">
                                    <div class="todo-left">
                                        <div class="todo-text-box">
                                            <div class="todo-title-row">
                                                <span class="subject-badge
                                                    <c:choose>
                                                        <c:when test="${assignment.subject eq '수학'}"> subject-math</c:when>
                                                        <c:when test="${assignment.subject eq '과학'}"> subject-science</c:when>
                                                        <c:when test="${assignment.subject eq '국어'}"> subject-korean</c:when>
                                                        <c:otherwise> subject-default</c:otherwise>
                                                    </c:choose>
                                                ">${assignment.subject}</span>
                                                <strong class="todo-title">${assignment.title}</strong>
                                            </div>
                                            <p class="todo-deadline">${assignment.deadline} 마감</p>
                                        </div>
                                    </div>
                                    <span class="todo-status-badge ${assignment.status eq '제출완료' or assignment.status eq '확인완료' or assignment.status eq '늦은제출' ? 'done' : (assignment.status eq '반려' ? 'reject' : (assignment.status eq '진행중' ? 'progress' : 'pending'))}"><c:choose><c:when test="${assignment.status eq '제출완료' or assignment.status eq '늦은제출'}">제출완료</c:when><c:otherwise>${assignment.status}</c:otherwise></c:choose></span>
                                </a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state-box small">표시할 과제가 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </div>

        <div class="student-home-side">
            <section class="card news-card">
                <div class="section-title-row between">
                    <h2 class="section-title">
                        <span class="section-icon bell-icon green icon-mask"></span>
                        가정통신문
                    </h2>
                    <a href="${pageContext.request.contextPath}${bulletinListUrl}" class="text-link">더보기</a>
                </div>

                <div class="news-list">
                    <c:choose>
                        <c:when test="${not empty bulletinList}">
                            <c:forEach var="notice" items="${bulletinList}">
                                <a href="${pageContext.request.contextPath}${bulletinListUrl}" class="news-item">
                                    <span class="news-dot ${notice.requiredUnread ? 'yellow' : 'green'}"></span>
                                    <div class="news-content">
                                        <strong class="news-title">${notice.title}</strong>
                                        <p class="news-desc">${notice.content}</p>
                                        <span class="news-time"><fmt:formatDate value="${notice.createdAt}" pattern="yyyy.MM.dd"/></span>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state-box small">표시할 가정통신문이 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <section class="card current-class-card">
                <div class="current-class-icon">
                    <span class="section-icon book-icon green large icon-mask"></span>
                </div>
                <h3>${currentClass.className}</h3>
                <p><c:if test="${not empty currentClass.teacherName}"> ${currentClass.teacherName} 선생님의 클래스입니다.</c:if></p>
                <a href="${pageContext.request.contextPath}${currentClassUrl}" class="primary-action-btn full">
                    클래스 입장하기
                </a>
            </section>
        </div>
    </div>
</section>
<script src="${pageContext.request.contextPath}/resources/js/student/student-home.js"></script>
