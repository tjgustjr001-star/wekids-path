<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                    <h2>자녀 과제 현황</h2>
                </div>

                <div class="card-header-actions">
                    <a href="${pageContext.request.contextPath}${assignmentListUrl}" class="detail-link">전체보기</a>
                    <form method="get" action="${pageContext.request.contextPath}/parent" class="child-select-form">
                        <select name="selection" onchange="this.form.submit()">
                            <c:forEach var="child" items="${childOptions}">
                                <option value="${child.value}" <c:if test="${child.value eq selectedSelection}">selected</c:if>>${child.label}</option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
            </div>

            <div class="learning-stat-row">
                <div class="learning-stat-box">
                    <span class="label">전체 과제</span>
                    <strong class="value">${assignmentTotal}</strong>
                </div>
                <div class="learning-stat-box blue">
                    <span class="label">제출 완료</span>
                    <strong class="value">${assignmentSubmitted}</strong>
                </div>
                <div class="learning-stat-box green">
                    <span class="label">미제출</span>
                    <strong class="value">${assignmentPending}</strong>
                </div>
            </div>

            <div class="progress-section">
                <div class="progress-label-row">
                    <span>과제 제출률</span>
                    <strong>${assignmentRate}%</strong>
                </div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width:${assignmentRate}%;"></div>
                </div>
            </div>

            <div class="recent-learning-section first-section">
                <div class="card-header recent-learning-header">
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
                        <h3>최근 과제</h3>
                    </div>
                    <a href="${pageContext.request.contextPath}${assignmentListUrl}" class="more-link">바로가기</a>
                </div>
                <c:choose>
                    <c:when test="${not empty recentAssignmentList}">
                        <div class="recent-learning-list">
                            <c:forEach var="assignment" items="${recentAssignmentList}">
                                <a href="${pageContext.request.contextPath}${assignmentListUrl}" class="recent-learning-item">
                                    <div class="recent-learning-main">
                                        <div class="recent-learning-title-row">
                                            <span class="subject-badge
                                                <c:choose>
                                                    <c:when test="${assignment.subject eq '수학'}"> subject-math</c:when>
                                                    <c:when test="${assignment.subject eq '과학'}"> subject-science</c:when>
                                                    <c:when test="${assignment.subject eq '국어'}"> subject-korean</c:when>
                                                    <c:otherwise> subject-default</c:otherwise>
                                                </c:choose>
                                            ">${empty assignment.subject ? '과제' : assignment.subject}</span>
                                            <strong>${assignment.title}</strong>
                                        </div>
                                        <span class="recent-learning-meta">${not empty assignment.deadline ? assignment.deadline.concat(" 마감") : "과제 확인"}</span>
                                    </div>
                                    <span class="recent-learning-status ${assignment.status eq '제출완료' or assignment.status eq '확인완료' or assignment.status eq '늦은제출' ? 'done' : (assignment.status eq '반려' ? 'reject' : 'pending')}"><c:choose><c:when test="${assignment.status eq '제출완료' or assignment.status eq '늦은제출'}">제출완료</c:when><c:otherwise>${empty assignment.status ? "미제출" : assignment.status}</c:otherwise></c:choose></span>
                                </a>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-box">최근 과제가 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <div class="parent-home-side">
        <aside class="parent-home-card parent-notice-card">
            <div class="card-header notice-header">
                <div class="card-title-wrap">
                    <span class="card-icon card-icon-notice blue-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M15 17h5l-1.4-1.4A2 2 0 0 1 18 14.2V11a6 6 0 1 0-12 0v3.2a2 2 0 0 1-.6 1.4L4 17h5" />
                            <path d="M9 17v1a3 3 0 0 0 6 0v-1" />
                        </svg>
                    </span>
                    <h2>가정통신문</h2>
                </div>
                <a href="${pageContext.request.contextPath}${bulletinListUrl}" class="more-link">더보기</a>
            </div>

            <div class="notice-list">
                <c:choose>
                    <c:when test="${not empty parentNotices}">
                        <c:forEach var="notice" items="${parentNotices}">
                            <a href="${pageContext.request.contextPath}${bulletinListUrl}" class="notice-item">
                                <span class="notice-dot ${notice.requiredUnread ? 'yellow' : 'blue'}"></span>
                                <div class="notice-content">
                                    <strong>${notice.title}</strong>
                                    <p>${notice.content}</p>
                                    <span><fmt:formatDate value="${notice.createdAt}" pattern="yyyy.MM.dd"/></span>
                                </div>
                            </a>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-box">표시할 가정통신문이 없습니다.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </aside>

        <section class="parent-home-card parent-class-shortcut-card">
            <div class="parent-class-shortcut-icon" aria-hidden="true">
                <span class="card-icon card-icon-learning">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 7v14" />
                        <path d="M3 18.5A2.5 2.5 0 0 1 5.5 16H12v5.5A2.5 2.5 0 0 0 9.5 19H5a2 2 0 0 1-2-2.5Z" />
                        <path d="M21 18.5A2.5 2.5 0 0 0 18.5 16H12v5.5a2.5 2.5 0 0 1 2.5-2.5H19a2 2 0 0 0 2-2.5Z" />
                        <path d="M5.5 16A2.5 2.5 0 0 1 3 13.5V5a2 2 0 0 1 2-2h4.5A2.5 2.5 0 0 1 12 5.5V16" />
                        <path d="M18.5 16a2.5 2.5 0 0 0 2.5-2.5V5a2 2 0 0 0-2-2h-4.5A2.5 2.5 0 0 0 12 5.5" />
                    </svg>
                </span>
            </div>
            <strong>${selectedClassName}</strong>
            <p>선택한 자녀의 클래스로 바로 이동합니다.</p>
            <a href="${pageContext.request.contextPath}${currentClassUrl}" class="class-shortcut-btn">클래스 바로가기</a>
        </section>
        </div>
    </div>
</section>
