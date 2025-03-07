package com.murple.murfy.application.chat.service

import com.murple.murfy.domain.chat.model.MessageAggregate
import com.murple.murfy.domain.chat.model.MessageType
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class ChatService(
    private val userRepository: UserRepository
)  {

     fun processMessage(messageText: String): MessageAggregate {
        val mentionedUsers = findMentionedUsers(messageText)

        return if (mentionedUsers.isNotEmpty()) {
            // 언급된 사용자가 있으면 PRIVATE 메시지 생성
            val userInfo = formatUserInfo(mentionedUsers)
            val responseMessage = "찾은 사용자 정보: $userInfo"

            MessageAggregate(
                type = MessageType.PRIVATE,
                searchedUserIds = responseMessage
            )
        } else {
            MessageAggregate(
                type = MessageType.BROADCAST,
                searchedUserIds = messageText,
                originalMessage = messageText
            )
        }
    }

     fun processRedisMessage(redisMessage: String): MessageAggregate {
        val parts = redisMessage.split("::", limit = 3)
        if (parts.size < 2) {
            throw IllegalArgumentException("Not valid Redis message format")
        }

        val messageType = parts[0]

        return when (messageType) {
            "BROADCAST" -> {
                val content = if (parts.size >= 3) parts[2] else parts[1]
                MessageAggregate(
                    type = MessageType.BROADCAST,
                    searchedUserIds = content
                )
            }
            "PRIVATE" -> {
                val targetSessionId = parts[1]
                val content = parts[2]
                MessageAggregate(
                    type = MessageType.PRIVATE,
                    targetSessionId = targetSessionId,
                    searchedUserIds = content
                )
            }
            else -> throw IllegalArgumentException("Not valid message type: $messageType")
        }
    }

    private fun findMentionedUsers(messageText: String): List<UserAggregate> {
        return mutableListOf()
    }


    private fun formatUserInfo(users: List<UserAggregate>): String {
        return users.joinToString(", ") { "${it.name}(ID: ${it.id})" }
    }
}
