package com.murple.murfy.domain.user.model


import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.murple.murfy.domain.user.enums.Label


class Phone private constructor(
    val id: Long? = null,
    val label: Label,
    val number: String,  // E.164 format
    val countryCode: String, // ISO 3166-1 alpha-2
    val isVerified: Boolean = false
) {

    init {
        require(countryCode.length == 2 && countryCode == countryCode.uppercase()) {
            "Country code must be two uppercase letters (ISO 3166-1 alpha-2), but got: $countryCode"
        }
        require(number.matches(Regex("^\\+[1-9]\\d{1,14}\$"))) {
            "Phone number must be in E.164 format (e.g., +821012345678), but got: $number"
        }
        validatePhoneNumberMatchesCountryCode(number, countryCode)
    }


    private fun validatePhoneNumberMatchesCountryCode(number: String, countryCode: String) {
        try {
            val phoneNumber = phoneNumberUtil.parse(number, "ZZ")  // "ZZ"는 알 수 없는 국가 코드 의미
            val regionCode = phoneNumberUtil.getRegionCodeForNumber(phoneNumber)

            require(regionCode == countryCode) {
                "Phone number $number belongs to region $regionCode, which doesn't match the provided country code $countryCode"
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to validate phone number against country code: ${e.message}", e)
        }
    }

    companion object {
        private val phoneNumberUtil = PhoneNumberUtil.getInstance()

        fun of(
            id: Long? = null,
            label: Label,
            number: String,
            countryCode: String? = null,
            isVerified: Boolean = false
        ): Phone {
            val resolvedCountryCode = countryCode ?: extractCountryCode(number)
            ?: throw IllegalArgumentException("A valid country code must be provided or extracted from the phone number.")

            return Phone(
                id = id,
                label = label,
                number = number,
                countryCode = resolvedCountryCode,
                isVerified = isVerified
            )
        }

        private fun extractCountryCode(phoneNumber: String): String? {
            return try {
                val number = phoneNumberUtil.parse(phoneNumber, "ZZ")
                phoneNumberUtil.getRegionCodeForNumber(number)
            } catch (e: Exception) {
                null
            }
        }
    }
}
