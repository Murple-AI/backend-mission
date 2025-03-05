package com.murple.murfy.presentation.dto.response

import com.murple.murfy.domain.model.Address


data class AddressResponse(
    val id: Long,
    val label: String,
    val address: String,
) {
    companion object {
        fun from(model: Address): AddressResponse {
            return AddressResponse(
                id = model.id!!,
                label = model.label.toString(),
                address = model.address
            )
        }
    }
}
