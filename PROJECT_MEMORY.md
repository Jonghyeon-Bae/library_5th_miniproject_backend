# Project Memory - Bookapp Backend (Library 5th Miniproject)

이 문서는 프로젝트의 진행 상황, 아키텍처 설정, 그리고 대화 흐름을 보존하여 개발 프로세스를 이어갈 수 있도록 돕는 "기억 자료"입니다.

---

## 📌 1. 프로젝트 개요 & 아키텍처
* **프론트엔드**: Next.js (로컬 포트 `http://localhost:3000` 사용, 실배포 도메인 `https://library.aivle.cloud` 예정)
* **백엔드**: Spring Boot 4.0.6 (Java 17, Gradle)
* **데이터베이스**: MySQL (Aiven Cloud DB 서비스 사용, DBeaver 툴을 통해 데이터 관리 및 확인 중)
* **보안 및 인증**: JWT (JSON Web Token) 및 Spring Security 기반 인증 시스템 구축 완료

---

## ⚙️ 2. 설정 현황
* **CORS 설정 (`WebConfig.java`)**: 
  - `http://localhost:3000` 및 `https://library.aivle.cloud` 허용
  - `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS` 메서드 허용
  - `allowCredentials(true)` 및 모든 헤더 허용
* **데이터베이스 설정 (`application.yaml`)**:
  - 개발용 H2 설정(주석 처리됨)과 실배포용/Aiven MySQL 설정(환경변수 `${MYSQL_...}` 주입 방식)이 공존함

---

## 🗂️ 3. 엔티티 구조 및 개발 분담
현재 프로젝트에는 아래 4개의 핵심 엔티티가 설계되어 있습니다.

| 엔티티 명 | 역할 | 개발 담당 상황 |
|---|---|---|
| `Users` | 회원 정보 관리 (이메일, 패스워드, 프로필 등) | 보안/인증 연동 완료 |
| `Books` | 도서 정보 관리 (제목, 저자, 카테고리 등) | 타 작업자 진행 중 (`BookController`, `BookService`, `BookRepository` 완료) |
| `Likes` | 도서 좋아요 목록 (User와 Book의 M:N 매핑 관계) | 구현 완료 (`LikeService`, `LikeController` 구현) |
| `SearchHistory` | 사용자별 검색 키워드 및 검색 시간 이력 관리 | 엔티티 및 Repository 존재, Service/Controller 미구현 |

---

## 🚀 4. 내가 독립적으로 구현할 만한 추천 기능 (백엔드)
타 작업자가 개발 중인 도서(`Book`) 코어 기능을 제외하고, JWT 인증 및 유저 컨텍스트와 깊게 연관된 아래 기능들을 독립적으로 설계 및 개발할 수 있습니다.

### 1) Likes (도서 좋아요) 기능 [완료]
* **역할**: 사용자가 도서를 찜(좋아요)하거나 취소하고, 자신이 좋아요 누른 도서 목록을 확인하는 기능
* **필요 컴포넌트**:
  - `LikeService`: 좋아요 추가/취소 토글, 좋아요 여부 확인, 좋아요 누른 책 리스트 조회
  - `LikeController`: `/api/books/{bookId}/like` (POST/DELETE), `/api/users/me/likes` (GET)
* **독립성**: 인증 객체(`UserDetails` 혹은 이메일)로부터 `Users` 엔티티를 찾고 `Books`와 매핑하여 저장하므로 독립적인 기능 구현 가능

### 2) SearchHistory (검색 기록) 기능
* **역할**: 도서 검색 시 검색어 이력을 데이터베이스에 저장하고, 사용자의 최근 검색어 5개 조회 및 개별/전체 삭제 기능 제공
* **필요 컴포넌트**:
  - `SearchHistoryService`: 검색 시 이력 추가, 최근 5개 조회 (`SearchHistoryRepository`에 내장된 `findRecentTop5ByUserId` 활용), 삭제 로직
  - `SearchHistoryController`: `/api/users/me/search-histories` (GET - 최근 5개 조회, DELETE - 특정 기록 삭제/전체 삭제)
* **독립성**: 검색 키워드 저장 시 로그인된 사용자 정보를 기반으로 처리해야 하므로 인증 시스템과 유기적으로 작동함

### 3) User Profile (마이페이지/회원 정보 조회 및 수정) 기능
* **역할**: 마이페이지에서 로그인된 사용자 자신의 정보를 확인하고 이름, 프로필 사진(아바타), 이메일 공개 여부를 수정하는 기능
* **필요 컴포넌트**:
  - `UserController` 신규 생성
  - `/api/users/me` (GET): 현재 로그인한 유저 정보 반환
  - `/api/users/me` (PATCH/PUT): `UsersService`의 `updateProfile` 호출하여 프로필 수정
  - `/api/users/me/avatar` (PATCH): 아바타 사진 변경
* **독립성**: 기존 `UsersService`에 비즈니스 로직(프로필 수정 등)이 이미 구현되어 있어 Controller 및 인증 연동만 완성하면 됨

### 4) 배포 환경 최적화 (Profiles 분리)
* **역할**: 로컬 개발(H2 또는 로컬 MySQL)과 배포(Aiven MySQL) 환경의 설정을 빌드 시점이나 기동 시점에 다르게 적용하기 위한 프로파일 분리
* **작업**:
  - `application-local.yaml` 및 `application-prod.yaml` 분리 설정
  - Docker 배포용 `Dockerfile` 작성

---

## 🔄 5. 대화 흐름 기록 보존용 히스토리
*(새로운 요구사항이나 협업 시 결정된 사항들을 이곳에 지속적으로 추가/업데이트합니다)*
* **2026-06-10**: Next.js ↔ Spring Boot 간의 JWT 인증 설정 진행 중. 협업 편의를 위해 개발 환경에서 인증 기능을 잠시 끄는 ON/OFF 스위치(Toggle) 기능 및 독립적인 기능 개발을 검토 중. 도서 좋아요(Likes) 기능 구현 완료 (좋아요 등록/취소/상태 확인/목록 조회 API). Postman 테스트 중 발생한 500/400 오류 해결을 위해 Controller 내 UserDetails Null 처리 추가 및 GlobalExceptionHandler에 RuntimeException 핸들러 등록 완료.
