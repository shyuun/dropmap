<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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