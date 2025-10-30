<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="head.jsp"/>
<body>
<div class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style='font-family: "Plus Jakarta Sans", "Noto Sans", sans-serif;'>
    <div class="layout-container flex flex-col min-h-0">
        <jsp:include page="header.jsp"/>
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

        <jsp:include page="infoSection.jsp"/>

        <jsp:include page="footer.jsp"/>
    </div>
</div>

<script>
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
            // 마커 갱신 로직 등, 움직임 종료시
            clearTimeout(idleTimer);
            idleTimer = setTimeout(function () {
                getInfo(map.getZoom());
            }, 200); // 200ms 안에 연속 idle 발생하면 무시
        });
    });

    function getInfo(zoom) {// 10: 시 / 11~13: 구 / 14: 동 / 15~16: 클러스터링 / 17~: 마커
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

</script>
</body>
</html>
