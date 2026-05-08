# Spotter Backend

Spring Boot 기반 Spotter 서비스의 백엔드 API 서버입니다.

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Security** (Stateless / JWT 예정)
- **Spring Data JPA + Hibernate**
- **PostgreSQL 16**
- **Flyway** (DB 마이그레이션)
- **Lombok**
- **Gradle**

---

## Getting Started

### 사전 요구사항

- Java 21
- Docker & Docker Compose

### 실행

```bash
# 1. PostgreSQL 컨테이너 시작
docker compose up -d

# 2. 애플리케이션 실행
./gradlew bootRun
```

서버는 기본적으로 `http://localhost:8080` 에서 실행됩니다.

헬스체크: `GET /api/health`

### 환경 변수

| 변수명 | 기본값 | 설명 |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/spotter` | DB 접속 URL |
| `SPRING_DATASOURCE_USERNAME` | `spotter` | DB 사용자명 |
| `SPRING_DATASOURCE_PASSWORD` | `spotter` | DB 비밀번호 |
| `SERVER_PORT` | `8080` | 서버 포트 |

---

## Project Structure

```
src/main/java/com/spotter/backend/
├── auth/                        # Spring Security 설정
│   └── SecurityConfig.java
├── category/                    # 장소 카테고리
│   ├── dto/
│   ├── entity/
│   └── repository/
├── common/                      # 공통 유틸리티
│   ├── converter/               # JPA AttributeConverter (enum ↔ DB)
│   ├── enums/                   # 공용 Enum 타입
│   └── HealthController.java
├── friendship/                  # 친구 관계
│   ├── dto/
│   ├── entity/
│   └── repository/
├── location/                    # 장소 정보
│   ├── dto/
│   ├── entity/
│   └── repository/
├── meetup/                      # 모임
│   ├── dto/
│   ├── entity/                  # Meetups, MeetupInvitations, MeetupParticipants
│   └── repository/
├── scrap/                       # 장소 스크랩
│   ├── dto/
│   ├── entity/
│   └── repository/
├── stageddata/                  # 스크랩 전 임시 저장 데이터
│   ├── dto/
│   ├── entity/
│   └── repository/
├── user/                        # 사용자
│   ├── dto/
│   ├── entity/
│   └── repository/
└── BackendApplication.java

src/main/resources/
├── db/migration/
│   └── V1__init.sql             # Flyway 초기 스키마
└── application.yml
```

패키지는 **도메인 중심**으로 구성되며, 각 도메인은 `entity`, `repository`, `dto` 레이어를 가집니다.

---

## Branch Convention

```
main         # 프로덕션 배포 브랜치 (직접 push 금지)
develop      # 통합 개발 브랜치
feature/*    # 기능 개발
fix/*        # 버그 수정
refactor/*   # 리팩토링
chore/*      # 빌드, 설정, 의존성 등 코드 변경 없는 작업
docs/*       # 문서 작업
```

### 브랜치 네이밍 규칙

```
<type>/<issue-number>-<short-description>

예시:
feature/12-add-user-login
fix/34-fix-friendship-status
refactor/56-extract-meetup-service
```

---

## Commit Convention

[Conventional Commits](https://www.conventionalcommits.org/) 스펙을 따릅니다.

### 형식

```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

### Type

| Type | 설명 |
|---|---|
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 기능 변경 없는 코드 리팩토링 |
| `test` | 테스트 코드 추가/수정 |
| `docs` | 문서 작성/수정 |
| `chore` | 빌드, 설정, 의존성 변경 등 |
| `style` | 코드 포맷팅, 세미콜론 누락 등 (로직 변경 없음) |
| `perf` | 성능 개선 |
| `ci` | CI/CD 설정 변경 |

### Scope (선택)

도메인 단위로 작성합니다.

```
user, location, meetup, friendship, scrap, stageddata, category, auth, common
```

### 예시

```
feat(user): 회원가입 API 구현

fix(meetup): 모임 상태 변환 오류 수정

refactor(common): CodeEnumConverter 제네릭 타입 개선

chore: Flyway 의존성 추가

docs: README 브랜치 컨벤션 작성
```

### 규칙

- `subject`는 명령형으로 작성 (동사 원형 또는 한국어 동사)
- 첫 글자 대문자 X, 마침표 X
- 한 커밋에 하나의 목적만 담기
- 이슈와 연결 시 footer에 `Closes #<issue-number>` 추가

---

## Database Migration

Flyway를 사용합니다. 마이그레이션 파일은 `src/main/resources/db/migration/` 에 위치합니다.

### 네이밍 규칙

```
V<version>__<description>.sql

예시:
V1__init.sql
V2__add_user_profile_image.sql
V3__add_index_location_category.sql
```

- 버전은 순차적으로 증가
- 한 번 적용된 파일은 절대 수정하지 않음
- 스키마 변경 시 새 파일로 추가
