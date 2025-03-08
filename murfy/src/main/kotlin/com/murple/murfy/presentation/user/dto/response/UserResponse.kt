package com.murple.murfy.presentation.user.dto.response

import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.time.ZonedDateTime


@Schema(description = "사용자 전체 정보 응답")
data class UserResponse(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long,

    @Schema(description = "사용자 이름", example = "김철수")
    val name: String,

    @Schema(description = "사용자 나이", example = "30")
    val age: Int? = null,

    @Schema(description = "사용자 성별", example = "남성")
    val gender: String? = null,

    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String? = null,

    @Schema(description = "사용자 전화번호 목록")
    val phones: List<PhoneResponse> = emptyList(),

    @Schema(description = "사용자 주소 목록")
    val addresses: List<AddressResponse> = emptyList(),

    @Schema(description = "생성 일시", example = "2025-03-08T10:15:30+09:00")
    val createdAt: LocalDateTime,

    @Schema(description = "수정 일시", example = "2025-03-08T10:15:30+09:00")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(model: UserAggregate): UserResponse {
            return UserResponse(
                id = model.id!!,
                name = model.name,
                age = model.age,
                gender = model.gender?.toString(),
                email = model.email,
                phones = model.phones.map { x -> PhoneResponse.from(x) },
                addresses = model.addresses.map { x -> AddressResponse.from(x) },
                createdAt = model.createdAt,
                updatedAt = model.updatedAt
            )
        }
    }
}

@Schema(description = "사용자 기본 정보 응답")
data class UserBasicInfoResponse(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long,

    @Schema(description = "사용자 이름", example = "김철수")
    val name: String,

    @Schema(description = "사용자 나이", example = "30")
    val age: Int? = null,

    @Schema(description = "사용자 성별", example = "남성")
    val gender: String? = null,

    @Schema(description = "사용자 이메일", example = "user@example.com")
    val email: String? = null,

    @Schema(description = "생성 일시", example = "2025-03-08T10:15:30+09:00")
    val createdAt: LocalDateTime,

    @Schema(description = "수정 일시", example = "2025-03-08T10:15:30+09:00")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(model: UserBasic): UserBasicInfoResponse {
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
