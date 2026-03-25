<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자녀 관리</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/settings/childLinkParent.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="parent-link-page">

    <div class="page-top-row">
        <div class="page-title-wrap">
            <h2>자녀 목록</h2>
            <p>등록된 자녀의 학습 현황을 한눈에 확인하세요.</p>
        </div>

        <button type="button" class="add-child-btn" id="openChildModalBtn">
            <i class="fa-solid fa-plus"></i> 자녀 추가 연동
        </button>
    </div>

    <div class="child-list-wrap">
        <c:choose>
            <c:when test="${not empty childList}">
                <c:forEach var="child" items="${childList}">
                    <div class="child-card"
                         onclick="location.href='${pageContext.request.contextPath}/parent/settings/child-link/detail?studentId=${child.studentId}'"
                         style="cursor:pointer;">
                        <div class="child-card-top">
                            <div class="child-profile">
                                <div class="child-avatar">
                                    <c:choose>
                                        <c:when test="${not empty child.studentName}">
                                            ${fn:substring(child.studentName, 0, 1)}
                                        </c:when>
                                        <c:otherwise>자</c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="child-info">
                                    <div class="child-name-row">
                                        <span class="child-name">
                                            <c:out value="${child.studentName}" />
                                        </span>
                                        <span class="child-badge">연결 완료</span>
                                    </div>

                                    <div class="child-meta">
                                        학생 ID: <c:out value="${child.studentId}" />
                                    </div>
                                </div>
                            </div>

                            <div class="child-arrow">
                                <i class="fa-solid fa-chevron-right"></i>
                            </div>
                        </div>

                        <div class="child-progress-row">
                            <div class="progress-item">
                                <div class="progress-label-row">
                                    <span>학습 진도</span>
                                    <span>100%</span>
                                </div>
                                <div class="progress-bar green">
                                    <div class="progress-fill" style="width:100%;"></div>
                                </div>
                            </div>

                            <div class="progress-item">
                                <div class="progress-label-row">
                                    <span>과제 제출</span>
                                    <span>75%</span>
                                </div>
                                <div class="progress-bar yellow">
                                    <div class="progress-fill" style="width:75%;"></div>
                                </div>
                            </div>

                            <div class="last-update">
                                최근 접속:
                                <c:choose>
                                    <c:when test="${not empty child.linkedAt}">
                                        <c:out value="${child.linkedAt}" />
                                    </c:when>
                                    <c:otherwise>정보 없음</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <div class="empty-child-box">
                    <div class="empty-icon">
                        <i class="fa-solid fa-children"></i>
                    </div>
                    <div class="empty-title">연결된 자녀가 없습니다</div>
                    <div class="empty-desc">
                        자녀 추가 연동 버튼을 눌러 학생이 발급한 보호자 연결 코드를 입력하세요.
                    </div>
                    <button type="button" class="empty-add-btn" id="openChildModalBtn2">
                        자녀 추가 연동
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- 모달 -->
<div class="modal-overlay" id="childModal">
    <div class="child-modal">
        <div class="modal-header">
            <div>
                <h3><i class="fa-solid fa-link"></i> 자녀 추가 연동</h3>
                <p>자녀의 연결 코드를 입력하세요</p>
            </div>
            <button type="button" class="modal-close-btn" id="closeChildModalBtn">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <div class="modal-body">
            <label class="input-label">보호자 연결 코드</label>

            <input type="text"
                   id="childConnectCode"
                   class="connect-code-input"
                   placeholder="ABCD1234"
                   maxlength="20">

            <p class="input-guide">
                자녀의 학생 계정 설정 &gt; 보호자 코드 화면에서 발급됩니다.
            </p>

            <button type="button" class="confirm-btn" id="connectChildBtn">
                <i class="fa-solid fa-link"></i> 코드 확인
            </button>

            <div class="modal-guide-box">
                <div class="guide-box-title">연결 코드 입력 방법</div>
                <ol>
                    <li>자녀가 학생 계정으로 로그인합니다.</li>
                    <li>설정 &gt; 보호자 초대 코드로 이동합니다.</li>
                    <li>“코드 생성하기” 버튼을 눌러 발급합니다.</li>
                    <li>발급된 8자리 코드를 입력 후 확인합니다.</li>
                </ol>
            </div>
        </div>
    </div>
</div>

<form id="connectChildForm"
      action="${pageContext.request.contextPath}/parent/settings/child-link/connect"
      method="post"
      style="display:none;">
    <input type="hidden" name="code" id="hiddenConnectCode">
</form>

<script src="${pageContext.request.contextPath}/resources/js/settings/childLinkParent.js"></script>

<c:if test="${not empty msg}">
<script>
    const msg = "${msg}";
    if (msg === "success") {
        alert("자녀 연결이 완료되었습니다.");
    } else if (msg === "fail") {
        alert("유효하지 않은 코드이거나 이미 연결된 자녀입니다.");
    } else if (msg === "only_parent") {
        alert("학부모만 사용할 수 있습니다.");
    } else if (msg === "removed") {
        alert("자녀 연동이 해제되었습니다.");
    } else if (msg === "remove_fail") {
        alert("자녀 연동 해제에 실패했습니다.");
    } else if (msg === "not_found_child") {
        alert("해당 자녀 정보를 찾을 수 없습니다.");
    }
</script>
</c:if>

</body>
</html>