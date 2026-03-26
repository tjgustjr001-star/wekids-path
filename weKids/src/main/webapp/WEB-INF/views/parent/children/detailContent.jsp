<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-children.css">

<section class="parent-child-detail-page">
    <div class="parent-child-detail-topbar">
        <a href="${pageContext.request.contextPath}/parent/children" class="parent-back-link">‹ 자녀 목록으로</a>

        <c:if test="${fn:length(allChildren) > 1}">
            <div class="parent-child-switcher">
                <button type="button" class="parent-child-switcher-btn" id="childSwitcherBtn">
                    <span class="avatar">${currentChild.initial}</span>
                    <span>${currentChild.name}</span>
                    <span>▼</span>
                </button>

                <div class="parent-child-switcher-menu" id="childSwitcherMenu">
                    <c:forEach var="child" items="${allChildren}">
                        <a href="${pageContext.request.contextPath}/parent/children/${child.id}" class="parent-child-switcher-item ${child.id eq currentChild.id ? 'active' : ''}">
                            <span class="avatar">${child.initial}</span>
                            <div>
                                <strong>${child.name}</strong>
                                <p>${child.className}</p>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </div>

    <div class="parent-child-detail-grid">
        <div class="parent-child-detail-left">
            <div class="parent-card centered">
                <div class="parent-big-avatar">${currentChild.initial}</div>
                <h3>${currentChild.name} 학생</h3>
                <p>${currentChild.className} ${currentChild.number}번</p>

                <div class="parent-info-chip-box">
                    <div class="parent-info-chip">
                        <span>최근 접속</span>
                        <strong>${currentChild.lastAccess}</strong>
                    </div>
                </div>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">
                    <span class="parent-section-icon comment"></span>
                    선생님 한마디
                </h3>
                <div class="parent-teacher-comment-box">
                    "${currentChild.teacherComment}"
                </div>
                <div class="parent-teacher-sign">
                    - ${currentChild.teacherName} 선생님 (${currentChild.teacherCommentDate})
                </div>
            </div>
        </div>

        <div class="parent-child-detail-right">
            <div class="parent-summary-grid">
                <div class="parent-summary-card">
                    <strong>${overallProgress}%</strong>
                    <span>전체 달성률</span>
                </div>
                <div class="parent-summary-card">
                    <strong>${currentChild.learningDone}/${currentChild.learningTotal}</strong>
                    <span>학습 완료</span>
                </div>
                <div class="parent-summary-card">
                    <strong>${currentChild.assignmentDone}/${currentChild.assignmentTotal}</strong>
                    <span>과제 제출</span>
                </div>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">
                    <span class="parent-section-icon book"></span>
                    이번 주 과목별 학습
                </h3>

                <div class="parent-subject-progress-list">
                    <c:forEach var="subj" items="${currentChild.weeklySubjects}">
                        <div>
                            <div class="parent-progress-label-row">
                                <span>${subj.subject}</span>
                                <strong>${subj.progress}%</strong>
                            </div>
                            <div class="parent-progress-bar slim">
                                <div class="parent-progress-fill ${subj.colorClass}" style="width:${subj.progress}%;"></div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="parent-overall-weekly-box">
                    <div class="parent-progress-label-row">
                        <span>전체 학습 진행률</span>
                        <strong>${weeklyAverage}%</strong>
                    </div>
                    <div class="parent-progress-bar">
                        <div class="parent-progress-fill blue" style="width:${weeklyAverage}%;"></div>
                    </div>
                </div>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">
                    <span class="parent-section-icon alert"></span>
                    확인이 필요한 항목
                </h3>

                <c:choose>
                    <c:when test="${not empty currentChild.pendingItems}">
                        <div class="parent-pending-list">
                            <c:forEach var="item" items="${currentChild.pendingItems}">
                                <div class="parent-pending-item ${item.urgent ? 'urgent' : 'normal'}">
                                    <div>
                                        <strong>${item.title}</strong>
                                        <p>마감: ${item.deadline}</p>
                                    </div>
                                    <button type="button">바로가기</button>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="parent-empty-ok-box">
                            확인이 필요한 항목이 없습니다.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>
<script src="${pageContext.request.contextPath}/resources/js/parent/parent-children.js"></script>