<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="child-detail-page">
    <div class="top-row">
        <a class="back-link" href="${pageContext.request.contextPath}/parent/settings/child-link">
            ← 자녀 목록으로
        </a>
    </div>
    <div class="detail-layout">
        <div class="profile-card">
            <div class="avatar">
                <c:choose>
                    <c:when test="${not empty child.studentName}">
                        ${child.studentName.substring(0,1)}
                    </c:when>
                    <c:otherwise>자</c:otherwise>
                </c:choose>
            </div>

            <h3>
                <c:out value="${child.studentName}" /> 학생
            </h3>

            <p>
                <c:choose>
                    <c:when test="${child.year > 0}">
                        <c:out value="${child.year}" />학년도
                        <c:out value="${child.grade}" />학년
                        <c:out value="${child.classNo}" />반
                    </c:when>
                    <c:otherwise>
                        학급 정보 없음
                    </c:otherwise>
                </c:choose>
            </p>

            <p>학습 진행률: <c:out value="${child.learningProgressRate}" />%</p>
            <p>과제 제출률: <c:out value="${child.assignmentRate}" />%</p>
        </div>

        <div class="info-card">
            <h3>자녀 상세 정보</h3>

            <div class="info-row">
                <strong>이름</strong>
                <c:out value="${child.studentName}" />
            </div>

            <div class="info-row">
                <strong>학년</strong>
                <c:out value="${child.grade}" />
            </div>

            <div class="info-row">
                <strong>반</strong>
                <c:out value="${child.classNo}" />
            </div>

            <div class="info-row">
                <strong>최근 연동일</strong>
                <c:choose>
                    <c:when test="${not empty child.linkedAt}">
                        <fmt:formatDate value="${child.linkedAt}" pattern="yyyy-MM-dd HH:mm" />
                    </c:when>
                    <c:otherwise>정보 없음</c:otherwise>
                </c:choose>
            </div>

            <div class="comment-box">
                <strong>선생님 한마디</strong>
                <p>
                    <c:choose>
                        <c:when test="${not empty child.teacherComment}">
                            <c:out value="${child.teacherComment}" />
                        </c:when>
                        <c:otherwise>등록된 코멘트가 없습니다.</c:otherwise>
                    </c:choose>
                </p>
            </div>

        </div>
    </div>
</div>