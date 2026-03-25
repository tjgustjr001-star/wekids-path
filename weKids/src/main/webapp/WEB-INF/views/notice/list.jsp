<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/notice/notice.css">

<section class="notice_page">
    <div class="notice_header">
        <div class="notice_header_left">
            <div class="notice_icon_box">
                <img src="${pageContext.request.contextPath}/resources/images/common/icon-notice.png" alt="가정통신문">
            </div>
            <div>
                <h1 class="notice_title">가정통신문</h1>
                <p class="notice_sub_title">
                    <c:choose>
                        <c:when test="${not empty classInfo.className}">
                            ${classInfo.className}
                        </c:when>
                        <c:when test="${not empty className}">
                            ${className}
                        </c:when>
                        <c:otherwise>
                            현재 클래스
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>

        <c:if test="${isTeacher}">
            <button type="button"
                    class="write_btn"
                    id="openWriteBtn"
                    data-class-id="<c:out value='${classInfo.classId}' default='${classId}'/>"
                    data-return-url="${currentUri}">
                새 가정통신문 작성
            </button>
        </c:if>
    </div>

    <div class="notice_top_box">
        <div class="notice_top_info">
            <h2>전체 가정통신문</h2>
            <p>총 <strong id="totalNoticeCount">${fn:length(noticeList)}</strong>건</p>
        </div>

        <div class="filter_row">
            <button type="button" class="filter_btn active" data-filter="all">
                전체 <span>0</span>
            </button>
            <button type="button" class="filter_btn" data-filter="STUDENT">
                학생 <span>0</span>
            </button>
            <button type="button" class="filter_btn" data-filter="PARENT">
                학부모 <span>0</span>
            </button>
        </div>
    </div>

    <div class="notice_list_wrap">
        <c:choose>
            <c:when test="${empty noticeList}">
                <div class="empty_box">
                    <p class="empty_title">등록된 가정통신문이 없습니다.</p>
                    <p class="empty_desc">
                        <c:if test="${isTeacher}">
                            새로운 가정통신문을 작성해주세요.
                        </c:if>
                        <c:if test="${isStudentOrParent}">
                            새로운 가정통신문이 등록되면 여기에서 확인할 수 있습니다.
                        </c:if>
                    </p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="notice_card_list">
                    <c:forEach var="notice" items="${noticeList}">
                        <div class="notice_card"
						     data-notice-id="${notice.noticeId}"
						     data-class-id="${classId}"
						     data-category="${notice.target}">

                            <div class="notice_card_top">
                                <div class="badge_group">
                                    <c:if test="${notice.requiredUnread or notice.confirmYn eq 1}">
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

                                <span class="notice_date">
                                    <fmt:formatDate value="${notice.createdAt}" pattern="yyyy.MM.dd"/>
                                </span>
                            </div>

                            <div class="notice_card_body">
                                <h3 class="notice_card_title">${notice.title}</h3>
                                <p class="notice_card_content">${notice.content}</p>
                            </div>

                            <div class="notice_card_bottom">
                                <div class="notice_meta">
                                    <span>${notice.writerName} 선생님</span>

                                    <c:if test="${not empty notice.attachList}">
                                        <span class="dot">•</span>
                                        <span>첨부 ${fn:length(notice.attachList)}개</span>
                                    </c:if>
                                </div>

                                <c:if test="${isStudentOrParent and (notice.requiredUnread or notice.confirmYn eq 1)}">
                                    <c:choose>
                                        <c:when test="${notice.readConfirmed}">
                                            <span class="read_state done">읽음 확인 완료</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="read_state need">읽음 확인 필요</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="pagination">
        <button type="button" id="prevBtn" class="page_btn">&lt;</button>
        <button type="button" id="nextBtn" class="page_btn">&gt;</button>
    </div>
</section>

<div id="requiredPopupOverlay" class="popup_overlay">
    <div class="popup_box">
        <div class="popup_header">
            <h3>필수 확인 가정통신문</h3>
            <button type="button" id="closeRequiredPopupBtn">×</button>
        </div>
        <div class="popup_body">
            <c:forEach var="notice" items="${noticeList}">
                <c:if test="${isStudentOrParent and (notice.requiredUnread or notice.confirmYn eq 1) and not notice.readConfirmed}">
                    <div class="required_item"
					     data-notice-id="${notice.noticeId}"
					     data-class-id="${classId}">
                        <strong>${notice.title}</strong>
                        <span>
                            <fmt:formatDate value="${notice.createdAt}" pattern="yyyy.MM.dd"/>
                        </span>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>

<div id="modalOverlay" class="modal_overlay">
    <div id="modalContent" class="modal_content"></div>
</div>

<script>
    window.noticeContextPath = '${pageContext.request.contextPath}';
    window.noticeReturnUrl = '${currentUri}';
    window.noticeClassId = '${classId}';
</script>
<script src="${pageContext.request.contextPath}/resources/js/notice/notice.js"></script>