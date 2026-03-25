<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>자녀 상세 보기</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            background: #f6f7fb;
            font-family: 'Malgun Gothic', sans-serif;
            color: #1f2937;
        }

        .detail-page {
            max-width: 1180px;
            margin: 0 auto;
            padding: 28px 24px 60px;
        }

        .top-back-row { margin-bottom: 18px; }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            color: #6b7280;
            text-decoration: none;
        }

        .back-link:hover { color: #111827; }

        .top-control-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 18px;
        }

        .child-select-chip {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: #fff;
            border: 1px solid #e5e7eb;
            border-radius: 14px;
            padding: 10px 14px;
            box-shadow: 0 4px 14px rgba(0,0,0,0.04);
        }

        .chip-avatar {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            background: #e9f7ef;
            color: #159a6f;
            font-size: 13px;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .chip-name {
            font-size: 14px;
            font-weight: 600;
            color: #374151;
        }

        .main-grid {
            display: grid;
            grid-template-columns: 300px 1fr;
            gap: 18px;
        }

        .left-col,
        .right-col {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .card {
            background: #fff;
            border: 1px solid #e5e7eb;
            border-radius: 18px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.04);
        }

        .profile-card {
            padding: 28px 24px;
            text-align: center;
        }

        .profile-avatar {
            width: 72px;
            height: 72px;
            margin: 0 auto 16px;
            border-radius: 50%;
            background: #e9f7ef;
            color: #159a6f;
            font-size: 30px;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        .profile-avatar img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .profile-name {
            font-size: 28px;
            font-weight: 800;
            color: #111827;
            margin-bottom: 6px;
        }

        .profile-sub {
            font-size: 13px;
            color: #6b7280;
            margin-bottom: 18px;
        }

        .profile-bottom {
            border-top: 1px solid #eef2f7;
            padding-top: 14px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
            color: #6b7280;
        }

        .profile-bottom .right-green {
            color: #159a6f;
            font-weight: 700;
        }

        .teacher-card {
            padding: 18px 18px 16px;
        }

        .section-title {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
            font-weight: 700;
            color: #111827;
            margin-bottom: 14px;
        }

        .teacher-message-box {
            background: #f7f9ff;
            border: 1px solid #cddafe;
            border-radius: 12px;
            padding: 14px;
            font-size: 13px;
            line-height: 1.7;
            color: #4b5563;
            min-height: 84px;
            white-space: pre-line;
        }

        .teacher-sign {
            margin-top: 10px;
            text-align: right;
            font-size: 12px;
            color: #9ca3af;
        }

        .stats-row {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 14px;
        }

        .stat-card {
            padding: 20px 18px;
            text-align: center;
        }

        .stat-icon {
            width: 34px;
            height: 34px;
            margin: 0 auto 10px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
        }

        .stat-icon.green-soft {
            background: #e9f7ef;
            color: #159a6f;
        }

        .stat-icon.mint-soft {
            background: #eafaf6;
            color: #10b981;
        }

        .stat-icon.orange-soft {
            background: #fff4e8;
            color: #f59e0b;
        }

        .stat-value {
            font-size: 30px;
            font-weight: 800;
            color: #111827;
            line-height: 1.1;
        }

        .stat-label {
            margin-top: 6px;
            font-size: 12px;
            color: #6b7280;
        }

        .progress-card,
        .check-card {
            padding: 20px 18px;
        }

        .subject-item { margin-bottom: 16px; }
        .subject-item:last-child { margin-bottom: 0; }

        .subject-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
            margin-bottom: 6px;
        }

        .subject-name {
            color: #374151;
            font-weight: 600;
        }

        .subject-rate {
            color: #111827;
            font-weight: 700;
        }

        .bar {
            width: 100%;
            height: 6px;
            background: #edf0f5;
            border-radius: 999px;
            overflow: hidden;
        }

        .fill {
            height: 100%;
            border-radius: 999px;
            background: #159a6f;
        }

        .empty-guide {
            font-size: 13px;
            color: #6b7280;
            background: #f9fafb;
            border: 1px dashed #d1d5db;
            border-radius: 12px;
            padding: 14px;
            line-height: 1.6;
        }

        .check-header {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
            font-weight: 700;
            color: #111827;
            margin-bottom: 14px;
        }

        .count {
            color: #ef4444;
            font-size: 13px;
            margin-left: 4px;
        }

        .alert-item {
            border-radius: 14px;
            padding: 14px 14px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
        }

        .alert-item.red-box {
            background: #fff5f5;
            border: 1px solid #fecaca;
        }

        .alert-left {
            display: flex;
            align-items: flex-start;
            gap: 12px;
        }

        .alert-icon {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            margin-top: 2px;
            background: #ffe4e6;
            color: #ef4444;
        }

        .alert-title {
            font-size: 13px;
            font-weight: 700;
            margin-bottom: 4px;
            color: #dc2626;
        }

        .alert-desc {
            font-size: 12px;
            color: #6b7280;
        }

        .bottom-btn-row {
            display: flex;
            gap: 12px;
            margin-top: 18px;
        }

        .list-btn,
        .unlink-btn {
            height: 46px;
            padding: 0 18px;
            border-radius: 12px;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
        }

        .list-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            background: #eef2f7;
            color: #374151;
            border: 1px solid #dbe2ea;
        }

        .unlink-btn {
            border: none;
            background: #ef4444;
            color: #fff;
        }

        .unlink-btn:hover {
            background: #dc2626;
        }

        @media (max-width: 960px) {
            .main-grid {
                grid-template-columns: 1fr;
            }

            .stats-row {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<div class="detail-page">

    <div class="top-back-row">
        <a href="${pageContext.request.contextPath}/parent/settings/child-link" class="back-link">
            <i class="fa-solid fa-chevron-left"></i> 자녀 목록으로
        </a>
    </div>

    <div class="top-control-row">
        <div></div>

        <div class="child-select-chip">
            <div class="chip-avatar">
                <c:choose>
                    <c:when test="${not empty child.studentName}">
                        ${child.studentName.substring(0,1)}
                    </c:when>
                    <c:otherwise>자</c:otherwise>
                </c:choose>
            </div>
            <div class="chip-name">${child.studentName}</div>
            <i class="fa-solid fa-angle-down" style="font-size:12px; color:#9ca3af;"></i>
        </div>
    </div>

    <div class="main-grid">

        <div class="left-col">
            <div class="card profile-card">
                <div class="profile-avatar">
                    <c:choose>
                        <c:when test="${not empty child.profileImage}">
                            <img src="${pageContext.request.contextPath}/resources/upload/${child.profileImage}" alt="프로필">
                        </c:when>
                        <c:when test="${not empty child.studentName}">
                            ${child.studentName.substring(0,1)}
                        </c:when>
                        <c:otherwise>자</c:otherwise>
                    </c:choose>
                </div>

                <div class="profile-name">${child.studentName}</div>

                <div class="profile-sub">
                    <c:choose>
                        <c:when test="${child.year > 0 && child.grade > 0 && child.classNo > 0}">
                            ${child.year}학년도 ${child.grade}학년 ${child.classNo}반
                        </c:when>
                        <c:otherwise>
                            학급 정보가 없습니다.
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="profile-bottom">
                    <span><i class="fa-regular fa-clock"></i> 최근 연동일</span>
                    <span class="right-green">
                        <c:choose>
                            <c:when test="${not empty child.linkedAt}">
                                <fmt:formatDate value="${child.linkedAt}" pattern="yyyy-MM-dd HH:mm"/>
                            </c:when>
                            <c:otherwise>정보 없음</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>

            <div class="card teacher-card">
                <div class="section-title">
                    <i class="fa-regular fa-comment" style="color:#4f7cff;"></i>
                    <span>선생님 한마디</span>
                </div>

                <div class="teacher-message-box">${child.teacherComment}</div>

                <div class="teacher-sign">최근 리포트 기준</div>
            </div>
        </div>

        <div class="right-col">

            <div class="stats-row">
                <div class="card stat-card">
                    <div class="stat-icon green-soft">
                        <i class="fa-solid fa-signal"></i>
                    </div>
                    <div class="stat-value">${child.learningProgressRate}%</div>
                    <div class="stat-label">전체 학습 진행률</div>
                </div>

                <div class="card stat-card">
                    <div class="stat-icon mint-soft">
                        <i class="fa-regular fa-rectangle-list"></i>
                    </div>
                    <div class="stat-value">${child.completedLearningCount}/${child.totalLearningCount}</div>
                    <div class="stat-label">학습 완료</div>
                </div>

                <div class="card stat-card">
                    <div class="stat-icon orange-soft">
                        <i class="fa-regular fa-clipboard"></i>
                    </div>
                    <div class="stat-value">${child.submittedAssignmentCount}/${child.totalAssignmentCount}</div>
                    <div class="stat-label">과제 제출</div>
                </div>
            </div>

            <div class="card progress-card">
                <div class="section-title">
                    <i class="fa-regular fa-file-lines" style="color:#10b981;"></i>
                    <span>학습/과제 현황</span>
                </div>

                <div class="subject-item">
                    <div class="subject-top">
                        <span class="subject-name">전체 학습 진행률</span>
                        <span class="subject-rate">${child.learningProgressRate}%</span>
                    </div>
                    <div class="bar">
                        <div class="fill" style="width:${child.learningProgressRate}%;"></div>
                    </div>
                </div>

                <div class="subject-item">
                    <div class="subject-top">
                        <span class="subject-name">과제 제출률</span>
                        <span class="subject-rate">${child.assignmentRate}%</span>
                    </div>
                    <div class="bar">
                        <div class="fill" style="width:${child.assignmentRate}%;"></div>
                    </div>
                </div>

                <div class="subject-item" style="margin-top:18px;">
                    <div class="empty-guide">
                        과목별 학습률은 현재 과목 정보 테이블이 확인되지 않아 아직 연결되지 않았습니다.
                    </div>
                </div>
            </div>

            <div class="card check-card">
                <div class="check-header">
                    <i class="fa-regular fa-circle-xmark" style="color:#ef4444;"></i>
                    <span>확인이 필요한 항목</span>
                    <span class="count">${child.unconfirmedNoticeCount}</span>
                </div>

                <c:choose>
                    <c:when test="${child.unconfirmedNoticeCount > 0}">
                        <div class="alert-item red-box">
                            <div class="alert-left">
                                <div class="alert-icon">
                                    <i class="fa-regular fa-file-lines"></i>
                                </div>
                                <div>
                                    <div class="alert-title">미확인 가정통신문이 있습니다.</div>
                                    <div class="alert-desc">확인 필요한 가정통신문 수: ${child.unconfirmedNoticeCount}건</div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-guide">
                            현재 확인이 필요한 가정통신문이 없습니다.
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="bottom-btn-row">
                    <a href="${pageContext.request.contextPath}/parent/settings/child-link" class="list-btn">
                        목록으로
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>

</body>
</html>