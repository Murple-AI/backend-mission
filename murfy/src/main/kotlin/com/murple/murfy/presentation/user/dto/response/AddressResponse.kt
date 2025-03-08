package com.murple.murfy.presentation.user.dto.response

import com.murple.murfy.domain.user.model.Address
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "주소 응답 정보")
data class AddressResponse(
    @Schema(description = "주소 ID", example = "1")
    val id: Long,

    @Schema(description = "주소 라벨", example = "HOME")
    val label: String,

    @Schema(description = "도로명 주소", example = "테헤란로 152")
    val street: String,

    @Schema(description = "도시", example = "서울시")
    val city: String,

    @Schema(description = "우편번호", example = "06236")
    val zipcode: String
) {
    companion object {
        fun from(model: Address): AddressResponse {
            return AddressResponse(
                id = model.id!!,
                label = model.label.toString(),
                street = model.street,
                city = model.city,
                zipcode = model.zipCode
            )
        }
    }
}

