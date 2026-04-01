
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/settings/childLinkStudent.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<div class="link-page">
    <div class="top-back">
        <!-- 🔥 수정 -->
        <a href="${pageContext.request.contextPath}${baseSettingsPath}">
            <i class="fa-solid fa-arrow-left"></i> 설정으로 돌아가기
        </a>
    </div>

    <div class="link-card">
        <div class="page-header">
            <h2>보호자 초대 코드</h2>
            <p>
                보호자 연결 코드를 발급하여 부모님께 전달해주세요.
                부모님이 이 코드를 보호자 등록 후 사용하면 자동으로 연동됩니다.
            </p>
        </div>

        <div class="section-title">연결된 보호자</div>

        <c:choose>
            <c:when test="${not empty parentList}">
                <c:forEach var="parent" items="${parentList}">
                    <div class="linked-parent-box">
                        <div class="status-icon">
                            <i class="fa-solid fa-check"></i>
                        </div>
                        <div class="status-text">
                            <div class="status-name">${parent.parentName}</div>
                            <div class="status-sub">${parent.email}</div>
                            <div class="status-date">
                                연결일:
                                <fmt:formatDate value="${parent.linkedAt}" pattern="yyyy년 M월 d일" />
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-status-box">
                    아직 연결된 보호자가 없습니다.
                </div>
            </c:otherwise>
        </c:choose>

        <div class="invite-panel">
            <div class="invite-head">
                <div class="invite-icon">
                    <i class="fa-solid fa-link"></i>
                </div>
                <div>
                    <div class="invite-title">추가 보호자 초대</div>
                    <div class="invite-desc">
                        코드를 생성하여 부모님께 전달하거나, 직접 복사해 공유해주세요.
                        다른 보호자를 추가하고 연결할 수 있어요.
                    </div>
                </div>
            </div>

            <div class="step-box">
                <div class="step-label">
                    <span class="step-number">1</span>
                    연결 코드 생성
                </div>

                <div class="code-area">
                    <div id="inviteCode" class="code-value">
                        <c:choose>
                            <c:when test="${not empty linkInfo and not empty linkInfo.parentLinkCode}">
                                ${linkInfo.parentLinkCode}
                            </c:when>
                            <c:otherwise>
                                아직 생성된 코드가 없습니다
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="code-btn-group">
                        <button type="button" class="outline-btn" onclick="generateCode()">
                            코드 생성하기
                        </button>
                        <button type="button" class="copy-btn" onclick="copyCode()">
                            코드 복사
                        </button>
                    </div>

                    <div class="code-help">
                        이 코드는 부모님께 전달해주세요. 코드는 24시간 동안 유효하도록 확장도 가능합니다.
                    </div>
                </div>

                <div class="email-send-row disabled-row">
                    <label>
                        <input type="checkbox" disabled>
                        이메일로 초대장 보내기 (선택)
                    </label>

                    <div class="email-form">
                        <input type="text" placeholder="parent@example.com" disabled>
                        <button type="button" disabled>
                            <i class="fa-solid fa-paper-plane"></i> 초대 보내기
                        </button>
                    </div>

                    <p class="disabled-guide">
                        이메일 초대 기능은 별도 전송 기능 추가가 필요합니다.
                    </p>
                </div>
            </div>
        </div>

        <div class="guide-title">보호자에게 공유되는 정보</div>
        <div class="guide-list-box">
            <ul>
                <li><i class="fa-solid fa-check"></i> 학습 진행 현황 및 제출 현황</li>
                <li><i class="fa-solid fa-check"></i> 과제 제출 여부</li>
                <li><i class="fa-solid fa-check"></i> 기본정보 및 공지사항</li>
                <li><i class="fa-solid fa-check"></i> 교사 피드백 및 코멘트</li>
                <li><i class="fa-solid fa-check"></i> 식습관 리포트</li>
            </ul>
        </div>

        <div class="privacy-box">
            <div class="privacy-title">개인정보 보호 안내</div>
            <ul>
                <li>보호자는 학생의 학습 데이터를 열람할 수 있습니다.</li>
                <li>연결 코드는 24시간 후 자동 만료되도록 확장할 수 있습니다.</li>
                <li>보호자의 부정확한 등록은 제한될 수 있습니다.</li>
            </ul>
        </div>
    </div>
</div>

<script>
function generateCode() {
    // 🔥 핵심 수정
    fetch('${pageContext.request.contextPath}${baseSettingsPath}/link/generate', {
        method: 'POST',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(function(response) {
        return response.text();
    })
    .then(function(result) {
        if (result === 'only_student') {
            alert('학생만 사용할 수 있습니다.');
            return;
        }

        document.getElementById('inviteCode').innerText = result;
        alert('연결 코드가 생성되었습니다.');
    })
    .catch(function(error) {
        console.error(error);
        alert('코드 생성 중 오류가 발생했습니다.');
    });
}

function copyCode() {
    var codeText = document.getElementById('inviteCode').innerText.trim();

    if (!codeText || codeText === '아직 생성된 코드가 없습니다') {
        alert('복사할 코드가 없습니다.');
        return;
    }

    navigator.clipboard.writeText(codeText)
        .then(function() {
            alert('코드가 복사되었습니다.');
        })
        .catch(function() {
            alert('복사에 실패했습니다.');
        });
}
</script>
