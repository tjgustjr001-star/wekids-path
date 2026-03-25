<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/teacher/class/create.css">

<section class="class-create-page">
    <div class="class-create-card">

        <div class="class-create-hero">
            <div class="hero-icon"></div>
            <h1>새 클래스 개설</h1>
            <p>클래스를 생성하고 학생들을 초대하세요.</p>
        </div>

        <form class="class-create-form"
              action="${pageContext.request.contextPath}/teacher/classes/new"
              method="post">

            <div class="form-group full">
                <label for="className">클래스 이름 *</label>
                <input type="text"
                       id="className"
                       name="className"
                       placeholder="예: 2026학년도 3학년 2반"
                       required>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="year">학년도 *</label>
                    <input type="number"
                           id="year"
                           name="year"
                           value="2026"
                           required>
                </div>

                <div class="form-group">
                    <label for="semester">학기 *</label>
                    <select id="semester" name="semester" required>
                        <option value="1">1학기</option>
                        <option value="2">2학기</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="grade">학년 *</label>
                    <select id="grade" name="grade" required>
                        <option value="">선택</option>
                        <option value="1">1학년</option>
                        <option value="2">2학년</option>
                        <option value="3">3학년</option>
                        <option value="4">4학년</option>
                        <option value="5">5학년</option>
                        <option value="6">6학년</option>
                        <option value="0">기타/방과후</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="classNo">반</label>
                    <input type="number"
                           id="classNo"
                           name="classNo"
                           placeholder="예: 2">
                </div>
            </div>

            <div class="form-group full">
                <label for="description">클래스 소개</label>
                <textarea id="description"
                          name="description"
                          rows="5"
                          placeholder="클래스에 대한 간단한 소개를 작성해주세요."></textarea>
            </div>

            <div class="guide-box">
                <h3>클래스 생성 후 안내</h3>
                <ul>
                    <li>클래스가 생성되면 초대 코드가 자동으로 발급됩니다.</li>
                    <li>초대 코드를 학생들에게 공유하여 클래스에 참여시킬 수 있습니다.</li>
                    <li>클래스 설정은 생성 후에도 수정할 수 있습니다.</li>
                </ul>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/teacher/classes" class="cancel-btn">취소</a>
                <button type="submit" class="submit-btn">클래스 생성하기 →</button>
            </div>
        </form>
    </div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/teacher/class/create.js"></script>