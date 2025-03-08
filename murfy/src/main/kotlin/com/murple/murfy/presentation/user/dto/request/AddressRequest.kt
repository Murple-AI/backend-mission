package com.murple.murfy.presentation.user.dto.request

import com.murple.murfy.application.user.dto.AddressDto
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "주소 요청 정보")
data class AddressRequest(
    @Schema(description = "주소 라벨", example = "home", required = true)
    val label: String,

    @Schema(description = "도로명 주소", example = "테헤란로 152", required = true)
    val street: String,

    @Schema(description = "도시", example = "서울시", required = true)
    val city: String,

    @Schema(description = "우편번호", example = "06236", required = true)
    val zipcode: String
) {
    fun toServiceDto(): AddressDto {
        return AddressDto(
            label = label,
            street = street,
            city = city,
            zipcode = zipcode
        )
    }
}
