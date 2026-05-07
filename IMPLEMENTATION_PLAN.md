# Spotter Backend Implementation Plan

이 문서는 Spotter 백엔드 구현 진행도를 추적하기 위한 체크리스트다.

## 1. Project Foundation

- [x] Spring Boot 프로젝트 생성
- [x] Gradle Wrapper 추가
- [x] Java 21 toolchain 설정
- [x] PostgreSQL, JPA, Security, Validation, Flyway, Lombok 의존성 추가
- [x] PostgreSQL 기준 `application.yml` 작성
- [x] 로컬 PostgreSQL용 `docker-compose.yml` 추가
- [x] 기본 패키지 구조 생성
- [x] `GET /api/health` 엔드포인트 추가
- [x] JDK 21 설치 후 `./gradlew test` 실행 확인
- [ ] `docker compose up -d`로 PostgreSQL 실행 확인
- [ ] `./gradlew bootRun`으로 서버 실행 확인

## 2. Database Schema

- [ ] `V1__init_schema.sql` Flyway 마이그레이션 생성
- [ ] `users` 테이블 생성
- [ ] `categories` 테이블 생성
- [ ] `locations` 테이블 생성
- [ ] `scraps` 테이블 생성
- [ ] `friendships` 테이블 생성
- [ ] `meetups` 테이블 생성
- [ ] `meetup_participants` 테이블 생성
- [ ] `meetup_invitations` 테이블 생성
- [ ] `shared_posts` 테이블 생성
- [ ] `shared_post_images` 테이블 생성
- [ ] `place_candidates` 테이블 생성
- [ ] 주요 unique constraint와 index 추가
- [ ] category seed 데이터 추가

## 3. Domain Model

- [ ] User entity/repository 구현
- [ ] Category entity/repository 구현
- [ ] Location entity/repository 구현
- [ ] Scrap entity/repository 구현
- [ ] Friendship entity/repository 구현
- [ ] Meetup entity/repository 구현
- [ ] MeetupParticipant entity/repository 구현
- [ ] MeetupInvitation entity/repository 구현
- [ ] SharedPost entity/repository 구현
- [ ] SharedPostImage entity/repository 구현
- [ ] PlaceCandidate entity/repository 구현
- [ ] 공통 enum 정의
- [ ] 공통 예외 응답 형식 정의

## 4. Authentication

- [ ] 회원가입 API 구현: `POST /api/users/signup`
- [ ] 비밀번호 BCrypt hash 저장
- [ ] 로그인 API 구현: `POST /api/users/login`
- [ ] JWT access token 발급 구현
- [ ] JWT 인증 필터 구현
- [ ] 현재 사용자 조회 API 구현: `GET /api/users/me`
- [ ] 인증 실패/권한 실패 응답 정리
- [ ] Refresh Token 확장 포인트 문서화

## 5. Shared Post Place Saving Flow

- [ ] 공유 게시글 업로드 API 구현: `POST /api/shared-posts`
- [ ] multipart caption image 업로드 처리
- [ ] source URL/source type 저장
- [ ] 앱에서 전달한 OCR text 저장
- [ ] OCR text 기반 장소 후보 추출 로직 구현
- [ ] 장소 후보를 `place_candidates`에 저장
- [ ] 검증 대기 목록 API 구현: `GET /api/shared-posts/pending`
- [ ] 공유 게시글 상세 API 구현: `GET /api/shared-posts/{sharedPostId}`
- [ ] 공유 게시글 삭제 API 구현: `DELETE /api/shared-posts/{sharedPostId}`
- [ ] 최종 장소 확정 API 구현: `POST /api/shared-posts/{sharedPostId}/confirm`
- [ ] 확정 시 `locations` 생성 또는 재사용
- [ ] 확정 시 `scraps` 생성
- [ ] 중복 scrap 방지 처리
- [ ] `locations.total_scrap_count` 증가 처리

## 6. External Place Search

- [ ] Naver Maps API 설정값 추가
- [ ] Naver place search client 구현
- [ ] 장소 검색 API 구현: `GET /api/places/search?query={query}`
- [ ] Naver category를 내부 category로 매핑
- [ ] 외부 API 실패 시 에러 응답 정리

## 7. Scraps and Locations

- [ ] 내 scrap 목록 API 구현: `GET /api/scraps`
- [ ] category filter 구현: `GET /api/scraps?categoryId={categoryId}`
- [ ] scrap 수정 API 구현: `PATCH /api/scraps/{scrapId}`
- [ ] scrap 삭제 API 구현: `DELETE /api/scraps/{scrapId}`
- [ ] 지도 영역 장소 조회 API 구현: `GET /api/locations/map`
- [ ] bounding box 기반 위치 검색 구현

## 8. Friendships

- [ ] 친구 요청 API 구현: `POST /api/friends/requests`
- [ ] 받은 친구 요청 목록 API 구현: `GET /api/friends/requests/incoming`
- [ ] 친구 요청 수락 API 구현: `POST /api/friends/requests/{requestId}/accept`
- [ ] 친구 요청 거절 API 구현: `POST /api/friends/requests/{requestId}/reject`
- [ ] 친구 목록 API 구현: `GET /api/friends`
- [ ] 공통 저장 장소 API 구현: `GET /api/friends/{friendId}/common-locations`
- [ ] 자기 자신 친구 요청 방지
- [ ] 중복 친구 요청 방지

## 9. Meetups

- [ ] 모임 생성 API 구현: `POST /api/meetups`
- [ ] 전체 모임 목록 API 구현: `GET /api/meetups`
- [ ] 모임 상세 API 구현: `GET /api/meetups/{meetupId}`
- [ ] 모임 참가 API 구현: `POST /api/meetups/{meetupId}/join`
- [ ] 모임 취소 API 구현: `POST /api/meetups/{meetupId}/cancel`
- [ ] 모임 나가기 API 구현: `POST /api/meetups/{meetupId}/leave`
- [ ] host 자동 participant 등록
- [ ] host만 cancel 가능하도록 검증
- [ ] non-host participant만 leave 가능하도록 검증
- [ ] visibility별 조회/참가 권한 검증

## 10. Meetup Invitations

- [ ] private 모임 초대 API 구현: `POST /api/meetups/{meetupId}/invitations`
- [ ] 받은 초대 목록 API 구현: `GET /api/meetup-invitations/incoming`
- [ ] 초대 수락 API 구현: `POST /api/meetup-invitations/{invitationId}/accept`
- [ ] 초대 거절 API 구현: `POST /api/meetup-invitations/{invitationId}/reject`
- [ ] 초대 수락 시 participant 생성
- [ ] 중복 초대 방지

## 11. Recommendations

- [ ] 카테고리 선호도 집계 쿼리 구현
- [ ] 미저장 장소 추천 API 구현: `GET /api/recommendations/locations`
- [ ] 친구 기반 장소 추천 API 구현: `GET /api/recommendations/friends`
- [ ] 이미 저장한 장소 제외 처리
- [ ] `total_scrap_count` 기반 정렬 처리

## 12. Testing and Demo Readiness

- [ ] repository/service 단위 테스트 작성
- [ ] auth API 통합 테스트 작성
- [ ] shared post confirm flow 통합 테스트 작성
- [ ] meetup join/cancel/leave 테스트 작성
- [ ] friendship flow 테스트 작성
- [ ] recommendation query 테스트 작성
- [ ] 데모용 seed 데이터 작성
- [ ] API 요청 예시 문서화
- [ ] README에 실행 방법 정리
