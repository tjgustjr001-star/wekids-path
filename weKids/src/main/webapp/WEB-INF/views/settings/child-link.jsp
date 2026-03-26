<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="child-manage-page">
    <div class="page-header">
        <h2>자녀 목록</h2>
        <p>등록된 자녀의 상태를 확인해 주세요.</p>
    </div>

    <div class="child-list">
        <c:forEach var="child" items="${childList}">
            <a class="child-card" href="${pageContext.request.contextPath}/parent/settings/child-link/${child.childId}">
                <div class="child-card-left">
                    <div class="child-avatar">
                        ${fn:substring(child.childName, 0, 1)}
                    </div>
                    <div class="child-info">
                        <div class="child-name">${child.childName} 학생</div>
                        <div class="child-meta">
                            ${child.schoolName} ${child.grade}학년 ${child.className}반 ${child.studentNo}번
                        </div>
                    </div>
                </div>
                <div class="child-card-right">
                    &gt;
                </div>
            </a>
        </c:forEach>
    </div>
</div>