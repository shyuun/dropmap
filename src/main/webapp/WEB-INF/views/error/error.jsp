<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title><c:out value="${status}"/> <c:out value="${error}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css"/>
</head>
<body class="error-body">
<div class="error-wrap">
    <div class="brand"></div>
    <span class="badge">Error</span>

    <div class="header">
        <div class="code"><c:out value="${status}"/></div>
        <div>
            <div class="title"><c:out value="${error}"/></div>
            <p class="desc"><c:out value="${message}"/></p>
            <div class="actions">
                <button class="btn primary" onclick="location.href='/'">홈으로 가기</button>
                <button class="btn" onclick="history.back()">이전 페이지</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
