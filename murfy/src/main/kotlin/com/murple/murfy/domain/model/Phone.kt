package com.murple.murfy.domain.model


class Phone(
    val id: Long? = null,
    val label: Label,
    val number: String,  // E.164 format
    val countryCode: String, // ISO 3166-1 alpha-2
    val isVerified: Boolean = false
) {
    init {
        require(countryCode.length == 2 && countryCode == countryCode.uppercase()) {
            "The country code must be two uppercase letters (ISO 3166-1 alpha-2)."
        }
        require(number.matches(Regex("^\\+[1-9]\\d{1,14}\$"))) {
            "The phone number must be in E.164 format (e.g., +821012345678)."
        }
    }
}
