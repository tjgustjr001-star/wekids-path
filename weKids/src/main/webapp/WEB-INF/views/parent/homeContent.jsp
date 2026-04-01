<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-home.css">

<section class="parent-home-page">
    <div class="parent-home-hero">
        <div class="parent-home-term">${termLabel}</div>
        <h1>안녕하세요, ${parentName}님!</h1>
        <p>자녀의 새로운 소식을 확인해보세요.</p>
    </div>

    <div class="parent-home-grid">
        <section class="parent-home-card parent-learning-card">
            <div class="card-header">
                <div class="card-title-wrap">
                    <span class="card-icon card-icon-learning" aria-hidden="true">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 7v14" />
                            <path d="M3 18.5A2.5 2.5 0 0 1 5.5 16H12v5.5A2.5 2.5 0 0 0 9.5 19H5a2 2 0 0 1-2-2.5Z" />
                            <path d="M21 18.5A2.5 2.5 0 0 0 18.5 16H12v5.5a2.5 2.5 0 0 1 2.5-2.5H19a2 2 0 0 0 2-2.5Z" />
                            <path d="M5.5 16A2.5 2.5 0 0 1 3 13.5V5a2 2 0 0 1 2-2h4.5A2.5 2.5 0 0 1 12 5.5V16" />
                            <path d="M18.5 16a2.5 2.5 0 0 0 2.5-2.5V5a2 2 0 0 0-2-2h-4.5A2.5 2.5 0 0 0 12 5.5" />
                        </svg>
                    </span>
                    <h2>자녀 학습 현황</h2>
                </div>

                <div class="card-header-actions">
                    <a href="${pageContext.request.contextPath}/parent/children/${selectedChildId}" class="detail-link">상세보기</a>

                    <form method="get" action="${pageContext.request.contextPath}/parent" class="child-select-form">
                        <select name="childId" onchange="this.form.submit()">
                            <c:forEach var="child" items="${children}">
                                <option value="${child.id}" <c:if test="${child.id eq selectedChildId}">selected</c:if>>
                                    ${child.name} (${child.className})
                                </option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </div>

            <div class="learning-stat-row">
                <div class="learning-stat-box">
                    <span class="label">전체 학습</span>
                    <strong class="value">${learningTotal}</strong>
                </div>

                <div class="learning-stat-box blue">
                    <span class="label">진행 중</span>
                    <strong class="value">${learningInProgress}</strong>
                </div>

                <div class="learning-stat-box green">
                    <span class="label">완료</span>
                    <strong class="value">${learningCompleted}</strong>
                </div>
            </div>

            <div class="progress-section">
                <div class="progress-label-row">
                    <span>오늘의 진도율</span>
                    <strong>${todayProgressPercent}%</strong>
                </div>

                <div class="progress-bar">
                    <div class="progress-fill" style="width:${todayProgressPercent}%;"></div>
                </div>
            </div>

            <div class="chart-section">
                <h3>주간 학습 시간 (시간)</h3>

                <div class="weekly-chart">
                    <c:forEach var="item" items="${weeklyStudyHours}">
                        <div class="chart-item">
                            <div class="chart-bar-area">
                                <div class="chart-bar" style="height:${item.percent}%;"></div>
                            </div>
                            <span class="chart-label">${item.day}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>

        <aside class="parent-home-card parent-notice-card">
            <div class="card-header notice-header">
                <div class="card-title-wrap">
                    <span class="card-icon card-icon-notice blue-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M15 17h5l-1.4-1.4A2 2 0 0 1 18 14.2V11a6 6 0 1 0-12 0v3.2a2 2 0 0 1-.6 1.4L4 17h5" />
                            <path d="M9 17v1a3 3 0 0 0 6 0v-1" />
                        </svg>
                    </span>
                    <h2>가정통신문 및 공지사항</h2>
                </div>

                <a href="${pageContext.request.contextPath}/parent/classes" class="more-link">더보기</a>
            </div>

            <div class="notice-list">
                <c:forEach var="notice" items="${parentNotices}">
                    <a href="${pageContext.request.contextPath}/parent/classes/${notice.classId}" class="notice-item">
                        <span class="notice-dot ${notice.colorClass}"></span>

                        <div class="notice-content">
                            <strong>${notice.title}</strong>
                            <p>${notice.description}</p>
                            <span>${notice.timeText}</span>
                        </div>
                    </a>
                </c:forEach>

                <c:if test="${empty parentNotices}">
                    <div class="empty-box">표시할 공지사항이 없습니다.</div>
                </c:if>
            </div>
        </aside>
    </div>
</section>