package ai.murple.wanho.service

import org.springframework.web.socket.WebSocketSession

interface ChatService {

    /**
     * 새로운 웹소켓 연결
     *
     * @param session
     */
    fun addSession(session: WebSocketSession)

    /**
     * 웹소켓 연결 종료
     *
     * @param session
     */
    fun removeSession(session: WebSocketSession)


    /**
     * 메세지 전송
     *
     * @param session
     * @param message
     */
    fun handleMessage(session: WebSocketSession, message: String)
}