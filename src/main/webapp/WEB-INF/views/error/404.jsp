<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <title>404 Not Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css"/>
</head>
<body>
<div class="error-wrap">
    <div class="brand"></div>
    <span class="badge">Not Found</span>
    <div class="header">
        <div class="code">404</div>
        <div>
            <div class="title">페이지를 찾을 수 없어요.</div>
            <p class="desc">주소가 변경되었거나 일시적으로 사용할 수 없습니다. 주소를 다시 확인하거나 아래 버튼을 이용해 주세요.</p>
            <div class="actions">
                <button class="btn primary" onclick="location.href='/'">홈으로 가기</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
