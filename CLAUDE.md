# CLAUDE.md

Spring Boot 3.5.15 + Java 17 단일 모듈 웹앱. 한국 안과 병원 검색/필터링 서비스 (`hangaram.eyes`).

## 커맨드

```bash
./gradlew build      # 빌드
./gradlew bootRun     # 로컬 실행 (포트 8080, PORT 환경변수로 변경 가능)
./gradlew test        # 테스트 실행 (JUnit 5)
```

Windows에서는 `gradlew.bat` 사용.

## 아키텍처

- 단일 모듈, 루트 패키지 `hangaram.eyes`. 진입점은 `EyesApplication`.
- 도메인별로 `{domain}/Entity.java`, `{domain}/Controller.java`, `{domain}/Repository.java` 3파일 패턴 사용 (`hospital/Hospital.java`, `HospitalController.java`, `HospitalRepository.java` 참고). 새 도메인 추가 시 동일 패턴을 따른다.
- 프론트엔드는 별도 빌드 파이프라인 없이 `src/main/resources/static/`에서 정적 파일로 직접 서빙된다 (Vanilla JS + D3.js).
- `spring.jpa.hibernate.ddl-auto=none` — JPA가 스키마를 생성하지 않으므로, 엔티티 필드 변경 시 DB 스키마를 직접 맞춰야 한다.

## 코드 컨벤션

- Entity는 Lombok `@Getter @Setter @NoArgsConstructor` 사용 (빌더/생성자 추가 패턴 없음).
- Controller의 요청 바디는 `record`로 정의 (예: `HospitalController.NoteUpdateRequest`).
- Repository는 메서드명 기반 쿼리(derived query)를 우선 사용 (예: `findAllByOrderByNoAsc`). `@Query` 어노테이션 사용 사례 없음.

## 주의사항

- `src/main/resources/application.yaml`에 PostgreSQL(Supabase) 접속 정보(계정/비밀번호)가 평문으로 들어 있다. 이 파일을 다루거나 새 설정을 추가할 때 자격증명을 그대로 노출/복제하지 말 것. 가능하면 환경변수로 분리를 제안한다.
- 테스트는 `EyesApplicationTests` 하나(컨텍스트 로드 확인)뿐이며 Controller/Repository 테스트가 없다. 새 기능 추가 시 테스트가 비어있다는 점을 감안한다.
- CI 파이프라인, Dockerfile 없음 — 배포/검증은 수동.
