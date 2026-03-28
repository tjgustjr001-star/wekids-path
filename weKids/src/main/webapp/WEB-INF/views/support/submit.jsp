<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>1:1 문의 작성 – We-Kids</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/resources/css/submit.css" >
</head>
<body>





<!-- ════════ MAIN ════════ -->
<main class="main-wrap">

    <div class="page-header">
        <div class="page-header-top">
            <a href="#" class="back-btn" aria-label="뒤로가기">
                <svg viewBox="0 0 24 24">
                    <polyline points="15 18 9 12 15 6"/>
                </svg>
            </a>
            <h1 class="page-title">1:1 문의 작성</h1>
        </div>
        <p class="page-subtitle">궁금한 점이나 불편한 점을 남겨주시면 빠르게 답변해 드리겠습니다.</p>
    </div>

    <div class="card-write">
        <form id="supportForm" action="${pageContext.request.contextPath}/support/submit"
              method="post" enctype="multipart/form-data" novalidate>

            <!-- 문의 유형 -->
            <div class="form-group">
                <label class="form-label" for="category">
                    문의 유형 <span class="required">*</span>
                </label>
                <select class="form-select" id="support" name="category">
                    <option value="account">계정 / 로그인</option>
                    <option value="class">클래스 관련</option>
                    <option value="payment">결제 / 환불</option>
                    <option value="tech">기술적 문제</option>
                    <option value="other">기타</option>
                </select>
                <span class="field-error" id="categoryError">문의 유형을 선택해 주세요.</span>
            </div>

            <!-- 제목 -->
            <div class="form-group">
                <label class="form-label" for="subject">
                    제목 <span class="required">*</span>
                </label>
                <input
                    type="text"
                    class="form-input"
                    id="title"
                    name="title"
                    placeholder="문의 내용을 간략히 요약해 주세요."
                    maxlength="100"
                >
                <span class="field-error" id="subjectError">제목을 입력해 주세요.</span>
            </div>

            <!-- 상세 내용 -->
            <div class="form-group">
                <label class="form-label" for="body">
                    상세 내용 <span class="required">*</span>
                </label>
                <div class="textarea-wrap">
                    <textarea
                        class="form-textarea"
                        id="content"
                        name="content"
                        placeholder="오류가 발생한 경우, 오류 메시지나 발생 상황을 자세히 적어주시면 더 빠른 확인이 가능합니다."
                        maxlength="1000"
                    ></textarea>
                    <span class="char-count" id="charCount">0/1000자</span>
                </div>
                <span class="field-error" id="bodyError">상세 내용을 입력해 주세요.</span>
            </div>

            <!-- 첨부 파일 -->
            <div class="form-group">
                <label class="form-label">첨부 파일 (선택)</label>
                <div class="file-upload-area" id="dropZone">
                    <svg viewBox="0 0 24 24">
                        <polyline points="16 16 12 12 8 16"/>
                        <line x1="12" y1="12" x2="12" y2="21"/>
                        <path d="M20.39 18.39A5 5 0 0 0 18 9h-1.26A8 8 0 1 0 3 16.3"/>
                    </svg>
                    <p class="file-upload-text">클릭하여 파일을 선택하거나 이 곳으로 드래그 하세요.</p>
                    <p class="file-upload-hint">JPG, PNG, PDF (최대 10MB)</p>
                    <input type="file" class="file-input" id="fileInput"
                           name="attachment" accept=".jpg,.jpeg,.png,.pdf">
                </div>
            </div>

            <!-- 안내 박스 -->
            <div class="notice-box">
                <svg viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="10"/>
                    <line x1="12" y1="8" x2="12" y2="12"/>
                    <line x1="12" y1="16" x2="12.01" y2="16"/>
                </svg>
                <p>
                    문의하신 내용은 순차적으로 확인 후 답변해 드립니다.<br>
                    운영 시간(평일 10:00 - 18:00) 외 접수된 문의는 다음 영업일에 처리됩니다.
                </p>
            </div>

            <!-- 버튼 : 취소 + 문의 등록하기 -->
            <div class="btn-row">
                <button type="button" class="btn-cancel"
                        onclick="history.back()">취소</button>
                <button type="submit" class="btn-submit">
                    <svg viewBox="0 0 24 24">
                        <line x1="22" y1="2" x2="11" y2="13"/>
                        <polygon points="22 2 15 22 11 13 2 9 22 2"/>
                    </svg>
                    문의 등록하기
                </button>
            </div>

        </form>
    </div>

</main>

<style>

/* ================================================
   write.css – We-Kids 1:1 문의 작성 페이지 스타일
   ================================================ */

@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500;600;700&display=swap');

:root {
    --green-primary : #2e7d4f;
    --green-dark    : #1e5c38;
    --green-light   : #eef6f2;
    --bg            : #f2f5f7;
    --white         : #ffffff;
    --border        : #e4e8ec;
    --text-main     : #1a1a1a;
    --text-sub      : #555;
    --text-muted    : #aaa;
    --red           : #e53935;
    --radius-card   : 16px;
    --shadow-card   : 0 2px 16px rgba(0,0,0,.06);
}

* { margin: 0; padding: 0; box-sizing: border-box; }



/* ════════════════════════════════
   MAIN WRAP
════════════════════════════════ */
.main-wrap {
    margin-top: var(--topnav-height);
    padding: 44px 56px;
    flex-direction: column;
    align-items: center;
}

/* ════════════════════════════════
   PAGE HEADER
════════════════════════════════ */
.page-header { 
	width: 960px;     /* card랑 동일하게 */
    margin: 0 auto;   /* 가운데 정렬 */
    margin-bottom:10px;
}
.page-header-top {
    display: flex; align-items: center;
    gap: 10px; margin-bottom: 8px;
    margin-left: 50px;
}
.back-btn {
    background: none; border: none;
    cursor: pointer; color: #666; 
    display: flex; align-items: center;
    text-decoration: none;
}
.back-btn svg {
    width: 20px; height: 20px;
    stroke: currentColor; fill: none;
    stroke-width: 2; stroke-linecap: round; stroke-linejoin: round;
}
.page-title { font-size: 22px; font-weight: 700; margin-left:50px; color: #1a1a1a; }
.page-subtitle { font-size: 14px; color: #888; margin-left:150px; }

/* ════════════════════════════════
   CARD
════════════════════════════════ */
.card-write {
    background: var(--white);
    border: 1px solid var(--border);
    border-radius: var(--radius-card);
    padding: 36px 40px;
    max-width: 680px;
    margin: 0 auto;
    box-shadow: var(--shadow-card);
}

/* ════════════════════════════════
   FORM
════════════════════════════════ */
.form-group { margin-bottom: 24px; }
.form-label {
    display: flex; align-items: center; gap: 4px;
    font-size: 14px; font-weight: 600;
    color: #333; margin-bottom: 8px;
}
.required { color: var(--red); font-size: 13px; }

.form-select,
.form-input {
    width: 100%; padding: 12px 16px;
    border: 1px solid #e0e4e8; border-radius: 10px;
    font-size: 14px; color: #222;
    font-family: inherit; background: var(--white);
    outline: none; appearance: none; -webkit-appearance: none;
    transition: border-color .2s, box-shadow .2s;
}
.form-select {
    background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%23aaa' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E");
    background-repeat: no-repeat;
    background-position: right 14px center;
    padding-right: 40px; cursor: pointer;
}
.form-select:focus,
.form-input:focus {
    border-color: var(--green-primary);
    box-shadow: 0 0 0 3px rgba(46,125,79,.08);
}

.textarea-wrap { position: relative; }
.form-textarea {
    width: 100%; padding: 14px 16px;
    border: 1px solid #e0e4e8; border-radius: 10px;
    font-size: 14px; color: #222;
    font-family: inherit; background: var(--white);
    outline: none; resize: none;
    min-height: 160px; line-height: 1.7;
    transition: border-color .2s, box-shadow .2s;
}
.form-textarea:focus {
    border-color: var(--green-primary);
    box-shadow: 0 0 0 3px rgba(46,125,79,.08);
}
.char-count {
    position: absolute; top: 12px; right: 14px;
    font-size: 12px; color: var(--text-muted);
}

/* ════════════════════════════════
   FILE UPLOAD
════════════════════════════════ */
.file-upload-area {
    border: 1.5px dashed #d0d5dd;
    border-radius: 10px; padding: 32px 20px;
    text-align: center; cursor: pointer;
    transition: border-color .2s, background .2s;
    background: #fafafa;
}
.file-upload-area:hover {
    border-color: var(--green-primary);
    background: #f8fdf9;
}
.file-upload-area svg {
    width: 32px; height: 32px;
    stroke: var(--text-muted); fill: none;
    stroke-width: 1.5; stroke-linecap: round; stroke-linejoin: round;
    margin: 0 auto 10px; display: block;
}
.file-upload-text { font-size: 14px; color: #555; margin-bottom: 4px; }
.file-upload-hint { font-size: 12px; color: var(--text-muted); }
.file-input { display: none; }

/* ════════════════════════════════
   NOTICE BOX
════════════════════════════════ */
.notice-box {
    display: flex; align-items: flex-start; gap: 10px;
    background: #f0f4ff; border-radius: 10px;
    padding: 16px 18px; margin-top: 8px;
}
.notice-box svg {
    width: 18px; height: 18px;
    stroke: #4a6cf7; fill: none;
    stroke-width: 1.8; stroke-linecap: round; stroke-linejoin: round;
    flex-shrink: 0; margin-top: 1px;
}
.notice-box p { font-size: 13px; color: #555; line-height: 1.6; }

/* ════════════════════════════════
   BUTTON ROW  (취소 + 문의 등록하기)
════════════════════════════════ */
.btn-row {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 12px;
    margin-top: 28px;
}
.btn-cancel {
    background: var(--white);
    color: var(--text-sub);
    border: 1px solid var(--border);
    padding: 12px 28px;
    border-radius: 10px;
    font-size: 15px;
    font-weight: 500;
    font-family: inherit;
    cursor: pointer;
    transition: background .2s, border-color .2s;
}
.btn-cancel:hover {
    background: #f5f6f8;
    border-color: #ccc;
}
.btn-submit {
    display: flex;
    align-items: center;
    gap: 6px;
    background: var(--green-primary);
    color: var(--white);
    border: none;
    padding: 12px 28px;
    border-radius: 10px;
    font-size: 15px;
    font-weight: 600;
    font-family: inherit;
    cursor: pointer;
    transition: background .2s;
}
.btn-submit:hover { background: var(--green-dark); }
.btn-submit svg {
    width: 16px; height: 16px;
    stroke: currentColor; fill: none;
    stroke-width: 2; stroke-linecap: round; stroke-linejoin: round;
}

/* ════════════════════════════════
   ERROR
════════════════════════════════ */
.alert-error {
    background: #fff5f5; border: 1px solid #fcc;
    border-radius: 10px; padding: 12px 16px;
    font-size: 13px; color: var(--red); margin-bottom: 20px;
}
.field-error { font-size: 12px; color: var(--red); margin-top: 5px; display: none; }
.field-error.show { display: block; }

</style>
<script>
    /* 글자 수 카운트 */
    var bodyEl    = document.getElementById('body');
    var charCount = document.getElementById('charCount');
    bodyEl.addEventListener('input', function () {
        charCount.textContent = bodyEl.value.length + '/1000자';
    });

    /* 파일 업로드 영역 */
    var dropZone  = document.getElementById('dropZone');
    var fileInput = document.getElementById('fileInput');

    dropZone.addEventListener('click', function () { fileInput.click(); });

    dropZone.addEventListener('dragover', function (e) {
        e.preventDefault();
        dropZone.style.borderColor = '#2e7d4f';
        dropZone.style.background  = '#f8fdf9';
    });
    dropZone.addEventListener('dragleave', function () {
        dropZone.style.borderColor = '#d0d5dd';
        dropZone.style.background  = '#fafafa';
    });
    dropZone.addEventListener('drop', function (e) {
        e.preventDefault();
        dropZone.style.borderColor = '#d0d5dd';
        dropZone.style.background  = '#fafafa';
        if (e.dataTransfer.files.length) showFileName(e.dataTransfer.files[0].name);
    });
    fileInput.addEventListener('change', function () {
        if (fileInput.files.length) showFileName(fileInput.files[0].name);
    });
    function showFileName(name) {
        var hint = dropZone.querySelector('.file-upload-hint');
        hint.textContent = '선택된 파일: ' + name;
        hint.style.color = '#2e7d4f';
    }

    /* 폼 유효성 검사 */
    document.getElementById('supportForm').addEventListener('submit', function (e) {
        var valid = true;

        var subject    = document.getElementById('title');
        var subjectErr = document.getElementById('subjectError');
        if (!subject.value.trim()) {
            subjectErr.classList.add('show');
            subject.style.borderColor = '#e53935';
            valid = false;
        } else {
            subjectErr.classList.remove('show');
            subject.style.borderColor = '';
        }

        var body    = document.getElementById('content');
        var bodyErr = document.getElementById('bodyError');
        if (!body.value.trim()) {
            bodyErr.classList.add('show');
            body.style.borderColor = '#e53935';
            valid = false;
        } else {
            bodyErr.classList.remove('show');
            body.style.borderColor = '';
        }

        if (!valid) e.preventDefault();
    });
</script>

</body>
</html>
