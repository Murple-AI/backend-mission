package com.murple.murfy.presentation.user.dto.request

import com.murple.murfy.application.user.dto.UserDto
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "사용자 전체 정보 요청")
data class UserRequest(
    @Schema(description = "사용자 이름", example = "김철수", required = true)
    val name: String,

    @Schema(description = "사용자 나이", example = "30")
    val age: Int? = null,

    @Schema(description = "사용자 성별", example = "male")
    val gender: String? = null,

    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String? = null,

    @Schema(description = "사용자 전화번호 목록")
    val phones: List<PhoneRequest>? = null,

    @Schema(description = "사용자 주소 목록")
    val addresses: List<AddressRequest>? = null
) {
    fun toServiceDto(): UserDto {
        return UserDto(
            name = name,
            age = age,
            gender = gender,
            email = email,
            phones = phones?.map { x -> x.toServiceDto() },
            addresses = addresses?.map { x -> x.toServiceDto() }
        )
    }
}

@Schema(description = "사용자 기본 정보 요청")
data class UserBasicInfoRequest(
    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    val name: String,

    @Schema(description = "사용자 나이", example = "30")
    val age: Int? = null,

    @Schema(description = "사용자 성별", example = "남성")
    val gender: String? = null,

    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String? = null
) {
    fun toServiceDto(): UserDto {
        return UserDto(
            name = name,
            age = age,
            gender = gender,
            email = email
        )
    }
}
