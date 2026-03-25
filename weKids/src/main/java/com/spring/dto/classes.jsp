<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="activeClassCount" value="0" />
<c:set var="archiveClassCount" value="0" />
<c:set var="blindedClassCount" value="0" />

<c:forEach items="${classList}" var="cls">
	<c:choose>
		<c:when test="${cls.classStatus eq 'ACTIVE'}">
			<c:set var="activeClassCount" value="${activeClassCount + 1}" />
		</c:when>
		<c:when test="${cls.classStatus eq 'ARCHIVED'}">
			<c:set var="archiveClassCount" value="${archiveClassCount + 1}" />
		</c:when>
		<c:when test="${cls.classStatus eq 'BLINDED'}">
			<c:set var="blindedClassCount" value="${blindedClassCount + 1}" />
		</c:when>
	</c:choose>
</c:forEach>

<input type="hidden" id="activeClassCount" value="${activeClassCount}">
<input type="hidden" id="archiveClassCount" value="${archiveClassCount}">
<input type="hidden" id="blindedClassCount" value="${blindedClassCount}">

<c:set var="totalClassCount" value="${fn:length(classList)}" />
<div class="admin-classes-page">

    <div class="page-header-row">
        <div>
            <h1 class="page-title">클래스 관리</h1>
            <p class="page-desc">생성된 모든 클래스를 모니터링하고 상태를 관리합니다.</p>
        </div>

        <div class="page-header-actions">
            <button type="button" class="outline-btn">상세 필터</button>
        </div>
    </div>

    <div class="classes-top-grid">
        <!-- 상태 분포 -->
        <section class="panel-card">
            <h2 class="panel-title center">클래스 상태 분포</h2>

            <div class="chart-wrap">
                <canvas id="classStatusChart"></canvas>
            </div>

            <div class="status-summary">
			    <div class="status-summary-item">
			        <div class="status-summary-left">
			            <span class="status-dot active"></span>
			            <span>활성 클래스</span>
			        </div>
			        <strong>${activeClassCount}개</strong>
			    </div>
			
			    <div class="status-summary-item">
			        <div class="status-summary-left">
			            <span class="status-dot archive"></span>
			            <span>아카이브</span>
			        </div>
			        <strong>${archiveClassCount}개</strong>
			    </div>
			
			    <div class="status-summary-item">
			        <div class="status-summary-left">
			            <span class="status-dot blind"></span>
			            <span>블라인드</span>
			        </div>
			        <strong>${blindedClassCount}개</strong>
			    </div>
			</div>
        </section>

        <!-- 모니터링 알림 -->
        <section class="panel-card wide">
            <div class="panel-head-line">
                <h2 class="panel-title">모니터링 알림</h2>
                <button type="button" class="text-btn">모두 읽음</button>
            </div>

            <div class="monitor-list">
                <div class="monitor-item">
                    <div class="monitor-icon warning"></div>
                    <div class="monitor-body">
                        <p>비정상적인 접속 시도가 감지된 클래스가 있습니다. (3학년 2반)</p>
                        <span>10분 전</span>
                    </div>
                    <button type="button" class="mini-btn">확인</button>
                </div>

                <div class="monitor-item">
                    <div class="monitor-icon danger"></div>
                    <div class="monitor-body">
                        <p>신고가 누적된 클래스에 대한 블라인드 처리가 필요합니다.</p>
                        <span>1시간 전</span>
                    </div>
                    <button type="button" class="mini-btn">확인</button>
                </div>

                <div class="monitor-item">
                    <div class="monitor-icon info"></div>
                    <div class="monitor-body">
                        <p>지난 학기 클래스 150개가 아카이브 처리되었습니다.</p>
                        <span>어제</span>
                    </div>
                    <button type="button" class="mini-btn">확인</button>
                </div>

                <div class="monitor-item">
                    <div class="monitor-icon info"></div>
                    <div class="monitor-body">
                        <p>신규 개설된 클래스가 24개 있습니다.</p>
                        <span>어제</span>
                    </div>
                    <button type="button" class="mini-btn">확인</button>
                </div>
            </div>
        </section>
    </div>

    <section class="classes-board">
        <div class="classes-board-top">
            <div class="tab-group">
			    <button type="button" class="tab-btn active" data-filter="전체">전체</button>
			    <button type="button" class="tab-btn" data-filter="ACTIVE">활성</button>
			    <button type="button" class="tab-btn" data-filter="ARCHIVED">아카이브</button>
			    <button type="button" class="tab-btn" data-filter="BLINDED">블라인드</button>
			</div>
            <div class="search-box">
                <input type="text" id="classSearchInput" placeholder="클래스명 또는 교사명 검색">
            </div>
        </div>

        <div class="class-card-grid" id="classCardGrid">
			<c:forEach items="${classList}" var="cls">
				<div class="class-card"
				     data-status="${cls.classStatus}"
				     data-name="${cls.className}"
				     data-teacher="${cls.teacherName}">
		
					<div class="class-card-top">
						<span class="badge
							${cls.classStatus eq 'ACTIVE' ? 'active' :
							  (cls.classStatus eq 'ARCHIVED' ? 'archive' : 'blind')}">
							<c:choose>
								<c:when test="${cls.classStatus eq 'ACTIVE'}">활성</c:when>
								<c:when test="${cls.classStatus eq 'ARCHIVED'}">아카이브</c:when>
								<c:when test="${cls.classStatus eq 'BLINDED'}">블라인드</c:when>
								<c:otherwise>${cls.classStatus}</c:otherwise>
							</c:choose>
						</span>
		
						<div class="menu-wrap">
							<button type="button" class="menu-toggle-btn">⋮</button>
							<div class="card-menu">
							    <form action="${pageContext.request.contextPath}/admin/classes/status" method="post">
							        <input type="hidden" name="classId" value="${cls.classId}">
							        <input type="hidden" name="classStatus" value="ACTIVE">
							        <button type="submit" class="menu-action">활성으로 변경</button>
							    </form>
							
							    <form action="${pageContext.request.contextPath}/admin/classes/status" method="post">
							        <input type="hidden" name="classId" value="${cls.classId}">
							        <input type="hidden" name="classStatus" value="ARCHIVED">
							        <button type="submit" class="menu-action">아카이브 처리</button>
							    </form>
							
							    <form action="${pageContext.request.contextPath}/admin/classes/status" method="post">
							        <input type="hidden" name="classId" value="${cls.classId}">
							        <input type="hidden" name="classStatus" value="BLINDED">
							        <button type="submit" class="menu-action danger">블라인드 처리</button>
							    </form>
							</div>
						</div>
					</div>
		
					<div class="class-card-body open-detail-btn"
					     data-id="${cls.classId}"
					     data-name="${cls.className}"
					     data-teacher="${cls.teacherName}"
					     data-students="${cls.studentCount}"
					     data-status="${cls.classStatus}"
					     data-last-active="${cls.lastActive}"
					     data-description="${cls.description}"
					     data-school="${cls.schoolName}">
						<h3>${cls.className}</h3>
						<p>담당: ${cls.teacherName} | 코드: ${cls.classId}</p>
		
						<div class="class-card-bottom">
							<span class="student-count">학생 ${cls.studentCount}명</span>
							<span class="last-active">최근 변경: ${cls.lastActive}</span>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>

        <div class="classes-board-footer">
            <button type="button" class="more-btn">더 보기</button>
        </div>
    </section>

</div>

<!-- 상세 모달 -->
<div class="class-detail-modal" id="classDetailModal">
    <div class="class-detail-dialog">
        <div class="class-detail-header">
            <h3>클래스 상세 정보</h3>
            <button type="button" id="closeClassModalBtn" class="close-modal-btn">×</button>
        </div>

        <div class="class-detail-body">
            <div class="detail-main-title">
                <div class="detail-title-row">
                    <h4 id="modalClassName">클래스명</h4>
                    <span id="modalClassStatus" class="badge active">활성</span>
                </div>
                <p id="modalClassId">코드: C001</p>
            </div>

            <div class="detail-info-grid">
                <div class="detail-box">
                    <p>담당 교사</p>
                    <strong id="modalTeacher">-</strong>
                </div>
                <div class="detail-box">
                    <p>소속 학교</p>
                    <strong id="modalSchool">-</strong>
                </div>
                <div class="detail-box">
                    <p>참여 학생</p>
                    <strong id="modalStudents">-</strong>
                </div>
                <div class="detail-box">
                    <p>최근 변경</p>
                    <strong id="modalLastActive">-</strong>
                </div>
            </div>

            <div class="detail-desc-wrap">
                <p>클래스 설명</p>
                <div class="detail-desc-box" id="modalDescription"></div>
            </div>

            <div class="detail-action-row">
                <button type="button" class="modal-action-btn active-btn" id="modalActivateBtn">활성으로 변경</button>
                <button type="button" class="modal-action-btn blind-btn" id="modalBlindBtn">블라인드 처리</button>
            </div>
        </div>
    </div>
</div>