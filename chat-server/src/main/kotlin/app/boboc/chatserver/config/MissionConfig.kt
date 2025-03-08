package app.boboc.chatserver.config

import app.boboc.chatserver.controller.handler.MissionWebSocketHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping

@Configuration
class MissionConfig(
    private val missionWebSocketHandler: MissionWebSocketHandler
) {
    @Bean
    fun webSocketSessionScope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Bean
    fun websocketHandlerMapping(): HandlerMapping {
        return SimpleUrlHandlerMapping(mapOf("" to missionWebSocketHandler), -1)
    }
}
