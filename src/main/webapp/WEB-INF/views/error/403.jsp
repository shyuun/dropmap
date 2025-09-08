<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>403 Forbidden</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css"/>
</head>
<body class="error-body">
<div class="error-wrap">
    <div class="brand"></div>
    <span class="badge" style="background:rgba(255,184,77,.12);border-color:rgba(255,184,77,.35);color:var(--warn)">Forbidden</span>

    <div class="header">
        <div class="code">403</div>
        <div>
            <div class="title">접근 권한이 없어요.</div>
            <p class="desc">로그인이 필요하거나 권한이 없는 계정일 수 있어요. 아래 버튼으로 진행해 보세요.</p>
            <div class="actions">
                <button class="btn primary" onclick="location.href='/'">홈으로 가기</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
