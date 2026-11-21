# querydsl-example-project

Spring Boot + Kotlin 웹 서버 기본 프로젝트입니다.

Spring Boot 4.2.0, Kotlin 2.4.21, JDK 25, Gradle Kotlin DSL 기반 프로젝트입니다.
공식 Spring Initializr로 생성했으며 Gradle은 Wrapper로 실행합니다.

## 실행

JDK 25를 설치하고 `JAVA_HOME`을 설정합니다. IntelliJ에서는 Gradle 프로젝트로 열고 Gradle JVM을 JDK 25로 선택합니다.

```sh
docker compose up -d --wait
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
새 기능도 같은 형태로 추가하면 됩니다. 회원 예제는 Spring Data JPA로 저장하고 OpenFeign Querydsl 7.7로 조건 검색합니다.

## 테스트와 빌드

```sh
./gradlew test
./gradlew build
java -jar build/libs/querydsl-sample-0.0.1-SNAPSHOT.jar
```

테스트는 애플리케이션 구동, 기본/사용자 지정 응답, 입력 검증, 상태 확인 API를 검증합니다.
첫 실행에는 Gradle과 Maven 의존성을 내려받을 인터넷 연결이 필요합니다.

## MySQL 및 Querydsl

[OpenFeign Querydsl](https://github.com/OpenFeign/querydsl)의 `io.github.openfeign.querydsl` 의존성을 사용합니다.
Kotlin kapt가 엔티티에서 Q 클래스를 생성합니다. `./gradlew compileKotlin` 후
`build/generated/source/kapt/main`에서 확인할 수 있으며 생성 파일은 Git에서 제외합니다.
JPA 엔티티에는 Kotlin JPA(no-arg)와 all-open 플러그인을 적용했습니다.

```text
config/QuerydslConfig.kt           # JPAQueryFactory 빈
member/entity/Member.kt           # JPA 엔티티
member/repository/MemberRepository.kt       # 저장
member/repository/MemberQueryRepository.kt  # Querydsl 동적 조건 조회
member/service/MemberService.kt   # 트랜잭션 및 DTO 변환
member/controller/MemberController.kt       # 등록/검색 API
```

MySQL 8.4는 `127.0.0.1:3307`로 노출되며 Docker named volume에 데이터를 보존합니다.
기본 DB는 `querydsl_sample`, 계정은 `querydsl`, 비밀번호는 `querydsl_local`입니다.
이 값과 root 비밀번호 `root_local`은 로컬 개발용입니다.

- 앱 설정: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DDL_AUTO`
- Compose 설정: `MYSQL_PORT`(기본 3307), `DB_USERNAME`, `DB_PASSWORD`, `MYSQL_ROOT_PASSWORD`
- 포트를 변경하면 앱의 `DB_URL`도 같은 포트로 설정합니다.
- Compose는 `.env`를 자동으로 읽지만 `bootRun`은 읽지 않으므로 앱 환경변수도 별도로 지정합니다.
- MySQL 계정 초기화 변수는 볼륨이 처음 생성될 때만 적용됩니다.
- `ddl-auto=update`는 로컬 예제용입니다. 운영 환경에는 별도 스키마 마이그레이션을 사용하세요.

```sh
curl -X POST http://localhost:8080/api/v1/members \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice","age":30}'
curl 'http://localhost:8080/api/v1/members?name=ali&minAge=20&limit=20'
```

검색 조건은 선택 사항이며 이름 부분 일치(대소문자 무시), 최소 나이 조건을 AND로 결합합니다.
ID 오름차순으로 반환하며 `limit`은 기본 20, 최대 100입니다.

```sh
docker compose ps
docker compose logs mysql
docker compose down  # 컨테이너 중지 및 제거, 데이터 볼륨은 유지
```

자동 테스트는 H2의 MySQL 호환 모드를 사용하므로 Docker 없이 실행할 수 있습니다.
회원 등록/검색, 복합 조건, 결과 제한, 입력 검증을 테스트합니다.
실제 MySQL 연결은 Docker 실행 후 `bootRun`과 위 API 호출로 확인할 수 있습니다.
