package app.boboc.chatserver.service

import app.boboc.util.WebSocketSessionUtil.sendMessage
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession

@Service
class WebSocketSessionService {

    private val sessions = mutableMapOf<String, WebSocketSession>()

    fun addSession(session: WebSocketSession) {
        sessions[session.id] = session
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session.id)
    }

    suspend fun broadCastWithoutSender(senderId: String, message: String) {
        sessions.forEach { (id, session) ->
            if (id != senderId) {
                session.sendMessage(message)
            }
        }
    }


}
