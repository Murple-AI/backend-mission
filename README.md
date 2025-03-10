# 머플 백엔드 엔지니어 채용과제

## 개요

본 문서는 머플(Murfy)에서 백엔드 개발자 채용을 위한 과제 안내입니다. 지원자는 요구사항을 분석하고 Spring Boot와 Kotlin을 활용하여 RESTful API를 개발하고, WebSocket을 이용한 실시간 채팅 서버를 구현해야 합니다. 또한, API 서버와 데이터베이스를 Docker 환경에서 실행할 수 있도록 구성해야 합니다.

본 과제는 기본적인 CRUD API 구현뿐만 아니라, 요구사항 분석 후 설계, 구현, WebSocket을 활용한 실시간 메시지 전송, 데이터베이스 연동, Docker 기반의 환경 설정 등을 포함하여 백엔드 개발자로서의 핵심 역량을 평가하는 데 초점을 맞추고 있습니다. 개발 과정에서는 코드의 명확성과 완성도를 중점적으로 고려하며, 과제의 요구사항을 충족하는 것은 물론, 추가적인 개선 사항이나 확장 기능을 고민하는 것이 좋은 평가를 받을 수 있는 요소가 될 것입니다.

이 문서를 참고하여 요구사항을 충실히 구현하고, 작업 과정을 기록하여 PR을 제출해 주세요. 원활한 협업과 코드 품질을 고려한 개발이 중요한 평가 기준이므로, 이에 유의하여 과제를 수행하시기 바랍니다.

## 환경 설정

### Docker
- Docker 지원: API 서버와 데이터베이스를 Docker로 실행할 수 있도록 설정합니다.
- 네트워크 구성: API 서버와 데이터베이스는 동일한 네트워크에 속해야 합니다.

### DB
- 데이터베이스: PostgreSQL을 사용합니다.
- 테이블: 설계 시, 다음과 같은 제약사항을 만족해야 합니다.
  - 이름 - 길이 제한 1024자
  - 이메일 - 1024자 이하
  - 주소 - 1024자 이하

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

### 도메인 객체
- 사용자 정보: 구현 시, 다음과 같은 요구사항을 만족해야 합니다. 하나의 도메인 객체를 말하는 것이 아닙니다.
  - 이름 - 길이 제한 1024자, 필수. 성과 이름을 한 번에 입력받습니다.
  - 나이 - 숫자, 음수 체크 필요, 선택사항, 만 나이로 입력받습니다.
  - 성별 - 남자, 여자, 선택사항
  - 이메일 - 이메일 형식, 1024자 이하, 선택사항
  - 전화번호 - 선택사항, 자택, 직장 등의 레이블을 붙일 수 있고, 0-8개까지 입력 가능합니다. E164 포맷(https://en.wikipedia.org/wiki/E.164)
  - 전화번호 국가코드 - 2자리, 선택사항, 전화번호마다 함께 저장되어야 합니다. ISO 3166-1 alpha-2, 대문자, 입력이 없는 경우, 전화번호에서 추출(KR, US, JP, CN 까지만 확인되면 됩니다.)합니다.
  - 전화번호 본인인증 여부 - 전화번호는 인증된 번호인지를 저장해야 합니다.
  - 주소 - 1024자 이하, 선택사항, 전화번호와 같이 자택, 직장 등의 레이블을 붙일 수 있고, 0-8개까지 입력 가능합니다.
  
### Websocket
- Websocket을 이용한 채팅 서버를 구현해 주세요.
- 기능:
  - 사용자가 채팅방에서 메시지를 보내면 본인을 포함한 모든 사용자에게 전달되어야 합니다.
  - 메세지에 DB에 저장된 사용자 이름이 포함된 경우, 해당 사용자의 PK를 찾아 메세지를 보낸 사람에게만 전달합니다.
  - 동일한 사용자 이름이 여럿인 경우, 생성시간 순으로 5개까지 전달합니다.
  - 재접속 후 사용자 채팅 기록 유지 등의 기능은 불필요함. 개별 접속이 새 접속으로 취급되어도 관계없습니다.
- 채팅 클라이언트: 별도로 제공되며, 실행 방법은 `./chat-client/README.md` 참고해 주세요.
- 메시지 포맷: 클라이언트-서버 간 메시지는 평문으로 전송됩니다.

## 과제 제출
- 본 레포지토리를 fork하여 작업 후, PR을 보내주세요.
- PR 제목은 `mission: 본인이름` 형식으로 작성해주세요.
- PR 본문은 과제 수행과정 중 어려웠던 점, 고민했던 점, 혹은 개선하고 싶은 점 등을 자유롭게 작성해주세요.
- PR에는 완료된 과제의 결과물과 그 결과물을 확인할 수 있는 방법(예: 터미널에서 docker compose up -d 후 localhost:3000접속 등)이 포함되어야 합니다.
- 제출된 PR은 보안과 형평성을 위해 저장후 바로 삭제됩니다.

## 문의 사항

문의사항이 있으시면 담당자에게 이메일 또는 제공된 연락처로 연락 부탁드립니다.

담당자: 조석규  
이메일: seokgyu.cho@murple.ai
