package com.murple.murfy.presentation.dto.response

import java.time.ZonedDateTime


data class AddressResponse(
    val id: Long,
    val label: String,
    val address: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime? = null
)
