package com.murple.murfy.infrastructure.chat.redis


import com.murple.murfy.application.chat.service.ChatService
import com.murple.murfy.domain.chat.model.MessageType
import com.murple.murfy.infrastructure.chat.websocket.WebSocketSessionManager
import mu.KotlinLogging
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ChatMessageListener(
    private val chatService: ChatService,
    private val messagingService: WebSocketSessionManager
) : MessageListener {

    private val logger = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val messageContent = String(message.body)
        try {
            val result = chatService.receiveMessageFromRedis(messageContent)

            // Redis로 온 메시지는 항상 BROADCAST 타입만 처리
            if (result.type == MessageType.BROADCAST) {
                messagingService.broadcastMessage(result.originalMessage)
            }
        } catch (e: Exception) {
            logger.error { "Error processing Redis message: ${e.message}" }
        }
    }
}
