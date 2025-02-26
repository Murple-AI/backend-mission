# Chat Client

테스트용 채팅 클라이언트입니다.

## 사용법
1. 도커 사용
   1. 도커 이미지 빌드시 `CHAT_SERVER` 환경변수에 웹소켓 서버 주소를 전달합니다. - 도커에서 접근할 수 있는 주소여야 합니다.
     ```bash
    docker build . --build-arg CHAT_SERVER=[ws://호스트:포트번호] -t murple-test-chat-client .

    docker run -it --rm -p [원하는포트:80]  murple-test-chat-client
    ```
   2. 도커 컨테이너는 80 포트를 사용합니다. 원하는 포트로 매핑 후 브라우저에서 접속합니다.
2. pnpm 등 패키지 매니저 사용
   1. 패키지 매니저를 사용하여 의존성을 설치합니다.
   2. `CHAT_SERVER` 환경변수에 웹소켓 서버 주소를 전달합니다.
   3. `dev` 명령어로 실행합니다.
   4. 브라우저에서 `http://localhost:5173`으로 접속합니다.

