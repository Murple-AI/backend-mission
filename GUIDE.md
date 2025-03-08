## 실행 방법

1. Docker 실행

```shell
docker compose up -d
```

※ 로컬에서 `5432` `8080` `3000` 포트가 실행되고 있지 않은 지, 확인 후, 실행 중이라면 해당 프로세스를 종료 하고 수행 필요

2. Chat Client 접속

`http://localhost:3000` 로 Chat Client 접속

3. 데이터 삽입

- `postgres://localhost:5432`를 통해서 DB에 직접 데이터 적재 가능.
- API를 통한 요청 필요 시, 아래의 [API Guide](#api-guide) 참고

## API Guide

| 기능         | HTTP Method | URI                                                               | 설명                      | Request Format                      |
|------------|-------------|-------------------------------------------------------------------|-------------------------|-------------------------------------|
| 사용자 목록 조회  | GET         | http://localhost:8080/users                                       | 사용자 목록 조회               | -                                   |
| 사용자 조회     | GET         | http://localhost:8080/users/{userId}                              | 특정 사용자 상세 정보 조회         | -                                   |
| 사용자 등록     | POST        | http://localhost:8080/users                                       | 사용자 정보 등록               | [USER](#user-request)               |
| 사용자 정보 수정  | PUT         | http://localhost:8080/users/{userId}                              | 사용자 ID 기반 해당 사용자의 정보 수정 | [USER](#user-request)               |
| 사용자 삭제     | DELETE      | http://localhost:8080/users/{userId}                              | 사용자 ID 기반 해당 사용자의 정보 삭제 | -                                   |
| 주소 목록 조회   | GET         | http://localhost:8080/users/{userId}/addresses                    | 특정 사용자의 주소 목록 조회        | -                                   |
| 주소 조회      | GET         | http://localhost:8080/users/{userId}/addresses/{addressId}        | 특정 사용자의 특정 주소 조회        | -                                   |
| 주소 등록      | POST        | http://localhost:8080/users/{userId}/addresses                    | 특정 사용자의 주소 정보 등록        | [Address](#address-request)         |
| 주소 수정      | PUT         | http://localhost:8080/users/{userId}/addresses/{addressId}        | 특정 사용자의 특정 주소 정보 수정     | [Address](#address-request)         |
| 주소 삭제      | DELETE      | http://localhost:8080/users/{userId}/addresses/{addressId}        | 특정 사용자의 특정 주소 정보 삭제     | -                                   |
| 전화번호 목록 조회 | GET         | http://localhost:8080/users/{userId}/phone-number                 | 특정 사용자의 전화번호 목록 조회      | -                                   |
| 전화번호 조회    | GET         | http://localhost:8080/users/{userId}/phone-number/{phoneNumberId} | 특정 사용자의 특정 전화번호 조회      | -                                   |
| 전화번호 등록    | POST        | http://localhost:8080/users/{userId}/phone-number                 | 특정 사용자의 전화번호 정보 등록      | [PhoneNumber](#phonenumber-request) |
| 전화번호 수정    | PUT         | http://localhost:8080/users/{userId}/phone-number/{phoneNumberId} | 특정 사용자의 특정 전화번호 정보 수정   | [PhoneNumber](#phonenumber-request) |
| 전화번호 삭제    | DELETE      | http://localhost:8080/users/{userId}/phone-number/{phoneNumberId} | 특정 사용자의 특정 전화번호 정보 삭제   | -                                   |

### DTO

#### User Request

```
{
  "name": String,
  "email": String,
  "age": Int?,
  "gender": "MALE" or "FEMALE"
}
```

#### Address Request

```
{
  "label": String,
  "address": String
}
```

#### PhoneNumber Request

```
{
  "label": String,
  "phoneNumber": String,
  "countryCode": String?
}
```