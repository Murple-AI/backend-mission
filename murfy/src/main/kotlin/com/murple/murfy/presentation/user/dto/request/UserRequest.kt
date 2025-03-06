package com.murple.murfy.presentation.user.dto.request

import com.murple.murfy.application.user.dto.UserDto


data class UserRequest(
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
    val phones: List<PhoneRequest>? = null,
    val addresses: List<AddressRequest>? = null
) {
    fun toServiceDto(): UserDto {
        return UserDto(
            name  = name,
            age = age,
            gender = gender,
            email = email,
            phones = phones?.map{x -> x.toServiceDto()},
            addresses = addresses?.map { x -> x.toServiceDto()}
        )
    }
}


data class UserBasicInfoRequest(
    val name: String,
    val age: Int? = null,
    val gender: String? = null,
    val email: String? = null,
) {
    fun toServiceDto(): UserDto {
        return UserDto(
            name  = name,
            age = age,
            gender = gender,
            email = email,
        )
    }
}
