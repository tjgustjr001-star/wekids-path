<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle} | We-Kids</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/common-base.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/common-header.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/common-sidebar.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/common-sidebar-icons.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/common-class.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common/parent-layout.css">
	
</head>
<body>
    <div class="app-shell parent-shell">
    <%@ include file="/WEB-INF/views/common/layout/parentSidebar.jsp" %>

    <div class="app-main">
        <%@ include file="/WEB-INF/views/common/layout/commonHeader.jsp" %>

        <main class="app-content">
            <jsp:include page="${contentPage}" />
        </main>
    </div>
</div>

    <script src="${pageContext.request.contextPath}/resources/js/common/parent-layout.js"></script>
</body>
</html>