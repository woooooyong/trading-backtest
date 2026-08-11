# Trading Backtest

미국 주식의 일별 종가 데이터를 기반으로 이동평균선 교차 전략을 백테스트하는 Spring Boot REST API입니다.

사용자는 회원가입·로그인 후 자신만의 이동평균선 전략을 생성하고, 종목·기간·초기 자금을 입력해 실제 주가 데이터로 백테스트를 실행할 수 있습니다.

## 주요 기능

* JWT 기반 회원가입·로그인 인증
* 내 이동평균선 전략 생성·조회·수정·삭제
* Alpha Vantage API를 통한 실제 미국 주식 일별 종가 조회
* 단기·장기 이동평균선 교차 기반 매수·매도 시뮬레이션
* 총수익률, 거래 횟수, 승률, MDD 계산
* 백테스트 실행 결과와 매수·매도 거래 내역 저장
* 내 백테스트 실행 이력 및 거래 내역 조회
* Swagger UI로 API 문서 조회 및 직접 테스트

## 기술 스택

| 구분                | 기술                             |
| ----------------- | ------------------------------ |
| Language          | Java 21                        |
| Framework         | Spring Boot                    |
| Database          | MySQL                          |
| Persistence       | Spring Data JPA                |
| Security          | Spring Security, JWT           |
| API Documentation | Swagger UI / springdoc-openapi |
| External API      | Alpha Vantage                  |
| Test              | JUnit 5, Mockito               |
| Build Tool        | Gradle                         |
| Container         | Docker, Docker Compose         |

## 핵심 흐름

```text
로그인
→ 전략 생성
→ 종목·기간·초기 자금 입력
→ Alpha Vantage에서 실제 종가 조회
→ 이동평균선 백테스트 실행
→ 결과와 매수·매도 기록 저장
→ 실행 이력 및 거래 내역 조회
```

## API 예시

| 기능           | Method | URL                               |
| ------------ | ------ | --------------------------------- |
| 회원가입         | POST   | `/auth/signup`                    |
| 로그인          | POST   | `/auth/login`                     |
| 내 전략 목록 조회   | GET    | `/strategies`                     |
| 전략 생성        | POST   | `/strategies`                     |
| 백테스트 실행      | POST   | `/backtests/run`                  |
| 내 백테스트 이력 조회 | GET    | `/backtests`                      |
| 거래 내역 조회     | GET    | `/backtests/{executionId}/trades` |

## 백테스트 실행 예시

```json
{
  "strategyId": 7,
  "symbol": "AAPL",
  "startDate": "2026-05-01",
  "endDate": "2026-08-01",
  "initialCash": 1000000
}
```

`initialCash`는 센트 단위입니다.
예를 들어 `1000000`은 10,000달러를 의미합니다.

## 로컬 실행 방법

### 1. 사전 준비

* Java 21
* MySQL
* Alpha Vantage API Key

### 2. 데이터베이스 생성

```sql
CREATE DATABASE trading_backtest
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 3. 로컬 설정 파일 생성

예시 파일을 복사해 실제 설정 파일을 만든다.

```bash
cp src/main/resources/application-example.properties \
   src/main/resources/application.properties
```

그다음 `application.properties`에서 아래 값을 자신의 환경에 맞게 수정한다.

```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
jwt.secret=YOUR_LONG_RANDOM_SECRET_KEY
alpha-vantage.api-key=YOUR_ALPHA_VANTAGE_API_KEY
```

`application.properties`는 API 키와 비밀번호를 포함하므로 GitHub에 올라가지 않는다.

### 4. 서버 실행

```bash
./gradlew bootRun
```

## Docker로 실행

### 1. 환경변수 파일 생성

프로젝트 최상위에 `.env` 파일을 만들고 실제 값만 입력한다.

```properties
MYSQL_USER=backtest_user
MYSQL_PASSWORD=backtest_password
MYSQL_ROOT_PASSWORD=root_password
JWT_SECRET=YOUR_LONG_RANDOM_SECRET_KEY
ALPHA_VANTAGE_API_KEY=YOUR_ALPHA_VANTAGE_API_KEY
```

`.env`에는 비밀값이 포함되므로 GitHub에 올라가지 않는다.

### 2. 컨테이너 실행

```bash
docker compose up --build
```

실행 후 Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

### 3. 컨테이너 종료

```bash
docker compose down
```

MySQL 데이터는 Docker Volume에 저장되므로 컨테이너를 종료해도 유지된다.

## 테스트 실행

```bash
./gradlew test
```

## API 문서

서버 실행 후 아래 주소에서 Swagger UI를 사용할 수 있습니다.

```text
http://localhost:8080/swagger-ui/index.html
```

로그인 API로 JWT를 발급받은 뒤 Swagger UI의 `Authorize`에 토큰을 등록하면 인증이 필요한 API도 테스트할 수 있습니다.

## 향후 개선 계획

* 거래 수수료·슬리피지 반영
* RSI, 볼린저 밴드 등 전략 확장
* AWS 배포 및 CI/CD 구축
* Redis 캐시와 성능 개선
