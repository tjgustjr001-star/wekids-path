<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="dashboard-page">
    <div class="page-title-box">
        <h1>관리자 홈</h1>
        <p>플랫폼 전체의 현황을 모니터링하고 관리하세요.</p>
    </div>

    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-top">
                <div class="icon-box blue"></div>
                <span class="trend positive">+12%</span>
            </div>
            <div class="stat-label">전체 사용자</div>
            <div class="stat-value">12,345</div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div class="icon-box green"></div>
                <span class="trend positive">+5%</span>
            </div>
            <div class="stat-label">활성 클래스</div>
            <div class="stat-value">842</div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div class="icon-box pink"></div>
                <span class="trend negative">-2%</span>
            </div>
            <div class="stat-label">신규 가입 교사</div>
            <div class="stat-value">38</div>
        </div>

        <div class="stat-card">
            <div class="stat-top">
                <div class="icon-box gray"></div>
                <span class="trend warning">주의</span>
            </div>
            <div class="stat-label">미해결 문의</div>
            <div class="stat-value">15</div>
        </div>
    </div>

    <div class="dashboard-bottom">
        <section class="chart-panel">
            <div class="panel-header">
                <h2>주간 접속 트렌드</h2>
                <button type="button" class="detail-btn">자세히 보기</button>
            </div>
            <div class="chart-box">
                <canvas id="weeklyTrendChart"></canvas>
            </div>
        </section>

        <section class="alert-panel">
            <h2>시스템 알림</h2>

            <div class="alert-item">
                <span class="dot info"></span>
                <div>
                    <div class="alert-title">DB 백업 완료</div>
                    <div class="alert-time">10분 전</div>
                </div>
            </div>

            <div class="alert-item">
                <span class="dot warning"></span>
                <div>
                    <div class="alert-title">API 응답 지연 발생</div>
                    <div class="alert-time">1시간 전</div>
                </div>
            </div>

            <div class="alert-item">
                <span class="dot info"></span>
                <div>
                    <div class="alert-title">신규 관리자 로그인</div>
                    <div class="alert-time">3시간 전</div>
                </div>
            </div>

            <div class="alert-item no-border">
                <span class="dot danger"></span>
                <div>
                    <div class="alert-title">서버 리소스 사용량 80% 초과</div>
                    <div class="alert-time">5시간 전</div>
                </div>
            </div>
        </section>
    </div>
</div>