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

        #left { grid-area: left; }
        #right { grid-area: right; }
        #footer { grid-area: footer; }
    </style>
</head>
<body>
<div id="wrapper">
    <div id="logo">DropMap</div>
    <div id="left">광고영역</div>
    <div id="map"></div>
    <div id="right">광고영역</div>
    <div id="footer">하단 광고영역</div>
</div>

<script>
    const SEOUL_CENTER = new naver.maps.LatLng(37.56661, 126.978388);
    var markers = [], infoWindows = [], regionGeoJson = [], polygons = [];

    const mapOptions = {
        center: SEOUL_CENTER,//지도의 초기 중심 좌표
        zoom: 10,
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
    var htmlMarker4 = generateCircleMarker('#2196f3', '#1e88e5', 55, 55, 10, 27, 27, '#1565c0', '#0d47a1', 0.75);
    var htmlMarker5 = generateCircleMarker('#90caf9', '#64b5f6', 30, 30, 9, 15, 35, '#1565c0', '#0d47a1', 0.6);

    $(function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const location = new naver.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(location);
                map.setZoom(10);
            });
        }

        naver.maps.Event.addListener(map, 'idle', function() {
            console.log('지도 idle 발생');
            // 마커 갱신 로직 등
            //updateMarkers(map, markers);
        });

        naver.maps.Event.addListener(map, 'zoom_changed', function(zoom) {
            console.log("ZOOM CHANGED!!");
            // ~10- 서울시
            // 11-구
            // 12-구
            // 13-동
            // 14-클러스터링
            // 15~-마커
            if(zoom > 15){
                //마커가져오기
            } else if(zoom > 14){
                //클러스터링 호출
            } else {
                //지역정보 가져오기
                getDistrictInfo(zoom);
            }
        });

        naver.maps.Event.trigger(map, 'zoom_changed');
    });

    function getDistrictInfo(zoom) {
        console.log("getDistrictInfo!");
        $.ajax({
            type : 'GET',
            url : '/api/getDistrictInfo',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : {"zoomLevel" : zoom}
        }).done(function(data){
            console.log(data);
            setDistrictInfo(data,zoom);
        }).fail(function(error){
            alert(JSON.stringify(error));
        })
    }

    function setDistrictInfo(data,zoom){
        clearMarkers(markers,polygons);

        for (var i = 0; i < data.length; i++) {
            var spot = data[i];
            var latlng = new naver.maps.LatLng(spot.lat, spot.lot);
            var htmlMarker;

            //아이콘생성
            if(zoom <= 10){//시
                htmlMarker = htmlMarker1;
            } else if(zoom == 11 || zoom == 12){//구
                htmlMarker = htmlMarker2;
            } else if(zoom == 13){//법정동
                htmlMarker = htmlMarker3;
            } else if(zoom == 14){//클러스터링

            } else {//마커

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

        for(var i=0; i<markers.length; i++){
            naver.maps.Event.addListener(markers[i], 'click', markerClickHandler(i));
            naver.maps.Event.addListener(markers[i], 'mouseover', function(e){
                polygons[i].setOptions({//TODO polygons 수정
                    visible:true
                })
            });
            naver.maps.Event.addListener(markers[i], 'mouseout', function(e){
                polygons[i].setOptions({
                    visible:false
                })
            });
        }

        //TODO
        naver.maps.Event.trigger(map, 'idle');
    }

    function clearMarkers(markers,polygons){
        for(var i=0; i<markers.length; i++){
            markers[i].setMap(null);
            polygons[i].setMap(null);
        }
        markers.length = 0;
        polygons.length = 0;
    }

    // 해당 마커의 인덱스를 seq라는 클로저 변수로 저장하는 이벤트 핸들러를 반환합니다.
    function markerClickHandler(seq) {
        return function(e) {
            var prevZoom = map.getZoom();
            var zoom = 0;
            if(prevZoom == map.getMaxZoom()){
                zoom = prevZoom;
            } else {
                zoom = prevZoom + 1;
            }
            map.morph(new naver.maps.LatLng(e.coord.lat(), e.coord.lng()), zoom);
        }
    }

    function generateCircleMarker(bgColor, hoverColor, width, height, fontSize, anchorX, anchorY, borderColor, hoverBorderColor, opacity) {
        return {
            content: `
            <div class="clusterBtn" style="
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
