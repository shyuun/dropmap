<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="head.jsp"/>
<body class="bg-gray-100 overflow-y-scroll">
<div class="relative flex size-full min-h-screen flex-col bg-white group/design-root overflow-x-hidden" style='font-family: "Plus Jakarta Sans", "Noto Sans", sans-serif;'>
    <div class="layout-container flex flex-col">
        <jsp:include page="header.jsp"/>

        <div class="flex flex-1 overflow-auto">
            <aside id="leftAd" class="hidden md:block md:w-48 lg:w-56 xl:w-64 bg-gray-50 border-r border-gray-200">
                <div class="p-2 text-xs text-center min-h-[250px]">
                    <ins class="adsbygoogle sideads"
                         style="display:block"
                         data-ad-client="ca-pub-4694284708905804"
                         data-ad-slot="3352268982"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                </div>
            </aside>

            <main class="flex-1 overflow-auto bg-gray-100">
                <div class="max-w-4xl mx-auto px-4 py-8 md:py-16">
                    <article class="bg-white p-6 md:p-12 rounded-lg shadow-md">
                        <h1 class="text-3xl md:text-4xl font-bold mb-8">📖</h1>
                        <c:if test="${gbn eq 'intro'}">
                            <section id="intro" class="mb-10">
                                <h2 class="text-2xl font-bold mb-4">서비스 소개</h2>
                                <p class="text-gray-700 leading-relaxed mb-4">
                                    '드롭맵'은 옷을 기부하고 재활용할 수 있는 의류수거함의 정확한 위치를 제공하여, 사용자가 편리하게 의류를 배출할 수 있도록 돕는 지도 기반 서비스입니다.
                                </p>
                                <p class="text-gray-700 leading-relaxed">
                                    서울시 전역에 분포된 의류수거함 정보를 수집 및 정제하여 지도 위에 정확한 위치와 정보를 표시합니다. 복잡한 지자체 정보를 통합하여 사용자의 의류 재활용 참여를 촉진하는 것을 목표로 합니다.
                                </p>
                            </section>
                            <hr class="my-10 border-gray-300">
                            <section id="guide" class="mb-10">
                                <h2 class="text-2xl font-bold mb-4">서비스 안내</h2>
                                <h3 class="text-xl font-semibold mb-6">주요 기능</h3>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                                    <div class="bg-gray-50 p-6 rounded-lg shadow-sm border border-gray-200">
                                        <div class="flex items-center text-lg font-bold mb-2">
                                            <span class="text-3xl mr-2">🗺️</span>
                                            <span>지도 기반 위치 확인</span>
                                        </div>
                                        <p class="text-gray-700 leading-relaxed">네이버 지도를 통해 서울시 내 모든 의류수거함의 위치를 확인할 수 있습니다.</p>
                                    </div>
                                    <div class="bg-gray-50 p-6 rounded-lg shadow-sm border border-gray-200">
                                        <div class="flex items-center text-lg font-bold mb-2">
                                            <span class="text-3xl mr-2">📊</span>
                                            <span>지역별 통계</span>
                                        </div>
                                        <p class="text-gray-700 leading-relaxed">지도를 확대/축소하여 시, 구, 동 단위의 의류수거함 개수를 파악할 수 있습니다.</p>
                                    </div>
                                    <div class="bg-gray-50 p-6 rounded-lg shadow-sm border border-gray-200">
                                        <div class="flex items-center text-lg font-bold mb-2">
                                            <span class="text-3xl mr-2">🔎</span>
                                            <span>주소 검색</span>
                                        </div>
                                        <p class="text-gray-700 leading-relaxed">특정 주소를 입력하여 해당 지역의 의류수거함 위치를 검색할 수 있습니다.</p>
                                    </div>
                                </div>
                                <h3 class="text-xl font-semibold mt-10 mb-2">데이터 정보</h3>
                                <div class="bg-gray-50 p-6 rounded-lg shadow-sm border border-gray-200">
                                    <p class="text-gray-700 leading-relaxed mb-4">
                                        본 서비스의 모든 정보는 서울시 24개 자치구의 공공데이터를 기반으로 합니다. 데이터는 월 1회 정기적으로 업데이트되어 최신 정보를 유지합니다.
                                    </p>
                                    <ul class="list-disc list-inside text-gray-700 leading-relaxed">
                                        <li><strong>데이터 출처</strong>: 서울시 24개 자치구 공공데이터</li>
                                        <li><strong>업데이트 주기</strong>: 월 1회 정기 업데이트</li>
                                    </ul>
                                </div>
                            </section>
                            <hr class="my-10 border-gray-300">
                        </c:if>

                        <c:if test="${gbn eq 'guide'}">
                            <section id="guide" class="mb-10">
                                <h2 class="text-2xl font-bold mb-4">🧺 의류수거함 배출 기준 안내</h2>
                                <h3 class="text-xl font-semibold mt-6 mb-2">✅ 수거 가능한 품목</h3>
                                <div class="overflow-x-auto mb-6">
                                    <table class="min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                                        <thead>
                                        <tr class="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
                                            <th class="py-3 px-6 text-left">분류</th>
                                            <th class="py-3 px-6 text-left">품목</th>
                                        </tr>
                                        </thead>
                                        <tbody class="text-gray-600 text-sm font-light">
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6 whitespace-nowrap">👕 의류류</td>
                                            <td class="py-3 px-6">일반 의류 (상의, 하의, 외투 등)</td>
                                        </tr>
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6">👟 패션잡화</td>
                                            <td class="py-3 px-6">신발 (운동화, 구두 등)<br>일반 가방 (백팩, 숄더백 등)</td>
                                        </tr>
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6">🛏 침구/직물류</td>
                                            <td class="py-3 px-6">담요, 누비이불<br>커튼, 카펫<br>베개커버, 이불커버</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <h3 class="text-xl font-semibold mt-6 mb-2">❌ 수거 불가능한 품목</h3>
                                <div class="overflow-x-auto mb-6">
                                    <table class="min-w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                                        <thead>
                                        <tr class="bg-gray-100 text-gray-600 uppercase text-sm leading-normal">
                                            <th class="py-3 px-6 text-left">분류</th>
                                            <th class="py-3 px-6 text-left">품목</th>
                                        </tr>
                                        </thead>
                                        <tbody class="text-gray-600 text-sm font-light">
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6 whitespace-nowrap">🛏 침구류</td>
                                            <td class="py-3 px-6">솜이불, 베개, 방석</td>
                                        </tr>
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6">⚠️ 특수 품목</td>
                                            <td class="py-3 px-6">전기장판, 전기요</td>
                                        </tr>
                                        <tr class="border-b border-gray-200 hover:bg-gray-50">
                                            <td class="py-3 px-6">🧳 바퀴류</td>
                                            <td class="py-3 px-6">바퀴 달린 신발 (롤러스케이트, 휠리스)<br>바퀴 달린 가방 (캐리어 등)</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <p class="mt-6 text-gray-700 leading-relaxed">
                                    <span class="font-bold text-lg">🔔 TIP:</span> 수거 불가 품목은 <em class="font-semibold text-red-600">대형폐기물 신고</em> 또는 <em class="font-semibold text-red-600">재활용센터</em>를 통해 배출해야 해요.
                                    <br>지역마다 기준이 다를 수 있으니, <strong class="text-blue-600">자치구 안내문</strong>도 꼭 확인해 주세요!
                                </p>
                            </section>
                            <hr class="my-10 border-gray-300">
                        </c:if>

                        <c:if test="${gbn eq 'contact'}">
                            <section id="contact">
                                <h2 class="text-2xl font-bold mb-4">문의 및 제안</h2>
                                <p class="text-gray-700 leading-relaxed mb-4">
                                    서비스 이용 중 불편한 점이나 제안할 의견이 있으시면 아래 연락처로 문의해주시기 바랍니다.
                                </p>
                                <ul class="list-disc list-inside text-gray-700 leading-relaxed mb-4">
                                    <li>⚠️ <strong>정보 오류 신고</strong>: 지도에 표시된 의류수거함 위치가 실제와 다르거나, 철거된 경우 등 오류 정보를 발견하시면 알려주시기 바랍니다. 확인 후 즉시 반영하겠습니다.</li>
                                    <li>💡 <strong>개선 의견</strong>: 서비스 기능에 대한 아이디어 또는 개선이 필요한 사항이 있다면 자유롭게 제안해 주십시오.</li>
                                    <li>🤝 <strong>협력 제안</strong>: 데이터 제공, 제휴 등 다양한 협력 가능성에 대해 논의하고 싶으시다면 연락 주시기 바랍니다.</li>
                                </ul>
                                <p class="text-gray-700 font-semibold mt-6">
                                    문의: <a href="mailto:shyuun@naver.com" class="text-blue-600 hover:text-blue-800">shyuun@naver.com</a>
                                </p>
                            </section>
                        </c:if>
                    </article>
                </div>
            </main>

            <aside id="rightAd" class="hidden md:block md:w-48 lg:w-56 xl:w-64 bg-gray-50 border-l border-gray-200">
                <div class="p-2 text-xs text-center min-h-[250px]">
                    <ins class="adsbygoogle sideads"
                         style="display:block"
                         data-ad-client="ca-pub-4694284708905804"
                         data-ad-slot="5128524706"
                         data-ad-format="auto"
                         data-full-width-responsive="true"></ins>
                </div>
            </aside>
        </div>

        <footer id="footerAd" class="block w-full bg-gray-50 border-t border-gray-200">
            <div class="p-2 text-center min-h-[320px]">
                <ins class="adsbygoogle footerads"
                     style="display:block"
                     data-ad-client="ca-pub-4694284708905804"
                     data-ad-slot="3048284131"
                     data-ad-format="auto"
                     data-full-width-responsive="true"></ins>
            </div>
        </footer>

        <div class="w-full bg-slate-50 border-t border-gray-200 py-8">
            <div class="max-w-4xl mx-auto px-4">
                <div class="flex flex-col md:flex-row items-center justify-center md:justify-start gap-8">
                    <div class="flex items-center gap-2 text-[#121416] mb-4 md:mb-0">
                        <div class="size-8">
                            <img src="${pageContext.request.contextPath}/img/favicon/logo_round.png" alt="dropmap 로고" class="w-full h-full"/>
                        </div>
                        <h2 class="text-xl font-bold leading-tight tracking-[-0.015em]">dropmap</h2>
                    </div>

                    <div class="flex flex-col md:flex-row items-center text-gray-500 text-sm space-y-2 md:space-y-0 md:space-x-4">
                        <p class="whitespace-nowrap">
                            &copy; 2025 dropmap. All Rights Reserved.
                        </p>
                        <div class="flex items-center justify-center flex-wrap gap-2 md:gap-3">
                            <a href="/" class="text-gray-600 hover:text-gray-800 whitespace-nowrap">홈</a>
                            <span class="text-gray-400">|</span>
                            <a href="/info/intro" class="text-gray-600 hover:text-gray-800 whitespace-nowrap">서비스 소개</a>
                            <span class="text-gray-400">|</span>
                            <a href="/info/guide" class="text-gray-600 hover:text-gray-800 whitespace-nowrap">배출 가이드</a>
                            <span class="text-gray-400">|</span>
                            <a href="/info/contact" class="text-gray-600 hover:text-gray-800 whitespace-nowrap">문의 및 제안</a>
                            <span class="text-gray-400">|</span>
                            <a href="mailto:shyuun@naver.com" class="text-gray-600 hover:text-gray-800 whitespace-nowrap">연락처</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>