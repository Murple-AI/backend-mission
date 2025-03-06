package com.murple.murfy.domain.user.model


import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.murple.murfy.domain.user.enums.Label

class Phone(
    val id: Long? = null,
    val label: Label,
    val number: String,  // E.164 format
    val countryCode: String, // ISO 3166-1 alpha-2
    val isVerified: Boolean = false
) {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    init {
        require(countryCode.length == 2 && countryCode == countryCode.uppercase()) {
            "The country code must be two uppercase letters (ISO 3166-1 alpha-2)."
        }
        require(number.matches(Regex("^\\+[1-9]\\d{1,14}\$"))) {
            "The phone number must be in E.164 format (e.g., +821012345678)."
        }

        // 전화번호와 국가코드 일치 여부 검증
        validatePhoneNumberMatchesCountryCode(number, countryCode)
    }

    private fun validatePhoneNumberMatchesCountryCode(number: String, countryCode: String) {
        try {
            val phoneNumber = phoneNumberUtil.parse(number, null)
            val regionCode = phoneNumberUtil.getRegionCodeForNumber(phoneNumber)

            require(regionCode == countryCode) {
                "Phone number $number belongs to region $regionCode, which doesn't match the provided country code $countryCode"
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to validate phone number against country code: ${e.message}")
        }
    }
}
