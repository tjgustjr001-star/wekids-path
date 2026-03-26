<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="modal_box detail_modal_box">
    <div class="modal_header">
        <h3>가정통신문 상세</h3>
        <button type="button" class="modal_close_btn" data-close-modal="true">×</button>
    </div>

    <div class="modal_body">
        <div class="detail_meta_row">
            <div class="badge_group">
                <c:if test="${notice.requiredUnread}">
                    <span class="badge badge_required">필독</span>
                </c:if>

                <span class="badge 
                    <c:choose>
                        <c:when test='${notice.target eq "STUDENT"}'>badge_student</c:when>
                        <c:when test='${notice.target eq "PARENT"}'>badge_parent</c:when>
                        <c:otherwise>badge_all</c:otherwise>
                    </c:choose>">
                    <c:choose>
                        <c:when test='${notice.target eq "STUDENT"}'>학생 대상</c:when>
                        <c:when test='${notice.target eq "PARENT"}'>학부모 대상</c:when>
                        <c:otherwise>전체 대상</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <span class="date">
                <fmt:formatDate value="${notice.createdAt}" pattern="yyyy.MM.dd"/>
            </span>
        </div>

        <h2 class="detail_title">${notice.title}</h2>

        <div class="writer_box">
            <div class="writer_avatar">${fn:substring(notice.writerName,0,1)}</div>
            <div>
                <div class="writer_name">${notice.writerName} 선생님</div>
                <div class="writer_desc">${notice.className}</div>
            </div>
        </div>

        <div class="detail_content">${notice.content}</div>

        <c:if test="${not empty notice.attachList}">
            <div class="attach_box">
                <h4>첨부파일</h4>
                <c:forEach var="attach" items="${notice.attachList}">
                    <a href="${pageContext.request.contextPath}/notice/download?ano=${attach.ano}" class="attach_link">
                        ${fn:substringAfter(attach.fileName, '$$')}
                    </a>
                </c:forEach>
            </div>
        </c:if>
    </div>

    <div class="modal_footer between">
        <div>
            <c:if test="${isStudentOrParent and notice.requiredUnread}">
                <form action="${pageContext.request.contextPath}/notice/confirm" method="post" class="inline_form">
                    <input type="hidden" name="noticeId" value="${notice.noticeId}">
                    <input type="hidden" name="classId" value="${notice.classId}">
                    <input type="hidden" name="returnUrl" value="${returnUrl}">
                    <button type="submit" class="primary_btn">필수 읽음 확인</button>
                </form>
            </c:if>

            <c:if test="${isTeacher}">
                <button type="button"
				        class="primary_btn"
				        id="openEditBtn"
				        data-notice-id="${notice.noticeId}"
				        data-class-id="${notice.classId}"
				        data-return-url="${returnUrl}">
				    수정하기
				</button>

                <form action="${pageContext.request.contextPath}/notice/delete"
                      method="post"
                      class="inline_form"
                      onsubmit="return confirm('삭제하시겠습니까?');">
                    <input type="hidden" name="noticeId" value="${notice.noticeId}">
                    <input type="hidden" name="classId" value="${notice.classId}">
                    <input type="hidden" name="returnUrl" value="${returnUrl}">
                    <button type="submit" class="danger_btn">삭제</button>
                </form>
            </c:if>
        </div>

        <button type="button" class="gray_btn" data-close-modal="true">닫기</button>
    </div>
</div>