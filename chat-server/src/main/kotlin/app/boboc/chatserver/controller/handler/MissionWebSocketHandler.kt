package app.boboc.chatserver.controller.handler

import app.boboc.chatserver.service.UserService
import app.boboc.chatserver.service.WebSocketSessionService
import app.boboc.handler.CoroutineWebSocketHandler
import app.boboc.util.WebSocketSessionUtil.sendMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class MissionWebSocketHandler(
    private val webSocketSessionService: WebSocketSessionService,
    private val userService: UserService,
) : CoroutineWebSocketHandler() {
    companion object {
    }

    override suspend fun coroutineHandle(session: WebSocketSession, message: WebSocketMessage) {
        val originMessage = message.payloadAsText
        val users = userService.getUsersByName(originMessage)
        if (users.isEmpty()) {
            webSocketSessionService.broadCastWithoutSender(session.id, originMessage)
            session.sendMessage(originMessage)
        } else {
            session.sendMessage(users.map { user -> user.id }.joinToString())
        }
    }

    override fun handle(session: WebSocketSession): Mono<Void> {
        webSocketSessionService.addSession(session)
        return super.handle(session)
            .doFinally {
                webSocketSessionService.removeSession(session)
            }
    }
}
