package ai.murple.wanho.service

import ai.murple.wanho.repository.KoreanLastNameRepository
import ai.murple.wanho.repository.UserInfoRepository
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val koreanLastNameRepository: KoreanLastNameRepository,
) : ChatService {

    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val userNameSessions = ConcurrentHashMap<String, String>()

    override fun addSession(session: WebSocketSession) {
        val userId = session.id

        sessions[userId] = session

        // TODO: 인증 기능 추가시 인증을 통해 로그인한 사용자의 아이디를 얻어오도록 변경 필요
        userNameSessions[userId] = randomLoginUserName()
    }

    override fun removeSession(session: WebSocketSession) {
        val userId = session.id

        sessions.remove(userId)
        userNameSessions.remove(userId)

    }

    override fun handleMessage(session: WebSocketSession, message: String) {

        val userNames = extractKoreanNames(message)

        if (userNames.isNotEmpty()) {
            userNames.forEach { name ->
                val userEntityList = userInfoRepository.findTop5ByNameOrderByCreatedAtAsc(name)
                userEntityList.forEach { userInfo ->
                    userNameSessions[userInfo.name]?.let { sessionId ->
                        sessions[sessionId]?.sendMessage(TextMessage(message))
                    }
                }

            }
        } else {
            sessions.values.forEach { webSocketSession ->
                if (webSocketSession.isOpen) {
                    webSocketSession.sendMessage(TextMessage(message))
                }
            }

        }
    }


    /**
     * 한국의 성씨를 추출하는 메서드
     *
     * @param content
     * @return 추출된 이름 배열
     */
    private fun extractKoreanNames(content: String): List<String> {
        // https://ko.wikipedia.org/wiki/%ED%95%9C%EA%B5%AD%EC%9D%98_%EC%84%B1%EC%94%A8_%EB%AA%A9%EB%A1%9D
        val lastNames = koreanLastNameRepository.findAll().map { it.lastName }.toList()

        // 가족관계등록예규 제475호, '이름의 기재문자와 관련된 가족관계등록사무'
        val words = Regex("[가-힣]{2,6}").findAll(content).map { it.value }.toList()


        return words.filter { word ->
            lastNames.any {
                word.startsWith(it)
            }
        }.distinct()
    }

    private fun randomLoginUserName(): String {
        val nameList = listOf("김민준", "이서준", "박예준", "최도윤", "강주원")

        return nameList.random()
    }

}