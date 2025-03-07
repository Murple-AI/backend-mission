package com.murple.murfy.presentation.chat

import com.murple.murfy.application.chat.service.ChatService
import com.murple.murfy.domain.chat.model.MessageType
import com.murple.murfy.infrastructure.chat.websocket.WebSocketSessionManager
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(
    private val chatService: ChatService,
    private val redisTemplate: RedisTemplate<String, String>,
    private val socketSessionManager: WebSocketSessionManager
) : TextWebSocketHandler() {

    private val logger = KotlinLogging.logger {}

    override fun afterConnectionEstablished(session: WebSocketSession) {
        socketSessionManager.registerSession(session)
        logger.info { "WebSocket connection established: ${session.id}" }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        socketSessionManager.removeSession(session.id)
        logger.info { "WebSocket connection closed: ${session.id}" }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val messageText = message.payload

        try {
            val result = chatService.sendMessageFromUser(messageText)

            when (result.type) {
                MessageType.PRIVATE -> {
                    // PRIVATE 메시지는 Redis를 사용하지 않고 발신자에게 바로 응답
                    session.sendMessage(TextMessage(result.searchedUserIds ?: ""))
                }
                MessageType.BROADCAST -> {
                    val redisMessage = "BROADCAST::${result.originalMessage}"
                    redisTemplate.convertAndSend("chat:messages", redisMessage)
                }
            }
        } catch (e: Exception) {
            logger.error { "Error processing message: ${e.message}" }
            session.sendMessage(TextMessage("An error occurred while processing the message."))
        }
    }
}
