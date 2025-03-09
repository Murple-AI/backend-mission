package ai.murple.wanho.handler

import ai.murple.wanho.service.ChatService
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(private val chatService: ChatService) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        chatService.addSession(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        chatService.handleMessage(session, message.payload)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        chatService.removeSession(session)
    }
}