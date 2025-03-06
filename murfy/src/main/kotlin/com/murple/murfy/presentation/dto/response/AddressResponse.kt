package com.murple.murfy.presentation.dto.response

import com.murple.murfy.domain.model.Address


data class AddressResponse(
    val id: Long,
    val label: String,
    val street: String,
    val city: String,
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
