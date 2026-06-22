# eyes

한국 안과(ophthalmology) 병원을 지역/우선순위/이름/의사명으로 검색하고, 지도에서 필터링하며 메모(note)를 관리할 수 있는 웹 애플리케이션입니다.

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.5.15, Spring Data JPA
- **Database**: H2(개발용, 인메모리), PostgreSQL(운영용, Supabase)
- **Frontend**: HTML/CSS/Vanilla JavaScript, D3.js v7 (지도 시각화), TopoJSON (한국 행정구역 데이터)
- **Build**: Gradle (Wrapper 포함)

## 주요 기능

- D3.js choropleth 지도를 통한 지역/구역 필터링
- 우선순위(JMC, 2~5) 필터링
- 병원명/의사명 검색
- 메모(note) 인라인 편집 및 필터링
- 페이지네이션 (10개씩)

## 프로젝트 구조

```
src/main/java/hangaram/eyes/
├── EyesApplication.java         # Spring Boot 진입점
└── hospital/
    ├── Hospital.java            # JPA Entity (hospitals 테이블)
    ├── HospitalController.java  # REST API (/api/hospitals)
    └── HospitalRepository.java  # JpaRepository

src/main/resources/
├── application.yaml             # Spring/DB 설정
└── static/                      # 프론트엔드 (index.html, css, korea-topo.json)
```

## 실행 방법

```bash
# macOS/Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

기본 포트는 `8080`이며 (`PORT` 환경변수로 변경 가능), 실행 후 `http://localhost:8080` 에서 접속합니다.

## API

| Method | Endpoint              | 설명                          |
|--------|-----------------------|-------------------------------|
| GET    | `/api/hospitals`      | 전체 병원 목록 조회 (no 오름차순) |
| PATCH  | `/api/hospitals/{id}` | 병원 메모(note) 수정          |

`PATCH` 요청 본문 예시:

```json
{ "note": "예약 필요, 평일만 진료" }
```

## 데이터베이스 스키마 (`hospitals` 테이블)

| 컬럼      | 타입    | 설명           |
|-----------|---------|----------------|
| id        | Long    | PK (auto)      |
| no        | Integer | 표시 순번      |
| region    | String  | 시/도          |
| district  | String  | 시/군/구       |
| name      | String  | 병원명         |
| doctor    | String  | 의사명         |
| address   | String  | 주소           |
| phone     | String  | 전화번호       |
| priority  | String  | 우선순위       |
| note      | String  | 메모           |

`spring.jpa.hibernate.ddl-auto=none` 이므로 스키마는 DB에 미리 생성되어 있어야 합니다.

## ⚠️ 보안 주의사항

`src/main/resources/application.yaml`에 PostgreSQL(Supabase) 접속 계정/비밀번호가 **평문으로 하드코딩**되어 있습니다. 운영 환경에서는 환경변수나 시크릿 매니저로 옮기고, 이미 노출된 자격증명은 교체(rotate)하는 것을 권장합니다.

## 테스트

```bash
./gradlew test
```

현재는 Spring 컨텍스트 로드 여부만 확인하는 테스트(`EyesApplicationTests`)만 존재합니다.
