# 1단계: 빌드 스테이지 (Gradle 빌드)
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# gradlew 실행 권한 부여 (Windows에서 git push 시 권한이 깨지는 현상 방지)
RUN chmod +x gradlew

# 의존성 다운로드 캐싱 (소스 코드 변경 시 매번 의존성을 다운로드하지 않도록 방지)
RUN ./gradlew dependencies --no-daemon || return 0

# 소스 코드 복사 및 애플리케이션 빌드 (테스트 제외)
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# 2단계: 실행 스테이지 (경량 JRE 이미지 사용)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌드 스테이지에서 생성된 jar 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Render 기본 포트 설정 (스프링 부트 포트를 Render 포트에 매핑 가능하게 설정)
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
