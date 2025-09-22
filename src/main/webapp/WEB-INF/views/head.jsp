<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <title>의류수거함 | 헌옷수거함</title>
    <meta name="description" content="내 주변 의류수거함(헌옷수거함) 위치를 빠르게 찾는 dropmap. 지도를 움직이면 범위 안 수거함을 자동으로 보여줍니다.">
    <meta name="keywords" content="의류수거함, 헌옷수거함, 의류수거함 위치, 헌옷 배출, 재활용, dropmap">
    <meta name="robots" content="index,follow">
    <link rel="canonical" href="https://dropmap.kr/">
    <!-- PWA/브라우저 색상 -->
    <meta name="theme-color" content="#1976d2">
    <meta name="format-detection" content="telephone=no,address=no,email=no">
    <!-- Open Graph -->
    <meta property="og:type" content="website">
    <meta property="og:locale" content="ko_KR">
    <meta property="og:site_name" content="dropmap">
    <meta property="og:title" content="의류수거함 | 헌옷수거함">
    <meta property="og:description" content="내 주변 의류수거함 위치를 한눈에! 지도를 이동하면 자동으로 보여줘요.">
    <meta property="og:url" content="https://dropmap.kr/">
    <!-- og:image는 실제 경로로 교체 -->
    <meta property="og:image" content="https://dropmap.kr/favicon/og-image.png">
    <meta property="og:image:alt" content="dropmap 의류수거함 지도 미리보기">
    <link rel="apple-touch-icon" sizes="57x57" href="${pageContext.request.contextPath}/img/favicon/apple-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="${pageContext.request.contextPath}/img/favicon/apple-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="${pageContext.request.contextPath}/img/favicon/apple-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="${pageContext.request.contextPath}/img/favicon/apple-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="${pageContext.request.contextPath}/img/favicon/apple-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="${pageContext.request.contextPath}/img/favicon/apple-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="${pageContext.request.contextPath}/img/favicon/apple-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="${pageContext.request.contextPath}/img/favicon/apple-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="${pageContext.request.contextPath}/img/favicon/apple-icon-180x180.png">
    <link rel="icon" type="image/png" sizes="192x192"  href="${pageContext.request.contextPath}/img/favicon/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/img/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="${pageContext.request.contextPath}/img/favicon/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/img/favicon/favicon-16x16.png">
    <link rel="manifest" href="${pageContext.request.contextPath}/img/favicon/manifest.json">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="${pageContext.request.contextPath}/img/favicon/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css"/>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=wzu7skvkq1"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/MarkerClustering.js"></script>
    <script type="text/javascript" src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin="" />
    <link rel="stylesheet" as="style" onload="this.rel='stylesheet'" href="https://fonts.googleapis.com/css2?display=swap&amp;family=Noto+Sans%3Awght%40400%3B500%3B700%3B900&amp;family=Plus+Jakarta+Sans%3Awght%40400%3B500%3B700%3B800"/>
    <link rel="icon" type="image/x-icon" href="data:image/x-icon;base64," />
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-4694284708905804" crossorigin="anonymous"></script><%-- Google adsense --%>
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-7YGDSJE9JB"></script><!-- Google tag (gtag.js) -->
    <script>
        window.addEventListener('load', () => {
            initVisibleAds();
            window.addEventListener('resize', () => setTimeout(initVisibleAds, 300)); // 브레이크포인트 전환 대응
        });

        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'G-7YGDSJE9JB');

        function initVisibleAds() {
            document.querySelectorAll('ins.adsbygoogle').forEach((ins) => {
                if (ins.getAttribute('data-ad-status') === 'filled') return; // 이미 렌더된 광고 건너뜀
                const visible = ins.offsetParent !== null && ins.clientWidth > 0;
                if (visible) { try { (adsbygoogle = window.adsbygoogle || []).push({}); } catch(e){} }
            });
        }
    </script>
</head>