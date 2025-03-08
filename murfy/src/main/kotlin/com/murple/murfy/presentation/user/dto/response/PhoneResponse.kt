package com.murple.murfy.presentation.user.dto.response

import com.murple.murfy.domain.user.model.Phone
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "전화번호 응답 정보")
data class PhoneResponse(
    @Schema(description = "전화번호 ID", example = "1")
    val id: Long,

    @Schema(description = "전화번호 라벨", example = "HOME")
    val label: String,

    @Schema(description = "전화번호", example = "+821012345678")
    val number: String,

    @Schema(description = "국가 코드", example = "KR")
    val countryCode: String,

    @Schema(description = "인증 여부", example = "true")
    val isVerified: Boolean,
) {
    companion object {
        fun from(model: Phone): PhoneResponse {
            return PhoneResponse(
                id = model.id!!,
                label = model.label.toString(),
                number = model.number,
                countryCode = model.countryCode,
                isVerified = model.isVerified,
            )
        }
    }
}
