# 머플 백엔드 엔지니어 채용과제

## 개요

본 문서는 머플(Murfy)에서 백엔드 개발자 채용을 위한 과제 안내입니다. 지원자는 Spring Boot와 Kotlin을 활용하여 RESTful API를 개발하고, WebSocket을 이용한 실시간 채팅 서버를 구현해야 합니다. 또한, API 서버와 데이터베이스를 Docker 환경에서 실행할 수 있도록 구성해야 합니다.

본 과제는 기본적인 CRUD API 구현뿐만 아니라, WebSocket을 활용한 실시간 메시지 전송, 데이터베이스 연동, Docker 기반의 환경 설정 등을 포함하여 백엔드 개발자로서의 핵심 역량을 평가하는 데 초점을 맞추고 있습니다. 개발 과정에서는 코드의 명확성과 완성도를 중점적으로 고려하며, 과제의 요구사항을 충족하는 것은 물론, 추가적인 개선 사항이나 확장 기능을 고민하는 것이 좋은 평가를 받을 수 있는 요소가 될 것입니다.

이 문서를 참고하여 요구사항을 충실히 구현하고, 작업 과정을 기록하여 PR을 제출해 주세요. 원활한 협업과 코드 품질을 고려한 개발이 중요한 평가 기준이므로, 이에 유의하여 과제를 수행하시기 바랍니다.

## 환경 설정

### Docker
- Docker 지원: API 서버와 데이터베이스를 Docker로 실행할 수 있도록 설정합니다.
- 네트워크 구성: API 서버와 데이터베이스는 동일한 네트워크에 속해야 합니다.

### DB
- 데이터베이스: PostgreSQL을 사용합니다.
- 사용자 정보 테이블: 다음과 같은 필드를 포함해야 합니다.
  - 이름 - 길이 제한 1024자, 필수
  - 나이 - 숫자, 음수 체크 필요, 선택사항
  - 성별 - 남자, 여자, 선택사항
  - 이메일 - 이메일 형식, 1024자 이하, 선택사항
  - 전화번호 - 선택사항, E164 포맷(https://en.wikipedia.org/wiki/E.164)
  - 국가코드 - 2자리, 선택사항, ISO 3166-1 alpha-2, 대문자, 입력이 없는 경우, 전화번호에서 추출(KR, US, JP, CN 까지만 확인되면 됩니다.)
  - 주소 - 1024자 이하, 선택사항

## 기능 요구사항

### REST API
- 프레임워크: Spring Boot + Kotlin을 사용하여 RESTful API를 구현합니다.
- API 서버는 다음과 같은 기능을 제공해야 합니다.
  - 사용자 정보를 저장하는 API
  - 사용자 정보를 조회하는 API
  - 사용자 정보를 수정하는 API
  - 사용자 정보를 삭제하는 API
- 요청 및 응답 데이터 형식: JSON 사용
- 인증: API 서버는 인증 기능을 포함하지 않습니다.

### Websocket
- Websocket을 이용한 채팅 서버를 구현해 주세요.
- 기능:
  - 사용자가 채팅방에서 메시지를 보내면 본인을 포함한 모든 사용자에게 전달됨
  - 메세지에 DB에 저장된 사용자 이름이 포함된 경우, 해당 사용자의 PK를 찾아 응답
- 채팅 클라이언트: 별도로 제공되며, 실행 방법은 chat-client/README.md 참고
- 메시지 포맷: 클라이언트-서버 간 메시지는 평문으로 전송됨

## 과제 제출
- 본 레포지토리를 fork하여 작업 후, PR을 보내주세요.
- PR 제목은 `mission: 본인이름` 형식으로 작성해주세요.
- PR 본문은 과제 수행과정 중 어려웠던 점, 고민했던 점, 혹은 개선하고 싶은 점 등을 자유롭게 작성해주세요.
- PR에는 완료된 과제의 결과물이 포함되어야 합니다.

## 문의 사항

문의사항이 있으시면 담당자에게 이메일 또는 제공된 연락처로 연락 부탁드립니다.

담당자: 조석규  
이메일: seokgyu.cho@murple.ai
