package com.murple.murfy.infrastructure.chat.websocket

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap


@Service
class WebSocketSessionManager {
    private val logger = KotlinLogging.logger {}
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun registerSession(session: WebSocketSession) {
        sessions[session.id] = session
        logger.info { "Session registered: ${session.id}" }
    }

    fun removeSession(sessionId: String) {
        sessions.remove(sessionId)
        logger.info { "Session removed: $sessionId" }
    }

    fun broadcastMessage(message: String) {
        sessions.values.forEach { session ->
            if (session.isOpen) {
                try {
                    session.sendMessage(TextMessage(message))
                } catch (e: Exception) {
                    logger.error { "Error sending message: ${e.message}" }
                }
            }
        }
    }
}
