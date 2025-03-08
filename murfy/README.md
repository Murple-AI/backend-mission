
# 사용자 정보 CRUD REST API & 채팅 서버 구현 


## 기술 스택

- **언어**: Kotlin
- **프레임워크**: Spring Boot
- **데이터베이스**: PostgreSQL, Redis
- **ORM**: Spring Data JPA
- **테스트**: JUnit 5, MockK
- **컨테이너화**: Docker, Docker Compose

## 주요 기능

- 사용자 관리 (생성, 조회, 수정, 삭제)
- 전화번호 관리 (추가, 수정, 삭제)
- 주소 관리 (추가, 수정, 삭제)
- 채팅: 사용자 PK 검색

## 아키텍처

프로젝트는 DDD(Domain-Driven Design) 패턴을 기반으로 설계되었으며 다음과 같은 계층으로 구성되어 있습니다:

- **프레젠테이션 계층**: API 엔드포인트 및 요청/응답 처리
- **애플리케이션 계층**: 비즈니스 로직 처리 및 서비스 조정
- **도메인 계층**: 핵심 비즈니스 규칙 및 엔티티 정의
- **인프라스트럭처 계층**: 데이터 접근 및 외부 시스템 연동



## API 엔드포인트

### 사용자 관리

- `POST /api/users` - 새 사용자 생성
- `GET /api/users/{id}` - ID로 사용자 조회
- `PUT /api/users/{id}` - 사용자 정보 업데이트
- `DELETE /api/users/{id}` - 사용자 삭제


### 전화번호 관리

- `POST /api/users/{userId}/phones` - 사용자 전화번호 추가
- `PUT /api/users/{userId}/phones/{phoneId}` - 전화번호 정보 업데이트
- `DELETE /api/users/{userId}/phones/{phoneId}` - 전화번호 삭제

### 주소 관리

- `POST /api/users/{userId}/addresses` - 사용자 주소 추가
- `PUT /api/users/{userId}/addresses/{addressId}` - 주소 정보 업데이트
- `DELETE /api/users/{userId}/addresses/{addressId}` - 주소 삭제

## 시작하기


### 로컬 개발 환경 설정

1. Docker Compose로 서비스(api & 채팅 서버, PostgreSQL, Redis) 실행

```bash
docker-compose up -d --build
```

2. 채팅 클라이언트 도커 실행
```bash
docker build . --build-arg CHAT_SERVER=[ws://호스트:포트번호] -t murple-test-chat-client .

docker run -it --rm -p [원하는포트:80]  murple-test-chat-client

웹소켓 기본 호스트 = ws://localhost/chat 
```
3. swagger 를 활용하여 간단한 api 테스트를 진행할 수 있습니다. 
4. 채팅 클라이언트를 통해, 실시간 채팅 기능을 테스트할 수 있습니다.
   a. '@사용자이름' 으로 메세지를 보내면, 사용자이름에 해당하는 pk 를 발신자에게만 전달합니다.
   b. 복수의 사용자 검색도 가능합니다. ex) "@철수 @영희"



## 테스트

1. UserAggregateTests: 도메인 모델, 비지니스 규칙 테스트  
2. UserServiceTests: api 로직 테스트 


## 데이터베이스 구조

### 주요 테이블

- `users` - 사용자 정보
- `phone_numbers` - 사용자의 전화번호 정보
- `addresses` - 사용자의 주소 정보

