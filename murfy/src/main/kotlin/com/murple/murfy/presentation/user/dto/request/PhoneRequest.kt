package com.murple.murfy.presentation.user.dto.request

import com.murple.murfy.application.user.dto.PhoneDto
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "전화번호 요청 정보")
data class PhoneRequest(
    @Schema(description = "전화번호 라벨", example = "work", required = true)
    val label: String,

    @Schema(description = "전화번호", example = "+821012345678", required = true)
    val number: String,

    @Schema(description = "국가 코드", example = "KR")
    val countryCode: String? = null,

    @Schema(description = "인증 여부", example = "false")
    val isVerified: Boolean = false
) {
    fun toServiceDto(): PhoneDto {
        return PhoneDto(
            label = label,
            number = number,
            countryCode = countryCode,
            isVerified = isVerified
        )
    }
}
