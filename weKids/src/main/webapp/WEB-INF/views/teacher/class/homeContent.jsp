<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/class/class-home-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-class-home.css">

<section class="class-home-page teacher-class-home">
    <div class="class-hero-card">
        <div class="class-hero-overlay"></div>
        <div class="class-hero-content">
            <h1>${className}</h1>
            <p>안녕하세요, ${teacherName}님! 우리들의 즐거운 온라인 배움터입니다.</p>
        </div>
    </div>

    <section class="class-home-panel teacher-summary-grid">
        <div class="summary-card teacher-summary-card teacher-summary-card--neutral">
            <div class="teacher-summary-card__top">
                <span class="teacher-summary-card__icon teacher-summary-card__icon--slate teacher-summary-card__icon--users"></span>
                <div class="teacher-summary-card__label">클래스 인원</div>
            </div>
            <div class="teacher-summary-card__value">${studentCount}명</div>
        </div>

        <div class="summary-card teacher-summary-card teacher-summary-card--blue">
            <div class="teacher-summary-card__top">
                <span class="teacher-summary-card__icon teacher-summary-card__icon--blue teacher-summary-card__icon--chart"></span>
                <div class="teacher-summary-card__label">금주 평균 진도율</div>
            </div>
            <div class="teacher-summary-card__value teacher-summary-card__value--green">${avgProgressPercent}%</div>
        </div>

        <div class="summary-card teacher-summary-card teacher-summary-card--red">
            <div class="teacher-summary-card__top">
                <span class="teacher-summary-card__icon teacher-summary-card__icon--red teacher-summary-card__icon--clipboard"></span>
                <div class="teacher-summary-card__label">미제출 과제 학생</div>
            </div>
            <div class="teacher-summary-card__value teacher-summary-card__value--red">${missingAssignmentStudentCount}명</div>
        </div>

        <div class="summary-card teacher-summary-card teacher-summary-card--yellow">
            <div class="teacher-summary-card__top">
                <span class="teacher-summary-card__icon teacher-summary-card__icon--yellow teacher-summary-card__icon--check"></span>
                <div class="teacher-summary-card__label">미평가 과제</div>
            </div>
            <div class="teacher-summary-card__value teacher-summary-card__value--dark">${ungradedAssignmentCount}건</div>
        </div>
    </section>

    <section class="teacher-bottom-grid">
        <section class="class-home-section">
            <div class="section-header">
                <h2>최근 업데이트</h2>
            </div>

            <div class="update-list">
                <c:forEach var="update" items="${recentUpdates}">
                    <div class="update-item">
                        <div class="update-avatar">${update.writerInitial}</div>
                        <div class="update-body">
                            <div class="update-meta">
                                <strong>${update.writerName}</strong>
                                <span>${update.timeText}</span>
                            </div>
                            <p>${update.content}</p>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty recentUpdates}">
                    <div class="empty-box">최근 업데이트가 없습니다.</div>
                </c:if>
            </div>
        </section>

        <section class="class-home-section">
            <div class="section-header section-header-inline">
                <h2>클래스 가정통신문</h2>
                <a href="${pageContext.request.contextPath}/teacher/classes/${classId}/bulletins/create" class="detail-link">새 글 쓰기</a>
            </div>

            <div class="notice-list">
                <c:forEach var="notice" items="${classBulletins}">
                    <a class="notice-item" href="${pageContext.request.contextPath}/teacher/classes/${classId}/bulletins/${notice.id}">
                        <div class="notice-left">
                            <c:if test="${notice.important}">
                                <span class="notice-badge">필독</span>
                            </c:if>
                            <span class="notice-title">${notice.title}</span>
                        </div>
                        <div class="notice-date">${notice.date}</div>
                    </a>
                </c:forEach>

                <c:if test="${empty classBulletins}">
                    <div class="empty-box">등록된 가정통신문이 없습니다.</div>
                </c:if>
            </div>
        </section>
    </section>
</section>