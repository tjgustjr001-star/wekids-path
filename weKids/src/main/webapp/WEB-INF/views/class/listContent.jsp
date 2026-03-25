<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/class/class-list.css">

<section class="class-list-page">
    <div class="page-top-row">
        <div class="page-title-box">
            <h1>클래스 목록</h1>

            <c:choose>
                <c:when test="${roleType eq 'parent'}">
                    <p>자녀가 참여 중인 클래스를 확인할 수 있습니다.</p>
                </c:when>
                <c:when test="${roleType eq 'teacher'}">
                    <p>담당 중인 클래스를 확인하고 새로운 클래스를 생성할 수 있습니다.</p>
                </c:when>
                <c:otherwise>
                    <p>참여 중인 클래스를 확인하고 새로운 클래스에 가입하세요.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="page-action-row">
            <div class="search-box">
                <span class="search-icon"></span>
                <input type="text" id="classSearchInput" placeholder="클래스 검색..." />
            </div>

            <c:if test="${showJoinButton}">
                <a href="${pageContext.request.contextPath}/student/classes/join" class="join-class-btn">
                    + 클래스 가입
                </a>
            </c:if>

            <c:if test="${showCreateButton}">
                <a href="${pageContext.request.contextPath}/teacher/classes/new" class="join-class-btn">
                    + 클래스 생성
                </a>
            </c:if>
        </div>
    </div>

    <div class="class-card-grid" id="classCardGrid">
        <c:forEach var="cls" items="${classList}">
            <c:choose>
                <c:when test="${roleType eq 'parent'}">
                    <c:url var="classDetailUrl" value="/parent/classes/${cls.classId}" />
                </c:when>
                <c:when test="${roleType eq 'teacher'}">
                    <c:url var="classDetailUrl" value="/teacher/classes/${cls.classId}" />
                </c:when>
                <c:otherwise>
                    <c:url var="classDetailUrl" value="/student/classes/${cls.classId}" />
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}${classDetailUrl}"
               class="class-card"
               data-class-name="${cls.className}">
                <div class="class-card-cover ${cls.coverType}">
                    <span class="class-year-badge">${cls.yearLabel}</span>
                </div>

                <div class="class-card-body">
                    <div class="class-book-badge">
                        <span class="class-card-icon"></span>
                    </div>

                    <div class="class-card-content">
                        <h3 class="class-name">${cls.className}</h3>

                        <c:choose>
                            <c:when test="${roleType eq 'parent'}">
                                <p class="class-teacher">담임교사: ${cls.teacherName}</p>
                                <p class="class-child-info">자녀: ${cls.childName}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="class-teacher">담임교사: ${cls.teacherName}</p>
                            </c:otherwise>
                        </c:choose>

                        <div class="class-card-footer">
                            <span class="class-member-count">멤버 ${cls.memberCount}명</span>
                            <span class="class-enter-link">입장하기 →</span>
                        </div>
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
</section>
<script src="${pageContext.request.contextPath}/resources/js/class/class-list.js"></script>