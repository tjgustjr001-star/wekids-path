<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="child-detail-page">
    <div class="top-row">
        <a class="back-link" href="${pageContext.request.contextPath}/parent/settings/child-link">← 자녀 목록으로</a>
    </div>

    <div class="detail-layout">
        <div class="profile-card">
            <div class="avatar">${child.childName.substring(0,1)}</div>
            <h3>${child.childName} 학생</h3>
            <p>${child.schoolName} ${child.grade}학년 ${child.className}반</p>
            <p>출석률: ${child.attendanceRate}%</p>
            <p>과제 제출률: ${child.assignmentRate}%</p>
        </div>

        <div class="info-card">
            <h3>자녀 상세 정보</h3>
            <div class="info-row"><strong>이름</strong> ${child.childName}</div>
            <div class="info-row"><strong>학교</strong> ${child.schoolName}</div>
            <div class="info-row"><strong>학년</strong> ${child.grade}</div>
            <div class="info-row"><strong>반</strong> ${child.className}</div>
            <div class="info-row"><strong>번호</strong> ${child.studentNo}</div>
            <div class="info-row"><strong>최근 갱신</strong> ${child.lastUpdated}</div>

            <div class="comment-box">
                <strong>선생님 한마디</strong>
                <p>${child.teacherComment}</p>
            </div>

            <form method="post"
                  action="${pageContext.request.contextPath}/parent/settings/child-link/${child.childId}/remove"
                  onsubmit="return confirm('정말로 자녀 연동을 해제하시겠습니까?');">
                <button type="submit" class="unlink-btn">자녀 연동 해제</button>
            </form>
        </div>
    </div>
</div>