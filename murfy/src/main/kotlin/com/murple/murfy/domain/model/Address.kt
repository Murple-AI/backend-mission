package com.murple.murfy.domain.model

class Address(
    val id: Long? = null,
    val label: Label,
    val address: String
) {
    init {
        require(address.isNotBlank() && address.length <= 1024) {
            "The address must not be empty and must be at most 1024 characters long."
        }
    }
}
