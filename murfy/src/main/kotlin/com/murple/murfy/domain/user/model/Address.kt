package com.murple.murfy.domain.user.model

import com.murple.murfy.domain.user.enums.Label

class Address(
    val id: Long? = null,
    val label: Label,
    val street: String,
    val city: String,
    val zipCode: String
) {
    init {
        val fullAddress = "$street, $city, $zipCode"
        require(fullAddress.length <= 1024) {
            "The full address must be at most 1024 characters long."
        }
    }

    fun getFullAddress(): String {
        return "$street, $city, $zipCode"
    }
}
