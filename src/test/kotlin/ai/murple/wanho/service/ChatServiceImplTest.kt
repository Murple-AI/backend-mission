package ai.murple.wanho.service

import ai.murple.wanho.entity.KoreanLastNameEntity
import ai.murple.wanho.entity.UserInfoEntity
import ai.murple.wanho.repository.KoreanLastNameRepository
import ai.murple.wanho.repository.UserInfoRepository
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@ExtendWith(SpringExtension::class)
class ChatServiceImplTest {

    private lateinit var chatService: ChatServiceImpl
    private val userInfoRepository = mockk<UserInfoRepository>()
    private val koreanLastNameRepository = mockk<KoreanLastNameRepository>()
    private val session = mockk<WebSocketSession>(relaxed = true)
    private val anotherSession = mockk<WebSocketSession>(relaxed = true)

    @BeforeEach
    fun setup() {
        chatService = ChatServiceImpl(userInfoRepository, koreanLastNameRepository)

        every { session.id } returns "session1"
        every { anotherSession.id } returns "session2"
    }

    @Test
    fun `채팅 접속 - 성공`() {
        // when
        chatService.addSession(session)

        // then
        val sessionsField = chatService.javaClass.getDeclaredField("sessions")
        sessionsField.isAccessible = true
        val sessions = sessionsField.get(chatService) as ConcurrentHashMap<*, *>

        val userNameSessionsField = chatService.javaClass.getDeclaredField("userNameSessions")
        userNameSessionsField.isAccessible = true
        val userNameSessions = userNameSessionsField.get(chatService) as ConcurrentHashMap<*, *>

        assert(sessions.containsKey("session1"))
        assert(userNameSessions.containsKey("session1"))
    }

    @Test
    fun `채팅 종료 - 성공`() {
        // given
        chatService.addSession(session)

        // when
        chatService.removeSession(session)

        // then
        val sessionsField = chatService.javaClass.getDeclaredField("sessions")
        sessionsField.isAccessible = true
        val sessions = sessionsField.get(chatService) as ConcurrentHashMap<*, *>

        val userNameSessionsField = chatService.javaClass.getDeclaredField("userNameSessions")
        userNameSessionsField.isAccessible = true
        val userNameSessions = userNameSessionsField.get(chatService) as ConcurrentHashMap<*, *>

        assert(!sessions.containsKey("session1"))
        assert(!userNameSessions.containsKey("session1"))
    }

    @Test
    fun `이름 존재 채팅 - 성공`() {
        // given
        val message = "김민준 안녕"

        every { koreanLastNameRepository.findAll() } returns listOf(
            KoreanLastNameEntity(1, "김"),
            KoreanLastNameEntity(2, "이"),
            KoreanLastNameEntity(3, "박")
        )

        val userEntity = mockk<UserInfoEntity> {
            every { name } returns "김민준"
        }

        every { userInfoRepository.findTop5ByNameOrderByCreatedAtAsc("김민준") } returns listOf(userEntity)

        chatService.addSession(session)
        chatService.addSession(anotherSession)

        val sessionId = session.id
        val userNameSessionsField = chatService.javaClass.getDeclaredField("userNameSessions")
        userNameSessionsField.isAccessible = true
        val userNameSessions = userNameSessionsField.get(chatService) as MutableMap<String, String>
        userNameSessions["김민준"] = sessionId

        every { session.isOpen } returns true
        every { anotherSession.isOpen } returns true

        val sendMessageSlot = slot<TextMessage>()
        every { session.sendMessage(capture(sendMessageSlot)) } just Runs

        // when
        chatService.handleMessage(session, message)

        // then
        verify(exactly = 1) { session.sendMessage(TextMessage(message)) }
        excludeRecords { session.id }
        confirmVerified(session)
    }

    @Test
    fun `이름 없는 채팅 - 성공`() {
        // given
        val message = "안녕"
        every { koreanLastNameRepository.findAll() } returns emptyList()

        chatService.addSession(session)
        chatService.addSession(anotherSession)

        every { session.isOpen } returns true
        every { anotherSession.isOpen } returns true

        val sendMessageSlot1 = slot<TextMessage>()
        val sendMessageSlot2 = slot<TextMessage>()

        every { session.sendMessage(capture(sendMessageSlot1)) } just Runs
        every { anotherSession.sendMessage(capture(sendMessageSlot2)) } just Runs

        // when
        chatService.handleMessage(session, message)

        // then
        verify { session.sendMessage(TextMessage(message)) }
        verify { anotherSession.sendMessage(TextMessage(message)) }
    }

}