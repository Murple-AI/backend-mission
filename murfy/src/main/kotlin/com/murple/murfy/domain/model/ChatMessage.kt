package com.murple.murfy.domain.model

import java.time.ZonedDateTime


class ChatMessage(
    val id: Long? = null,
    val senderId: String,
    val content: String,
    val sentAt: ZonedDateTime = ZonedDateTime.now()
)
