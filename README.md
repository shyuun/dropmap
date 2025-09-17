# 📦 Dropmap – 의류수거함 위치 기반 서비스

## 개요
서울시 전역의 **의류수거함(헌옷수거함) 위치**를 직관적으로 확인할 수 있는 위치 기반 서비스입니다.  
기존에 각 지자체 홈페이지에 흩어져 있던 엑셀/텍스트 데이터를 모아, **네이버 지도 기반 시각화**로 누구나 쉽게 의류 기부 및 재활용을 실천할 수 있도록 돕습니다.

---

## 주요 기능
- 🗺️ **의류수거함 지도 표시**  
  - 네이버 지도 기반 마커/클러스터링  
  - 시·구·행정동별 수거함 개수 확인  

- 📊 **데이터 수집 및 정제**  
  - 서울시 25개 자치구 데이터 수집  
    - Open API (5개 구)  
    - 공공데이터포털 CSV (15개 구)  
    - 비정제 데이터 수집 (4개 구)  
    - 수거함 미존재 (1개 구)  

- 🔄 **자동 업데이트**  
  - 배치 프로세스로 월 1회 최신 데이터 갱신  
  - CSV 데이터는 Swagger 최신 ID 자동 추출 후 반영  

- 🔍 **검색 기능**  
  - 네이버 맵 검색 부재 → 다음 주소 검색 API로 보완  

---

## 아키텍처

```mermaid
flowchart LR
    subgraph DataSources[데이터 소스]
        A[공공데이터 OpenAPI]
        B[공공데이터 CSV]
        C[비정제 데이터 (Excel/Web)]
    end

    subgraph Processing[데이터 처리<br/>(Spring Boot + Batch + JPA)]
        D[정제 · 변환]
    end

    subgraph Database[DB]
        E[(MySQL)]
    end

    subgraph Visualization[지도 시각화<br/>(JSP + JS + Naver Maps API + Vworld)]
        F[지도 렌더링]
    end

    subgraph Infra[Infra<br/>(AWS EC2 + Jenkins + Nginx)]
        G[배포 & 운영]
    end

    A --> D
    B --> D
    C --> D
    D --> E
    E --> F
    G -.-> D
    G -.-> F
