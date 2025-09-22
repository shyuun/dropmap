<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#f1f2f4] px-10 py-3">
    <a href="javascript:location.href='/'">
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
    $(function() {

        const currentPath = window.location.pathname;
        const contextPath = '${pageContext.request.contextPath}';

        if (currentPath !== contextPath + '/' && currentPath !== contextPath) {
            $("#addressSearchBtn").hide();
        }

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
</script>