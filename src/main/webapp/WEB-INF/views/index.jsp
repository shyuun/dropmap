<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
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
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'G-7YGDSJE9JB');
    </script>
</head>
<body>
<div class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style='font-family: "Plus Jakarta Sans", "Noto Sans", sans-serif;'>
    <div class="layout-container flex flex-col min-h-0">
        <header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#f1f2f4] px-10 py-3">
            <a href="javascript:location.reload()">
                <div class="flex items-center gap-2 text-[#121416]">
                    <div class="size-10">
                        <img src="${pageContext.request.contextPath}/img/favicon/logo_round.png" alt="dropmap 로고" class="w-full h-full"/>
                    </div>
                    <h2 class="text-[#121416] text-lg font-bold leading-tight tracking-[-0.015em]">dropmap</h2>
                </div>
            </a>
            <div class="flex gap-2">
                <button id="addressSearchBtn" class="flex max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 bg-[#f1f2f4] text-[#121416] gap-2 text-sm font-bold leading-normal tracking-[0.015em] min-w-0 px-2.5">
                    <div class="text-[#121416]" data-icon="MagnifyingGlass" data-size="20px" data-weight="regular">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" fill="currentColor" viewBox="0 0 256 256">
                            <path d="M229.66,218.34l-50.07-50.06a88.11,88.11,0,1,0-11.31,11.31l50.06,50.07a8,8,0,0,0,11.32-11.32ZM40,112a72,72,0,1,1,72,72A72.08,72.08,0,0,1,40,112Z"></path>
                        </svg>
                    </div>
                </button>
                <button id="infoDetailBtn" class="flex max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-xl h-10 bg-[#f1f2f4] text-[#121416] gap-2 text-sm font-bold leading-normal tracking-[0.015em] min-w-0 px-2.5">
                    <div class="text-[#121416]" data-icon="Info" data-size="20px" data-weight="regular">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" fill="currentColor" viewBox="0 0 256 256">
                            <path d="M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm0,192a88,88,0,1,1,88-88A88.1,88.1,0,0,1,128,216Zm16-40a8,8,0,0,1-8,8,16,16,0,0,1-16-16V128a8,8,0,0,1,0-16,16,16,0,0,1,16,16v40A8,8,0,0,1,144,176ZM112,84a12,12,0,1,1,12,12A12,12,0,0,1,112,84Z"></path>
                        </svg>
                    </div>
                </button>
            </div>
        </header>
        <div id="guideBar" class="mx-3 md:mx-10 mt-3">
            <div class="rounded-xl bg-white/90 backdrop-blur border border-gray-200 shadow-sm p-3 md:p-4">
                <div class="flex items-start gap-3">
                    <div class="flex-1 text-sm md:text-base leading-relaxed">
                        👕 dropmap은 <strong>의류수거함 위치 안내 서비스</strong>입니다.
                        <br class="md:hidden">
                        집 근처 의류수거함을 쉽게 찾아보세요!
                    </div>
                </div>
            </div>
        </div>
        <main>
            <div id="wrapper" class="relative flex flex-1 min-h-0 w-full md:flex-row flex-col">
            <!-- 좌측 광고 -->
            <aside id="leftAd" class="hidden md:block md:w-48 lg:w-56 xl:w-64 bg-gray-50 border-r border-gray-200">
                <!-- 구글 애드센스 코드 들어갈 자리 -->
                <div class="p-2 text-xs text-center min-h-[250px]">
                    <!-- 좌광고 -->
                    <ins class="adsbygoogle sideads"
                         style="display:block"
                         data-ad-client="ca-pub-4694284708905804"
                         data-ad-slot="3352268982"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                </div>
            </aside>

            <!-- 지도 -->
            <div id="map" class="w-full h-[60vh] md:h-[650px]"></div>

            <!-- 우측 광고 -->
            <aside id="rightAd" class="hidden md:block md:w-48 lg:w-56 xl:w-64 bg-gray-50 border-l border-gray-200">
                <!-- 구글 애드센스 코드 들어갈 자리 -->
                <div class="p-2 text-xs text-center min-h-[250px]">
                    <!-- 우광고 -->
                    <ins class="adsbygoogle sideads"
                         style="display:block"
                         data-ad-client="ca-pub-4694284708905804"
                         data-ad-slot="5128524706"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                </div>
            </aside>
        </div>
        </main>
        <!-- 하단 광고 (모바일에서만 표시) -->
        <footer id="footerAd" class="block w-full bg-gray-50 border-t border-gray-200">
            <div class="p-2 text-center min-h-[320px]">
                <!-- 하단광고 -->
                <ins class="adsbygoogle footerads"
                     style="display:block"
                     data-ad-client="ca-pub-4694284708905804"
                     data-ad-slot="3048284131"
                     data-ad-format="auto"
                     data-full-width-responsive="true"></ins>
            </div>
        </footer>
    </div>
</div>

<div id="searchLayer" class="popup-box">
    <button onclick="closeSearchLayer()" class="popup-close-btn" aria-label="닫기">×</button>
    <div id="daumPostcodeInner"></div>
</div>

<div id="infoLayer" class="popup-box">
    <button onclick="closeInfoLayer()" class="popup-close-btn" aria-label="닫기">×</button>
    <div class="popup-inner-content">
        <h2>🧺 의류수거함 배출 기준 안내</h2>

        <h3>✅ 수거 가능한 품목</h3>
        <table>
            <thead>
            <tr><th>분류</th><th>품목</th></tr>
            </thead>
            <tbody>
            <tr>
                <td>👕 의류류</td>
                <td>일반 의류 (상의, 하의, 외투 등)</td>
            </tr>
            <tr>
                <td>👟 패션잡화</td>
                <td>신발 (운동화, 구두 등)<br>일반 가방 (백팩, 숄더백 등)</td>
            </tr>
            <tr>
                <td>🛏 침구/직물류</td>
                <td>담요, 누비이불<br>커튼, 카펫<br>베개커버, 이불커버</td>
            </tr>
            </tbody>
        </table>

        <h3>❌ 수거 불가능한 품목</h3>
        <table>
            <thead>
            <tr><th>분류</th><th>품목</th></tr>
            </thead>
            <tbody>
            <tr>
                <td>🛏 침구류</td>
                <td>솜이불, 베개, 방석</td>
            </tr>
            <tr>
                <td>⚠️ 특수 품목</td>
                <td>전기장판, 전기요</td>
            </tr>
            <tr>
                <td>🧳 바퀴류</td>
                <td>바퀴 달린 신발 (롤러스케이트, 휠리스)<br>바퀴 달린 가방 (캐리어 등)</td>
            </tr>
            </tbody>
        </table>

        <p>🔔 <strong>TIP:</strong> 수거 불가 품목은 <em>대형폐기물 신고</em> 또는 <em>재활용센터</em>를 통해 배출해야 해요.<br>
            지역마다 기준이 다를 수 있으니, <strong>자치구 안내문</strong>도 꼭 확인해 주세요!</p>
    </div>
</div>

<script>
    window.addEventListener('load', () => {
        initVisibleAds();
        window.addEventListener('resize', () => setTimeout(initVisibleAds, 300)); // 브레이크포인트 전환 대응
    });

    let idleTimer;
    const SEOUL_CENTER = new naver.maps.LatLng(37.56661, 126.978388);
    var markers = [], infoWindows = [], regionGeoJson = [], polygons = [];
    var markerClustering;

    const mapOptions = {
        center: SEOUL_CENTER,//지도의 초기 중심 좌표
        zoom: 17,
        minZoom: 10,
        zoomControl: true,
        zoomControlOptions: {
            position: naver.maps.Position.TOP_RIGHT
        }
    };

    const map = new naver.maps.Map('map', mapOptions);

    // 예시로 각 줌레벨별 마커 구성
    var htmlMarker1 = generateCircleMarker("marker-blue", 80, 80, 14, 40, 40);
    var htmlMarker2 = generateCircleMarker("marker-midblue", 65, 65, 13, 32, 22);
    var htmlMarker3 = generateCircleMarker("marker-lightblue", 60, 60, 11, 30, 30);
    var htmlMarker4 = {
        content : `<div class="clusterBtn"></div>`,
        size : N.Size(100,100),
        anchor : N.Point(50,50)
    }

    naver.maps.Event.once(map, 'init', function () {
        //customControl 객체 이용하기

        var locationBtnHtml =
            `<button type="button" class="btn_location" id="currentLocationBtn" aria-pressed="false">
                <svg width="24" height="24" viewBox="0 0 29 29" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                    <path class="icon-path"
                        d="M13.89 23.01V21a.61.61 0 0 1 1.22 0v2.01a8.533 8.533 0 0 0 7.9-7.9H21a.61.61 0 0 1 0-1.22h2.01a8.533 8.533 0 0 0-7.9-7.9V8a.61.61 0 0 1-1.22 0V5.99a8.533 8.533 0 0 0-7.9 7.9H8a.61.61 0 0 1 0 1.22H5.99a8.533 8.533 0 0 0 7.9 7.9zm10.36-8.51c0 5.385-4.365 9.75-9.75 9.75s-9.75-4.365-9.75-9.75 4.365-9.75 9.75-9.75 9.75 4.365 9.75 9.75zm-9.75 1.625a1.625 1.625 0 1 0 0-3.25 1.625 1.625 0 0 0 0 3.25z"/>
                </svg>
            </button>`;

        var noticeHtml =
            ` <div class="rounded-lg bg-white px-3 py-2 shadow-md border border-gray-200 text-xs md:text-sm leading-relaxed">
            <div class="flex flex-col gap-0.5">
              <span>🟦 마커 = 의류수거함 위치</span>
              <span>🖱️ 클릭 = 상세 위치</span>
            </div>
          </div>`;

        var customControl = new naver.maps.CustomControl(locationBtnHtml, {
            position: naver.maps.Position.TOP_RIGHT
        });

        customControl.setMap(map);

        var customControl2 = new naver.maps.CustomControl(noticeHtml, {
            position: naver.maps.Position.TOP_LEFT
        });
        customControl2.setMap(map);

        naver.maps.Event.addDOMListener(customControl.getElement(), 'click', function () {
            setCurrentPosition();
        });

        naver.maps.Event.trigger(map, 'idle');
    });

    $(function() {
        setCurrentPosition();

        naver.maps.Event.addListener(map, 'idle', function() {
            // 마커 갱신 로직 등
            clearTimeout(idleTimer);
            idleTimer = setTimeout(function () {
                getInfo(map.getZoom());
            }, 200); // 200ms 안에 연속 idle 발생하면 무시
        });

        document.getElementById("infoDetailBtn").addEventListener("click", function () {
            if($("#infoLayer").css("display") !== "none"){
                $('.popup-box').hide()
            } else {
                $('.popup-box').hide()
                $("#infoLayer").show();
            }
        });

        //카카오 주소검색 이벤트
        document.getElementById("addressSearchBtn").addEventListener("click", function () {
            if($("#searchLayer").css("display") !== "none"){
                $('.popup-box').hide()
                return;
            } else {
                $('.popup-box').hide()
            }
            var element_layer = document.getElementById('searchLayer');

            new daum.Postcode({
                oncomplete: function(data) {
                    element_layer.style.display = 'none'; // 닫기
                    var fullAddr = data.roadAddress || data.jibunAddress;

                    // 카카오 주소 → 좌표 변환
                    $.ajax({
                        url: "https://dapi.kakao.com/v2/local/search/address.JSON",
                        type: "GET",
                        data: { query: fullAddr },
                        headers: {
                            Authorization: "KakaoAK e71d9b282245d3aefd979738c1f585f6"  // 카카오 REST API 키 넣기
                        },
                        success: function(res) {
                            if (res.documents.length > 0) {
                                var coord = res.documents[0].address;
                                var lat = coord.y;
                                var lng = coord.x;

                                const latlng = new naver.maps.LatLng(lat, lng);
                                map.setCenter(latlng);
                                map.setZoom(17);
                            } else {
                                alert("해당 주소에 대한 좌표를 찾을 수 없습니다.");
                            }
                        },
                        error: function(err) {
                            console.error(err);
                            alert("주소 좌표 검색 중 오류가 발생했습니다.");
                        }
                    });
                },
                width : '100%',
                height : '92%'
            }).embed(element_layer);
            element_layer.style.display = 'block';
        });

        $(document).on('keydown', function (e) {
            if (e.key === "Escape" || e.keyCode === 27) {
                $('.popup-box').hide(); // 모든 팝업 닫기
            }
        });
    });

    function closeSearchLayer() {
        document.getElementById('searchLayer').style.display = 'none';
    }

    function closeInfoLayer() {
        document.getElementById('infoLayer').style.display = 'none';
    }

    function getInfo(zoom) {// 10 이하: 서울시 / 11~12: 구 / 13: 동 / 14~15: 클러스터링 / 16 이상: 마커
        if (zoom > 14){
            //클러스터링,마커 호출
            getMarkerInfo(zoom);
        } else {
            //지역정보 가져오기
            getDistrictInfo(zoom);
        }
    }

    function getMarkerInfo(zoom) {
        $.ajax({
            type : 'GET',
            url : '/api/getMarkerInfo',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : getMapBoundsParams()
        }).done(setMarkerInfo)
            .fail(error =>{
                console.error("❌ 마커 요청 실패", error);
                alert(JSON.stringify(error));
            });
    }

    function getDistrictInfo(zoom) {
        $.ajax({
            type : 'GET',
            url : '/api/getDistrictInfo',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : getMapBoundsParams(zoom)
        }).done(data => {
            setDistrictInfo(data,zoom);
        }).fail(error => {
            alert(JSON.stringify(error));
        })
    }

    function setMarkerInfo(data){
        clearMarkers(markers,polygons,infoWindows);

        for (var i = 0; i < data.length; i++) {
            var spot = data[i];
            var latlng = new naver.maps.LatLng(spot.lat, spot.lot);
            var marker = new naver.maps.Marker({
                position: latlng,
                draggable: false,
                map: map
            });
            var infoWindow = new naver.maps.InfoWindow({
                content: '<div style="width:200px;text-align:center;padding:10px;font-size: 12px;">📍&nbsp;'+ spot.lotAddress +'</div>'
            });

            markers.push(marker);
            infoWindows.push(infoWindow);
        }

        markerClustering = new MarkerClustering({
            minClusterSize: 1,
            maxZoom: 17,
            map: map,
            markers: markers,
            disableClickZoom: false,
            gridSize: 400,
            icons: [htmlMarker4],
            indexGenerator: [200],
            stylingFunction: function (clusterMarker, count) {
                $(clusterMarker.getElement()).find('.clusterBtn').text(count);
            }
        });

        markerClustering.setMarkers(markers); // 새 markers 등록
        markerClustering._redraw();           // 클러스터링 재생성

        for (var i=0, ii=markers.length; i<ii; i++) {
            naver.maps.Event.addListener(markers[i], 'click', markerClickHandler(i));
        }
    }

    function setDistrictInfo(data,zoom){
        clearMarkers(markers,polygons,infoWindows);

        for (var i = 0; i < data.length; i++) {
            var spot = data[i];
            var latlng = new naver.maps.LatLng(spot.lat, spot.lot);
            var htmlMarker;

            //아이콘생성
            if(zoom <= 10){//시
                htmlMarker = htmlMarker1;
            } else if(zoom === 11 || zoom === 12 || zoom === 13){//구
                htmlMarker = htmlMarker2;
            } else if(zoom === 14){//법정동
                htmlMarker = htmlMarker3;
            }

            var $content = $(htmlMarker.content).clone();
            $content.addClass(spot.code);
            $content.find(".marker_nm").text(spot.name);
            $content.find(".marker_cnt").text(spot.count);

            var newHtmlMarker = {
                content: $content.prop('outerHTML'),
                size: htmlMarker.size,
                anchor: htmlMarker.anchor
            };

            //마커생성
            var marker = new naver.maps.Marker({
                position: latlng,
                draggable: false,
                map: map,
                icon: newHtmlMarker
            });

            markers.push(marker);

            //폴리곤 생성
            var polygon = new naver.maps.Polygon({
                paths: JSON.parse(spot.boundary), // 여기에 받아온 경계 좌표를 넣어준다.
                fillColor: 'rgb(21, 101, 192)',
                fillOpacity: 0.3,
                strokeColor: 'rgb(21, 101, 192)',
                strokeWeight: 2,
                strokeOpacity: 0.4,
                zIndex: 2,
                clickable: true,
                visible:false,
                map: map // 위에서 생성한 지도에 띄운다
            });
            polygons.push(polygon);
        }

        for (let i=0, ii=markers.length; i<ii; i++) {
            naver.maps.Event.addListener(markers[i], 'click', districtInfoClickHandler(i));
            naver.maps.Event.addListener(markers[i], 'mouseover', function(e){
                polygons[i].setOptions({
                    visible:true
                })
            });
            naver.maps.Event.addListener(markers[i], 'mouseout', function(e){
                polygons[i].setOptions({
                    visible:false
                })
            });
        }
    }

    function clearMarkers(markers,polygons,infoWindows){
        for (const marker of markers) marker.setMap(null);
        for (const polygon of polygons) polygon.setMap(null);
        for (const infoWindow of infoWindows) infoWindow.setMap(null);

        markers.length = 0;
        polygons.length = 0;
        infoWindows.length = 0;

        if(markerClustering){
            markerClustering._clearClusters();
            markerClustering.setMap(null);
        }
    }

    function districtInfoClickHandler(seq) {
        return function(e) {
            var prevZoom = map.getZoom();
            var zoom = 0;
            if(prevZoom == map.getMaxZoom()){
                zoom = prevZoom;
            } else {
                zoom = prevZoom + 1;
            }
            map.morph(new naver.maps.LatLng(e.coord.lat(), e.coord.lng()), zoom);//클릭한곳이 중앙으로 오게
        }
    }

    function markerClickHandler(seq) {
        return function(e) {
            var marker = markers[seq],
                infoWindow = infoWindows[seq];

            if (infoWindow.getMap()) {
                infoWindow.close();
            } else {
                infoWindow.open(map, marker);
            }
        }
    }

    function updateMarkers(map, markers) {
        var mapBounds = map.getBounds();
        var marker, position;

        for (var i = 0; i < markers.length; i++) {

            marker = markers[i]
            position = marker.getPosition();

            if (mapBounds.hasLatLng(position)) {
                showMarker(map, marker);
            } else {
                hideMarker(map, marker);
            }
        }
    }

    function showMarker(map, marker) {
        if (marker.getMap()) return;
        marker.setMap(map);
    }

    function hideMarker(map, marker) {
        if (!marker.getMap()) return;
        marker.setMap(null);
    }

    function setCurrentPosition() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(location);
                map.setZoom(17);
            });
        }
    }

    function getMapBoundsParams(zoom = null) {
        const bounds = map.getBounds();
        const sw = bounds.getSW();
        const ne = bounds.getNE();

        const params = {
            lat1: sw.lat(),
            lat2: ne.lat(),
            lot1: sw.lng(),
            lot2: ne.lng()
        };

        if (zoom !== null) {
            params.zoomLevel = zoom;
        }

        return params;
    }

    function generateCircleMarker(className, width, height, fontSize, anchorX, anchorY) {
        return {
            content: `<div class="circle-marker ${'$'}{className}" style="width:${'$'}{width}px;height:${'$'}{height}px;font-size:${'$'}{fontSize}px;">
                            <p class="marker_nm"></p>
                            <p class="marker_cnt" style="font-size:${'$'}{fontSize - 2}px;"></p>
                          </div>`,
            size: N.Size(width, height),
            anchor: N.Point(anchorX, anchorY)
        };
    }

    function initVisibleAds() {
        document.querySelectorAll('ins.adsbygoogle').forEach((ins) => {
            if (ins.getAttribute('data-ad-status') === 'filled') return; // 이미 렌더된 광고 건너뜀
            const visible = ins.offsetParent !== null && ins.clientWidth > 0;
            if (visible) { try { (adsbygoogle = window.adsbygoogle || []).push({}); } catch(e){} }
        });
    }
</script>
</body>
</html>
