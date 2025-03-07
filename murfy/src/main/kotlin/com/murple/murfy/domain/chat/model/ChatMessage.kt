package com.murple.murfy.domain.chat.model



enum class MessageType {
    BROADCAST,
    PRIVATE
}

data class MessageAggregate(
    val type: MessageType,
    val targetSessionId: String? = null,
    val searchedUserIds: String? = null ,
    val originalMessage: String
)

