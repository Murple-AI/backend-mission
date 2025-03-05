package com.murple.murfy.application.service


import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.springframework.stereotype.Service


@Service
class PhoneNumberService {
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    fun extractCountryCode(phoneNumber: String): String? {
        return try {
            val number = phoneNumberUtil.parse(phoneNumber, null)
            val regionCode = phoneNumberUtil.getRegionCodeForNumber(number) ?: return null

            if (regionCode in listOf("KR", "US", "JP", "CN")) {
                regionCode
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun validatePhoneNumber(phoneNumber: String): Boolean {
        return try {
            val number = phoneNumberUtil.parse(phoneNumber, null)
            phoneNumberUtil.isValidNumber(number)
        } catch (e: Exception) {
            false
        }
    }
}
