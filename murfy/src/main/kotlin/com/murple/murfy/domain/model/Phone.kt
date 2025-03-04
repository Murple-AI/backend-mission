package com.murple.murfy.domain.model


class Phone(
    val id: Long? = null,
    val label: Label,
    val number: String,  // E.164 형식
    val countryCode: String, // ISO 3166-1 alpha-2
    val isVerified: Boolean = false
) {
    init {
        require(countryCode.length == 2 && countryCode == countryCode.uppercase()) {
            "국가 코드는 2자리 대문자여야 합니다 (ISO 3166-1 alpha-2)"
        }
        require(number.matches(Regex("^\\+[1-9]\\d{1,14}\$"))) {
            "전화번호는 E.164 형식이어야 합니다 (예: +821012345678)"
        }
    }
}
