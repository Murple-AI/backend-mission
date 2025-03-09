package ai.murple.wanho.code

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CountryCodeTypeTest {
    @Test
    fun `국가코드 반환 - 성공`() {
        assertEquals("KR", CountryCodeType.fromDialCode("+821012345678"))
        assertEquals("US", CountryCodeType.fromDialCode("+14155552671"))
        assertEquals("JP", CountryCodeType.fromDialCode("+819012345678"))
        assertEquals("CN", CountryCodeType.fromDialCode("+8613901234567"))
    }

    @Test
    fun `국가코드 반환 - 실패 - 매칭되는 국가 코드 없음`() {
        assertNull(CountryCodeType.fromDialCode("+999"))  // 존재하지 않는 국가 코드
        assertNull(CountryCodeType.fromDialCode(""))      // 빈 문자열
        assertNull(CountryCodeType.fromDialCode("82"))    // '+' 없는 경우
    }
}