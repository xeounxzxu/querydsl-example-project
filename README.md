# querydsl-example-project

Spring Boot + Kotlin 웹 서버 기본 프로젝트입니다.

Spring Boot 4.2.0, Kotlin 2.4.21, JDK 25, Gradle Kotlin DSL 기반 프로젝트입니다.
공식 Spring Initializr로 생성했으며 Gradle은 Wrapper로 실행합니다.

## 실행

JDK 25를 설치하고 `JAVA_HOME`을 설정합니다. IntelliJ에서는 Gradle 프로젝트로 열고 Gradle JVM을 JDK 25로 선택합니다.

```sh
./gradlew bootRun
```

기본 포트는 8080입니다. `SERVER_PORT=9090 ./gradlew bootRun`으로 변경할 수 있습니다.

```sh
curl 'http://localhost:8080/api/v1/greetings'
# {"message":"Hello, World!"}
curl 'http://localhost:8080/api/v1/greetings?name=Kotlin'
# {"message":"Hello, Kotlin!"}
curl 'http://localhost:8080/actuator/health'
# {"status":"UP"}
```

이름은 최대 100자이며 공백만 입력하면 HTTP 400을 반환합니다.

## 구조

```text
src/main/kotlin/com/example/querydslsample/
├── QuerydslSampleApplication.kt
└── greeting/
    ├── controller/GreetingController.kt
    ├── service/GreetingService.kt
    └── dto/GreetingResponse.kt
src/main/resources/application.yml
src/test/kotlin/com/example/querydslsample/QuerydslSampleApplicationTests.kt
```

기능별 패키지 아래 Controller(HTTP 요청), Service(업무 로직), DTO(응답)를 나눕니다.
새 기능도 같은 형태로 추가하면 됩니다. 데이터베이스, JPA, Querydsl은 아직 구성하지 않았습니다.

## 테스트와 빌드

```sh
./gradlew test
./gradlew build
java -jar build/libs/querydsl-sample-0.0.1-SNAPSHOT.jar
```

테스트는 애플리케이션 구동, 기본/사용자 지정 응답, 입력 검증, 상태 확인 API를 검증합니다.
첫 실행에는 Gradle과 Maven 의존성을 내려받을 인터넷 연결이 필요합니다.
