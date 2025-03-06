package app.boboc.chatserver.controller

import app.boboc.annotation.WebSocketController
import app.boboc.annotation.WebSocketHandlerMapping
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession

@WebSocketController
class WebSocketRouter {
    @WebSocketHandlerMapping("")
    suspend fun handleWebSocket(session: WebSocketSession, message: WebSocketMessage): String {
        println(session)

        return "ECHO ${message.payloadAsText}"
    }
}
