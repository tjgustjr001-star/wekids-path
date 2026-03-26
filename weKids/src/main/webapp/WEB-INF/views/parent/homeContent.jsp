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
                    <span class="card-icon">⌘</span>
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
                    <span class="card-icon blue-icon">◌</span>
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