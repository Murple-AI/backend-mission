// websocket_server.ts
Deno.serve({ port: 8080 }, (req) => {
    const { socket, response } = Deno.upgradeWebSocket(req);

    socket.onopen = () => {
        console.log("WebSocket 연결됨!");
    };

    socket.onmessage = (event) => {
        console.log("클라이언트로부터 메시지:", event.data);
        socket.send(`서버에서 받은 메시지: ${event.data}`);
    };

    socket.onerror = (err) => {
        console.error("WebSocket 오류:", err);
    };

    socket.onclose = () => {
        console.log("WebSocket 연결 종료됨");
    };

    return response;
});

console.log("WebSocket 서버가 ws://localhost:8080 에서 실행 중...");
