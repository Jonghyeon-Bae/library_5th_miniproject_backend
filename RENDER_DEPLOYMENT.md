# Render 배포 시 환경 변수 (.env) 설정 가이드

본 프로젝트를 Render에 지속적 배포(CD)할 때, 데이터베이스 정보 및 보안 토큰 키(`.env` 파일)를 안전하고 올바르게 관리 및 등록하는 방법을 설명합니다.

---

## ⚠️ 중요: `.env` 파일은 절대 Git에 포함하지 마세요!
`.env` 파일에는 데이터베이스 패스워드와 같은 중요한 민감 정보가 포함되어 있습니다. 
* `.gitignore` 파일에 `.env`가 등록되어 있는지 반드시 확인하세요.
* Git 저장소에 소스 코드를 Push하여 Render에 배포하되, `.env` 파일 자체는 업로드되지 않아야 합니다.

---

## 🚀 Render에서 설정하는 2가지 방법

Render 대시보드에 로그인한 후, 배포할 **Web Service**의 상세 페이지로 이동하여 설정합니다.

### 방법 1: Render의 "Secret Files" 기능 사용 (가장 추천)
현재 프로젝트는 `.env` 파일을 로컬 파일 시스템에서 직접 읽어오도록 설정되어 있습니다 (`spring.config.import`). 이 구성을 그대로 활용하는 가장 간단하고 깔끔한 방법입니다.

1. 웹 서비스 설정 페이지 왼쪽 메뉴에서 **Environment** 탭을 클릭합니다.
2. **Secret Files** 영역으로 스크롤한 뒤 **Add Secret File** 버튼을 클릭합니다.
3. **Filename**에 `.env`를 입력합니다.
4. **Contents** 영역에 로컬 컴퓨터에 있는 `.env` 파일 내용을 그대로 전체 복사-붙여넣기 합니다.
   ```properties
   MYSQL_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
   MYSQL_URL=jdbc:mysql://miniproject-aivle-5th-db-miniproject5th.l.aivencloud.com:27594/defaultdb?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=Asia/Seoul
   MYSQL_USERNAME=avnadmin
   MYSQL_PASSWORD=아아벤_실제_비밀번호
   ```
5. **Save Changes**를 클릭합니다.
> **작동 원리**: Render가 서버 인프라를 실행할 때 프로젝트 루트 디렉토리에 `.env` 파일을 자동으로 생성해 주며, 스프링 부트가 이 파일을 로컬처럼 읽어 들입니다.

---

### 방법 2: 개별 "Environment Variables"로 설정
파일이 아니라 운영체제(OS) 환경 변수로 등록하여 스프링 부트에 바인딩하는 표준 클라우드 방식입니다.

1. 웹 서비스 설정 페이지의 **Environment** 탭으로 이동합니다.
2. **Environment Variables** 영역에서 **Add Environment Variable** 버튼을 클릭합니다.
3. 로컬 `.env` 파일에 기재된 키와 값을 하나씩 추가합니다.
   * `MYSQL_DRIVER_CLASS_NAME` = `com.mysql.cj.jdbc.Driver`
   * `MYSQL_URL` = `jdbc:mysql://miniproject-aivle-5th-db-miniproject5th.l.aivencloud.com:27594/defaultdb...`
   * `MYSQL_USERNAME` = `avnadmin`
   * `MYSQL_PASSWORD` = `아아벤_실제_비밀번호`
4. **Save Changes**를 클릭합니다.
> **작동 원리**: 스프링 부트의 `application.yaml` 내 `${MYSQL_URL}` 등의 플레이스홀더가 OS 레벨 환경 변수를 탐색하여 자동으로 주입받습니다. 이 방식 또한 물리적인 `.env` 파일 없이 안전하게 연결을 수행합니다.

---

## 💡 로컬 개발 환경 팁
동료들과 협업할 때 변수 구성을 알리기 위해, 실제 비밀번호 정보만 지운 공백의 템플릿 파일인 `.env.example` 파일을 레포지토리에 추가해 두는 것이 권장됩니다.
