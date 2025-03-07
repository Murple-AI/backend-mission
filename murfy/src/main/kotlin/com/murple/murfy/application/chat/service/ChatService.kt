package com.murple.murfy.application.chat.service

import com.murple.murfy.application.user.service.UserService
import com.murple.murfy.domain.chat.model.MessageAggregate
import com.murple.murfy.domain.chat.model.MessageType
import com.murple.murfy.domain.user.model.UserBasic
import org.springframework.stereotype.Service


@Service
class ChatService(
    private val userService: UserService
)  {

     fun sendMessageFromUser(messageText: String): MessageAggregate {
        val mentionedUsers = findMentionedUsers(messageText)

        return if (mentionedUsers.isNotEmpty()) {
            // 언급된 사용자가 있으면 PRIVATE 메시지 생성
            val userInfo = formatUserInfo(mentionedUsers)
            val responseMessage = "User ids: $userInfo"

            MessageAggregate(
                type = MessageType.PRIVATE,
                searchedUserIds = responseMessage,
                originalMessage = messageText
            )
        } else {
            MessageAggregate(
                type = MessageType.BROADCAST,
                originalMessage = messageText
            )
        }
    }

     fun receiveMessageFromRedis(redisMessage: String): MessageAggregate {
        val parts = redisMessage.split("::", limit = 2)
        if (parts.size < 2) {
            throw IllegalArgumentException("Not valid Redis message format")
        }
        val messageType = parts[0]

        return when (messageType) {
            "BROADCAST" -> {
                val content = parts[1]
                MessageAggregate(
                    type = MessageType.BROADCAST,
                    originalMessage = content
                )
            }
            else -> throw IllegalArgumentException("Not valid message type: $messageType")
        }
    }

    private fun findMentionedUsers(messageText: String): List<UserBasic> {
        val userNameList = extractUserNamesFromMessage(messageText)
        if (userNameList.isEmpty()) return emptyList()

        if (userNameList.size == 1) {
            return userService.findTopUsersByName(userNameList.first())
        }
        return userService.findTopUsersByNameList(userNameList)
    }

    private fun extractUserNamesFromMessage(message: String): List<String> {
        // @이름 으로 파싱
        val pattern = Regex("@([\\p{L}\\p{N}_]+)")
        return pattern.findAll(message)
            .map { it.groupValues[1] }
            .toList()
    }

    private fun formatUserInfo(users: List<UserBasic>): String {
        return users.joinToString(", ") { "${it.name}(ID: ${it.id})" }
    }
}
