
#프로젝트명 sample-api

#프로젝트 정보
Gradle(7.6), Kotlin(1.6), Spring Boot(2.7), Jar Packaging, JDK 11, MySQL(5.7)

#실행 환경
Intel MacBook, Docker Desktop.

#실행 순서
```
# 로컬 MySQL DB 구동
$cd local-db
$docker-compose up

# API 서버 구동
$./gradlew bootRun
```

#테스트
```
$./gradlew test
```