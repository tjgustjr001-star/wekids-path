<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/teacher-class-manage.css">

<div class="class-content-shell">
    <div class="class-content-hero">
        <div class="class-content-hero__icon"><i class="fa-solid fa-gear"></i></div>
        <div class="class-content-hero__text">
            <h1 class="class-content-hero__title">클래스 설정</h1>
            <p class="class-content-hero__subtitle">${not empty className ? className : (not empty classInfo.className ? classInfo.className : '현재 클래스')}</p>
        </div>
    </div>
<section class="teacher-class-manage-page">
    <div class="class-content-panel">
    <div class="manage-summary-grid">
        <div class="summary-card">
            <div class="summary-icon users-summary-icon green"></div>
            <div class="summary-text-box">
                <p>등록 학생 수</p>
                <strong>${empty studentCount ? 0 : studentCount}명</strong>
            </div>
        </div>

        <div class="summary-card">
            <div class="summary-icon learn-summary-icon blue"></div>
            <div class="summary-text-box">
                <p>등록 학습 자료</p>
                <strong>${empty learnCount ? 0 : learnCount}개</strong>
            </div>
        </div>

        <div class="summary-card">
            <div class="summary-icon assignment-summary-icon purple"></div>
            <div class="summary-text-box">
                <p>진행 중 과제</p>
                <strong>${empty activeAssignmentCount ? 0 : activeAssignmentCount}개</strong>
            </div>
        </div>
    </div>

    <section class="manage-section">
        <div class="section-title-row">
            <h2>
                <span class="section-title-icon settings-section-icon"></span>
                클래스 기본 설정
            </h2>
        </div>

        <div class="manage-form-box">
            <form action="${pageContext.request.contextPath}/teacher/classes/${classId}/manage" method="post" class="manage-form">
                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="className">클래스 이름</label>
                        <input type="text" id="className" name="className"
                               value="${empty className ? '' : className}">
                    </div>

                    <div class="form-field">
                        <label for="classStatus">상태 변경</label>
                        <select id="classStatus" name="classStatus">
                            <option value="ACTIVE" ${classStatus eq 'ACTIVE' ? 'selected' : ''}>운영중 (공개)</option>
                            <option value="BLINDED" ${classStatus eq 'BLINDED' ? 'selected' : ''}>준비중 (비공개)</option>
                            <option value="ARCHIVED" ${classStatus eq 'ARCHIVED' ? 'selected' : ''}>종료 (읽기 전용)</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="grade">학년</label>
                        <select id="grade" name="grade">
                            <option value="1" ${grade == 1 ? 'selected' : ''}>1학년</option>
                            <option value="2" ${grade == 2 ? 'selected' : ''}>2학년</option>
                            <option value="3" ${grade == 3 ? 'selected' : ''}>3학년</option>
                            <option value="4" ${grade == 4 ? 'selected' : ''}>4학년</option>
                            <option value="5" ${grade == 5 ? 'selected' : ''}>5학년</option>
                            <option value="6" ${grade == 6 ? 'selected' : ''}>6학년</option>
                            <option value="0" ${grade == 0 ? 'selected' : ''}>기타/방과후</option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label for="classNo">반</label>
                        <input type="number" id="classNo" name="classNo"
                               value="${empty classNo ? '' : classNo}" placeholder="예: 2">
                    </div>
                </div>

                <div class="form-field">
                    <label for="description">클래스 소개</label>
                    <textarea id="description" name="description" rows="5"
                              placeholder="학생들에게 보여질 클래스 소개 문구를 작성하세요.">${empty description ? '' : description}</textarea>
                </div>

                <div class="form-action-row">
                    <button type="submit" class="primary-save-btn">변경사항 저장</button>
                </div>
            </form>
        </div>
    
    </div>
</section>

    <section class="manage-section">
        <div class="section-title-row">
            <h2>
                <span class="section-title-icon assignment-section-icon"></span>
                과제 제출 기본 설정
            </h2>
        </div>

        <div class="manage-form-box">
            <form action="${pageContext.request.contextPath}/teacher/classes/${classId}/assignment-settings" method="post" class="manage-form">
                <div class="form-grid two-cols">
                    <div class="form-field">
                        <label for="defaultDueTime">과제 마감 기본 시간</label>
                        <select id="defaultDueTime" name="defaultDueTime">
                            <option value="18:00" ${defaultDueTime eq '18:00' ? 'selected' : ''}>18:00</option>
                            <option value="21:00" ${defaultDueTime eq '21:00' ? 'selected' : ''}>21:00</option>
                            <option value="23:59" ${defaultDueTime eq '23:59' ? 'selected' : ''}>23:59</option>
                        </select>
                        <p class="field-help-text">새로운 과제 생성 시 기본으로 설정될 마감 시간입니다.</p>
                    </div>

                    <div class="form-field">
                        <label for="allowSubmissionModifyYn">제출 후 수정 허용</label>
                        <select id="allowSubmissionModifyYn" name="allowSubmissionModifyYn">
                            <option value="1" ${allowSubmissionModifyYn == 1 ? 'selected' : ''}>허용</option>
                            <option value="0" ${allowSubmissionModifyYn == 0 ? 'selected' : ''}>불가</option>
                        </select>
                        <p class="field-help-text">학생이 제출 후 다시 수정할 수 있을지 설정합니다.</p>
                    </div>
                </div>

                <div class="form-action-row">
                    <button type="submit" class="primary-save-btn">설정 저장</button>
                </div>
            </form>
        </div>
    </section>

    <section class="manage-section">
        <div class="section-title-row">
            <h2>
                <span class="section-title-icon shield-section-icon"></span>
                초대 코드 관리
            </h2>
        </div>

        <div class="invite-manage-box">
            <div class="invite-info-box">
                <h3>학생 및 학부모 초대 코드</h3>
                <p>이 코드를 공유하여 학생과 학부모가 클래스에 참여할 수 있도록 하세요. 코드는 언제든지 재발급할 수 있습니다.</p>

                <div class="invite-code-row">
                    <div class="invite-code-box">
                        ${empty inviteCode ? '-' : inviteCode}
                    </div>

                    <button type="button" class="secondary-line-btn" onclick="navigator.clipboard.writeText('${inviteCode}')">복사</button>

                    <form action="${pageContext.request.contextPath}/teacher/classes/${classId}/invite-code" method="post">
                        <button type="submit" class="secondary-green-btn">재발급</button>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <section class="danger-zone-section">
        <div class="section-title-row">
            <h2 class="danger-title">
                <span class="section-title-icon danger-section-icon"></span>
                위험 구역
            </h2>
        </div>

        <div class="danger-zone-box">
            <div class="danger-text-box">
                <h3>클래스 종료</h3>
                <p>현재 구조에서는 물리 삭제 대신 클래스를 종료(ARCHIVED) 처리합니다.</p>
            </div>

            <form action="${pageContext.request.contextPath}/teacher/classes/${classId}/delete" method="post">
                <button type="submit" class="danger-outline-btn">클래스 종료</button>
            </form>
        </div>
    </section>
</section>
</div>