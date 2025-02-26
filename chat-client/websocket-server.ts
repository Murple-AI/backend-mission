// websocket_server.ts
const clients = {};

Deno.serve({port: 8080}, (req) => {
    const {socket, response} = Deno.upgradeWebSocket(req);
    const key = Math.random().toString(16).slice(2)

    socket.onopen = () => {
        for (let clientsKey in clients) {
            const c = clients[clientsKey];
            c.send(`새로운 클라이언트 ${key} 연결됨!`);
        }
        console.log(`WebSocket ${key} 연결됨!`);
        clients[key] = socket;
        socket.send("key: " + key);
    };

    socket.onmessage = (event) => {
        console.log("클라이언트로부터 메시지:", event.data);
        for (let clientsKey in clients) {
            const c = clients[clientsKey];
            c.send(`${key}: ${event.data}`);
        }
    };

    socket.onerror = (err) => {
        console.error("WebSocket 오류:", err);
    };

    socket.onclose = () => {
        clients[key] = undefined;
        for (let clientsKey in clients) {
            const c = clients[clientsKey];
            c.send(`클라이언트 ${key} 연결 종료`);
        }

        console.log("WebSocket 연결 종료됨");
    }

    return response;
});

console.log("WebSocket 서버가 ws://localhost:8080 에서 실행 중...");
