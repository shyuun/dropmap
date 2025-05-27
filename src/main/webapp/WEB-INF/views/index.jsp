<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
<script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=6gkjtzogno"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/MarkerClustering.js"></script>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <title>DropMap - 지도 시각화</title>

    <!-- 외부 라이브러리 -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=6gkjtzogno"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${pageContext.request.contextPath}/js/MarkerClustering.js"></script>

    <style>
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            overflow: hidden;
            font-family: Arial, sans-serif;
        }

        #wrapper {
            display: grid;
            grid-template-columns: 200px 1fr 200px;
            grid-template-rows: 50px 1fr 120px;
            grid-template-areas:
                "logo header header"
                "left map right"
                "footer footer footer";
            height: 100vh;
            gap: 10px;
        }

        #logo {
            grid-area: logo;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            background-color: #1976d2;
            color: white;
        }

        #map {
            grid-area: map;
            width: 100%;
            height: 80%;
            align-self: center;
            justify-self: center;
        }

        #left, #right, #footer {
            background-color: #f0f0f0;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #666;
            font-size: 14px;
        }

        #left {
            grid-area: left;
            background-color: #f0f0f0;
            display: flex;
            flex-direction: column;      /* 추가 */
            align-items: center;
            justify-content: flex-start; /* 수정 */
            color: #666;
            font-size: 14px;
            padding-top: 20px;           /* 상단 여백 */
        }

        #right { grid-area: right; }
        #footer { grid-area: footer; }

        #addressSearchBtn {
            background-color: #1976d2;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 8px 12px;
            font-size: 14px;
            cursor: pointer;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            width: 100%;
            margin-bottom: 20px; /* 하단 여백만 */
        }
        #addressSearchBtn:hover {
            background-color: #1565c0;
        }
    </style>
</head>
<body>
<div id="layer" style="display:none; position:fixed; top:46%; left:50%; transform:translate(-50%, -50%); z-index:100; border:1px solid #ccc; background:#fff; box-shadow: 0 4px 12px rgba(0,0,0,0.3);">
    <div style="text-align: right; padding: 8px; background-color: #ececec;">
        <button onclick="closeDaumPostcode()" style="background: none; border: none; font-size: 16px; cursor: pointer;">❌</button>
    </div>
    <div id="daumPostcodeInner"></div>
</div>
<div id="wrapper">
    <div id="logo">DropMap</div>
    <div id="left">
        <button id="addressSearchBtn">🔍 주소 검색</button>
        광고영역
    </div>
    <div id="map"></div>
    <div id="right">광고영역</div>
    <div id="footer">하단 광고영역</div>
</div>

<script>
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
    var htmlMarker1 = generateCircleMarker('#1976d2', '#1565c0', 80, 80, 14, 40, 40, '#1565c0', '#0d47a1', 0.8);
    var htmlMarker2 = generateCircleMarker('#2196f3', '#1e88e5', 65, 65, 13, 32, 22, '#1565c0', '#0d47a1', 0.75);
    var htmlMarker3 = generateCircleMarker('#42a5f5', '#2196f3', 60, 60, 11, 30, 30, '#1565c0', '#0d47a1', 0.75);
    var htmlMarker4 = {
        content : `<div class="clusterBtn" style="cursor: pointer; width: 100px; height: 100px; line-height: 100px; font-size: 27px; color: white; text-align: center; font-weight: bold; background: url(/img/cluster_01.png) 0% 0% / contain;"></div>`,
        size : N.Size(100,100),
        anchor : N.Point(50,50)
    }

    $(function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(location);
                map.setZoom(17);
            });
        }

        naver.maps.Event.addListener(map, 'idle', function() {
            // 마커 갱신 로직 등
            getInfo(map.getZoom());
            // updateMarkers(map, markers);
        });

        naver.maps.Event.addListener(map, 'zoom_changed', function(zoom) {
            getInfo(zoom);
        });

        naver.maps.Event.trigger(map, 'zoom_changed');

        //카카오 주소검색 이벤트
        document.getElementById("addressSearchBtn").addEventListener("click", function () {
            var element_layer = document.getElementById('layer');
            var inner_container = document.getElementById('daumPostcodeInner');

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
                height : '100%'
            }).embed(element_layer);

            element_layer.style.display = 'block';
            element_layer.style.width = '400px';
            element_layer.style.height = '500px';
        });
    });

    function closeDaumPostcode() {
        document.getElementById('layer').style.display = 'none';
    }


    function getInfo(zoom){
        // ~10- 서울시
        // 11-구
        // 12-구
        // 13-동
        // 14-클러스터링
        // 15-클러스터링
        // 16~-마커
        if(zoom > 14){
            //클러스터링,마커 호출
            getMarkerInfo(zoom);
        } else {
            //지역정보 가져오기
            getDistrictInfo(zoom);
        }
    }

    function getMarkerInfo(zoom) {
        const mapBounds = map.getBounds(); // 현재 지도 범위
        const southWest = mapBounds.getSW(); // 남서쪽 (left-bottom)
        const northEast = mapBounds.getNE(); // 북동쪽 (right-top)

        $.ajax({
            type : 'GET',
            url : '/api/getMarkerInfo',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : {"lat1" : southWest.lat(), "lat2" : northEast.lat(), "lot1" : southWest.lng(), "lot2": northEast.lng()}
        }).done(function(data){
            setMarkerInfo(data,zoom);
        }).fail(function(error){
            alert(JSON.stringify(error));
        })
    }

    function getDistrictInfo(zoom) {
        const mapBounds = map.getBounds(); // 현재 지도 범위
        const southWest = mapBounds.getSW(); // 남서쪽 (left-bottom)
        const northEast = mapBounds.getNE(); // 북동쪽 (right-top)

        $.ajax({
            type : 'GET',
            url : '/api/getDistrictInfo',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : {"zoomLevel" : zoom, "lat1" : southWest.lat(), "lat2" : northEast.lat(), "lot1" : southWest.lng(), "lot2": northEast.lng()}
        }).done(function(data){
            setDistrictInfo(data,zoom);
        }).fail(function(error){
            alert(JSON.stringify(error));
        })
    }

    function setMarkerInfo(data,zoom){
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

        //naver.maps.Event.trigger(map, 'idle');
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

        //naver.maps.Event.trigger(map, 'idle');
    }

    function clearMarkers(markers,polygons,infoWindows){
        for(var i=0; i<markers.length; i++){
            markers[i].setMap(null);
            if(polygons.length !== 0){
                polygons[i].setMap(null);
            }

            if(infoWindows .length !== 0){
                infoWindows[i].setMap(null);
            }
        }
        markers.length = 0;

        if(polygons.length !== 0) {
            polygons.length = 0;
        }
        if(infoWindows .length !== 0) {
            infoWindows.length = 0;
        }

        if(markerClustering != null){
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

    function generateCircleMarker(bgColor, hoverColor, width, height, fontSize, anchorX, anchorY, borderColor, hoverBorderColor, opacity) {
        return {
            content: `
            <div style="
                width: ${'$'}{width}px;
                height: ${'$'}{height}px;
                background-color: ${'$'}{bgColor};
                opacity: ${'$'}{opacity};
                border: 2px solid ${'$'}{borderColor};
                border-radius: 50%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                font-size: ${'$'}{fontSize}px;
                font-weight: 700;
                color: white;
                text-align: center;
                cursor: pointer;
                transition:
                    background-color 0.3s ease,
                    transform 0.3s ease,
                    box-shadow 0.3s ease,
                    border-color 0.3s ease,
                    opacity 0.3s ease;
            "
            onmouseover="this.style.backgroundColor='${'$'}{hoverColor}'; this.style.borderColor='${'$'}{hoverBorderColor}'; this.style.transform='scale(1.05)'; this.style.boxShadow='0 4px 10px rgba(0,0,0,0.25)';"
            onmouseout="this.style.backgroundColor='${'$'}{bgColor}'; this.style.borderColor='${'$'}{borderColor}'; this.style.transform='scale(1)'; this.style.boxShadow='none';"
            onmousedown="this.style.transform='scale(0.97)';"
            onmouseup="this.style.transform='scale(1.05)';"
            >
                <p class="marker_nm" style="margin: 0;"></p>
                <p class="marker_cnt" style="margin: 0; font-size: ${'$'}{fontSize - 2}px;"></p>
            </div>
        `,
            size: N.Size(width, height),
            anchor: N.Point(anchorX, anchorY)
        };
    }
</script>
</body>
</html>
