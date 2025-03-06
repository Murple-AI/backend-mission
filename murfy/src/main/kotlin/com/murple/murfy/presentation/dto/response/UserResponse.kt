package com.murple.murfy.presentation.dto.response

import com.murple.murfy.domain.model.User
import java.time.ZonedDateTime


data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneResponse> = emptyList(),
    val addresses: List<AddressResponse> = emptyList(),
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
) {
    companion object {
        fun from(model: User): UserResponse {
            return UserResponse(
                id = model.id!!,
                name = model.name,
                age = model.age,
                gender = model.gender?.toString(),
                email = model.email,
                phones = model.phones.map {x -> PhoneResponse.from(x) },
                addresses = model.addresses.map { x -> AddressResponse.from(x) },
                createdAt = model.createdAt,
                updatedAt = model.updatedAt
            )
        }
    }
}

data class UserBasicInfoResponse(
    val id: Long,
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime
) {
    companion object {
        fun from(model: User): UserBasicInfoResponse {
            return UserBasicInfoResponse(
                id = model.id!!,
                name = model.name,
                age = model.age,
                gender = model.gender?.toString(),
                email = model.email,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt
            )
        }
    }
}

