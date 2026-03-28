
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/parent/parent-children.css">

<section class="parent-child-detail-page">
    <div class="parent-child-detail-topbar">
        <a href="${pageContext.request.contextPath}/parent/children" class="parent-back-link">‹ 자녀 목록으로</a>
    </div>

    <div class="parent-child-detail-grid">
        <div class="parent-child-detail-left">
            <div class="parent-card centered">
                <div class="parent-big-avatar">
                    <c:choose>
                        <c:when test="${not empty child.studentName}">${fn:substring(child.studentName,0,1)}</c:when>
                        <c:otherwise>자</c:otherwise>
                    </c:choose>
                </div>
                <h3>${child.studentName} 학생</h3>
                <p>
                    <c:choose>
                        <c:when test="${child.year > 0}">${child.year}학년도 ${child.grade}학년 ${child.classNo}반</c:when>
                        <c:otherwise>학생 ID: ${child.studentId}</c:otherwise>
                    </c:choose>
                </p>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">선생님 한마디</h3>
                <div class="parent-teacher-comment-box">${child.teacherComment}</div>
            </div>
        </div>

        <div class="parent-child-detail-right">
            <div class="parent-summary-grid">
                <div class="parent-summary-card">
                    <strong>${overallProgress}%</strong>
                    <span>전체 달성률</span>
                </div>
                <div class="parent-summary-card">
                    <strong>${child.completedLearningCount}/${child.totalLearningCount}</strong>
                    <span>학습 완료</span>
                </div>
                <div class="parent-summary-card">
                    <strong>${child.submittedAssignmentCount}/${child.totalAssignmentCount}</strong>
                    <span>과제 제출</span>
                </div>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">학습 현황</h3>
                <div class="parent-progress-label-row">
                    <span>학습 진도율</span>
                    <strong>${child.learningProgressRate}%</strong>
                </div>
                <div class="parent-progress-bar">
                    <div class="parent-progress-fill blue" style="width:${child.learningProgressRate}%;"></div>
                </div>

                <div class="parent-progress-label-row" style="margin-top:16px;">
                    <span>과제 제출율</span>
                    <strong>${child.assignmentRate}%</strong>
                </div>
                <div class="parent-progress-bar">
                    <div class="parent-progress-fill green" style="width:${child.assignmentRate}%;"></div>
                </div>
            </div>

            <div class="parent-card">
                <h3 class="parent-section-title">확인이 필요한 항목</h3>
                <div class="parent-empty-ok-box">
                    미확인 가정통신문 ${child.unconfirmedNoticeCount}건
                </div>
            </div>
        </div>
    </div>
</section>
