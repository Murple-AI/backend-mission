package ai.murple.wanho.code


/**
 * 전화번호 국가 코드 ENUM
 *
 *
 */
enum class CountryCodeType(val dialCode: String, val isoCode: String)  {
    KOREA("+82", "KR"),
    USA("+1", "US"),
    JAPAN("+81", "JP"),
    CHINA("+86", "CN");

    companion object {
        fun fromDialCode(dialCode: String): String? =
            entries.find { dialCode.startsWith(it.dialCode) }?.isoCode
    }
}