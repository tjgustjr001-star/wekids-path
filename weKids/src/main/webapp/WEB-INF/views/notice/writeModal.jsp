<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
    <c:when test="${mode eq 'edit'}">
        <c:set var="formAction" value="/notice/modify" />
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="/notice/regist" />
    </c:otherwise>
</c:choose>

<div class="modal_box write_modal_box">
    <div class="modal_header">
        <h3>
            <c:if test="${mode eq 'edit'}">가정통신문 수정</c:if>
            <c:if test="${mode ne 'edit'}">새 가정통신문 작성</c:if>
        </h3>
        <button type="button" class="modal_close_btn" data-close-modal="true">×</button>
    </div>

    <form action="${pageContext.request.contextPath}${formAction}"
          method="post"
          enctype="multipart/form-data">

        <c:if test="${mode eq 'edit'}">
            <input type="hidden" name="noticeId" value="${notice.noticeId}">
        </c:if>

        <input type="hidden" name="classId" value="${classId}">
        <input type="hidden" name="returnUrl" value="${returnUrl}">

        <div class="modal_body">
            <label class="field_label">제목</label>
            <input type="text" name="title" class="text_input" value="${notice.title}" required>

            <label class="check_row">
                <input type="checkbox" name="confirmYn" value="1"
                       <c:if test="${notice.confirmYn eq 1}">checked</c:if>>
                필수 공지로 설정
            </label>
            <input type="hidden" name="confirmYn" value="0">

            <label class="field_label">내용</label>
            <textarea name="content" class="textarea_input" required>${notice.content}</textarea>

            <label class="field_label">첨부파일</label>
            <input type="file" name="uploadFile" class="text_input" multiple>

            <label class="field_label">대상</label>
            <select name="target" class="text_input">
                <option value="ALL" <c:if test="${notice.target eq 'ALL'}">selected</c:if>>전체</option>
                <option value="STUDENT" <c:if test="${notice.target eq 'STUDENT'}">selected</c:if>>학생</option>
                <option value="PARENT" <c:if test="${notice.target eq 'PARENT'}">selected</c:if>>학부모</option>
            </select>
        </div>

        <div class="modal_footer between">
            <button type="submit" class="primary_btn">
                <c:if test="${mode eq 'edit'}">수정하기</c:if>
                <c:if test="${mode ne 'edit'}">등록하기</c:if>
            </button>
            <button type="button" class="gray_btn" data-close-modal="true">취소</button>
        </div>
    </form>
</div>