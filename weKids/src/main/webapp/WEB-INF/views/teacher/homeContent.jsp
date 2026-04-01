<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-home.css">

<section class="teacher-dashboard">
    <div class="teacher-dashboard-grid">
        <div class="teacher-dashboard-main">
            <section class="teacher-hero-card">
                <div class="teacher-hero-badge">${empty currentSemesterLabel ? '2026 1학기' : currentSemesterLabel}</div>
                <h1>안녕하세요, ${empty userName ? '김교사' : userName}님!</h1>
                <p>선생님, 오늘도 화이팅하세요!</p>
            </section>

            <section class="teacher-panel-card">
                <div class="teacher-panel-header">
                    <h2>
                        <span class="section-icon clipboard-icon green"></span>
                        오늘 처리할 업무
                    </h2>

                    <form method="get" action="${pageContext.request.contextPath}/teacher" class="teacher-home-filter-form">
                        <select class="class-select-box" name="classId" onchange="this.form.submit()">
                            <c:forEach var="cls" items="${teacherClassOptions}">
                                <option value="${cls.classId}" ${cls.classId eq selectedClassId ? 'selected' : ''}>${cls.className}</option>
                            </c:forEach>
                        </select>
                    </form>
                </div>

                <div class="task-summary-grid">
                    <a href="${pageContext.request.contextPath}${teacherAssignmentUrl}" class="task-summary-card danger">
                        <span class="task-icon alert-circle-icon"></span>
                        <strong>${empty taskSummary.unsubmittedCount ? 0 : taskSummary.unsubmittedCount}</strong>
                        <span>미제출 과제</span>
                    </a>
                    <a href="${pageContext.request.contextPath}${teacherAssignmentUrl}" class="task-summary-card warning">
                        <span class="task-icon clock-circle-icon"></span>
                        <strong>${empty taskSummary.approachingCount ? 0 : taskSummary.approachingCount}</strong>
                        <span>마감 임박 과제</span>
                    </a>
                    <a href="${pageContext.request.contextPath}${teacherAssignmentUrl}" class="task-summary-card primary">
                        <span class="task-icon check-circle-icon"></span>
                        <strong>${empty taskSummary.feedbackNeededCount ? 0 : taskSummary.feedbackNeededCount}</strong>
                        <span>피드백 필요</span>
                    </a>
                </div>
            </section>

            <section class="teacher-panel-card">
                <div class="teacher-panel-header">
                    <h2>
                        <span class="section-icon clock-section-icon blue"></span>
                        오늘 마감 과제 우선순위
                    </h2>

                    <a href="${pageContext.request.contextPath}${teacherAssignmentUrl}" class="panel-link-btn">
                        과제 관리
                        <span class="arrow-right-mini"></span>
                    </a>
                </div>

                <div class="priority-assignment-list">
                    <c:choose>
                        <c:when test="${not empty deadlineAssignmentList}">
                            <c:forEach var="assignment" items="${deadlineAssignmentList}">
                                <a href="${pageContext.request.contextPath}${teacherAssignmentUrl}" class="priority-assignment-card">
                                    <div class="priority-card-top">
                                        <div class="priority-title-wrap">
                                            <span class="subject-badge">${assignment.subject}</span>
                                            <h3>${assignment.title}</h3>
                                        </div>
                                        <span class="deadline-badge ${assignment.unsubmittedCount gt 0 ? 'danger' : 'normal'}">${assignment.deadlineLabel} 마감</span>
                                    </div>
                                    <div class="priority-progress-wrap">
                                        <div class="priority-progress-text">
                                            <span class="left"><span class="mini-users-icon"></span>미제출 학생 ${assignment.unsubmittedCount}명</span>
                                            <span class="right">${assignment.submittedCount} / ${assignment.totalCount} 명 제출</span>
                                        </div>
                                        <div class="progress-bar"><div class="progress-fill" style="width:${assignment.progressPercent}%;"></div></div>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-box">오늘 마감 예정인 과제가 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </div>

        <div class="teacher-dashboard-side">
            <section class="teacher-panel-card">
                <div class="teacher-panel-header bulletin-header">
                    <h2>
                        <span class="section-icon bell-section-icon green"></span>
                        가정통신문
                    </h2>
                    <a href="${pageContext.request.contextPath}${teacherBulletinUrl}" class="panel-link-btn">
                        가정통신문 작성
                        <span class="arrow-right-mini"></span>
                    </a>
                </div>

                <div class="bulletin-status-list">
                    <c:choose>
                        <c:when test="${not empty bulletinList}">
                            <c:forEach var="bulletin" items="${bulletinList}">
                                <a href="${pageContext.request.contextPath}${teacherBulletinUrl}" class="bulletin-status-card simple-bulletin-card">
                                    <div class="bulletin-title-row">
                                        <div class="bulletin-title-wrap">
                                            <c:if test="${bulletin.confirmYn eq 1}"><span class="required-badge">필독</span></c:if>
                                            <h3>${bulletin.title}</h3>
                                        </div>
                                    </div>
                                    <div class="bulletin-meta-row simple">
                                        <div class="bulletin-meta-left">
                                            <span class="mini-file-icon"></span>
                                            <span>${empty bulletin.target ? '전체 대상' : bulletin.target}</span>
                                            <span><fmt:formatDate value="${bulletin.createdAt}" pattern="yyyy.MM.dd"/></span>
                                        </div>
                                    </div>
                                    <p class="bulletin-simple-content">${bulletin.content}</p>
                                </a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-box">최근 가정통신문이 없습니다.</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <section class="teacher-quick-card">
                <div class="quick-icon-wrap">
                    <span class="section-icon large book-section-icon green"></span>
                </div>
                <strong class="quick-class-name">${empty selectedClassName ? '운영 중인 클래스가 없습니다.' : selectedClassName}</strong>
                <p>현재 운영중인 클래스로 이동하여 학생들과 학습을 시작합니다.</p>
                <a href="${pageContext.request.contextPath}${teacherClassEnterUrl}" class="quick-enter-btn">클래스 입장</a>
            </section>
        </div>
    </div>
</section>
