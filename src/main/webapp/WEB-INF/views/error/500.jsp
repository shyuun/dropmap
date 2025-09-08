<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>500 Internal Server Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css"/>
</head>
<body class="error-body">
<div class="error-wrap">
    <div class="brand"></div>
    <span class="badge" style="background:rgba(255,107,107,.12);border-color:rgba(255,107,107,.35);color:var(--danger)">Server Error</span>

    <div class="header">
        <div class="code">500</div>
        <div>
            <div class="title">서버에서 오류가 발생했어요.</div>
            <p class="desc">잠시 후 다시 시도해주세요.</p>
            <div class="actions">
                <button class="btn primary" onclick="location.reload()">새로고침</button>
                <button class="btn primary" onclick="location.href='/'">홈으로 가기</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
