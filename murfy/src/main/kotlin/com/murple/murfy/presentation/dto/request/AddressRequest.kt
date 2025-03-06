package com.murple.murfy.presentation.dto.request

import com.murple.murfy.application.dto.AddressDto


data class AddressRequest(
    val label: String,
    val street: String,
    val city: String,
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
