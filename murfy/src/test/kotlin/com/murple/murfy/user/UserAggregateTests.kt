

package com.murple.murfy.user

import com.murple.murfy.domain.user.enums.Gender
import com.murple.murfy.domain.user.enums.Label
import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("사용자 도메인 모델(UserAggregate) 테스트")
class UserAggregateTest {

    @Test
    @DisplayName("유효한 필드로 사용자 생성 성공")
    fun createValidUser() {
        val user = UserAggregate(
            name = "John Doe",
            age = 30,
            gender = Gender.MALE,
            email = "john.doe@example.com"
        )

        assertEquals("John Doe", user.name)
        assertEquals(30, user.age)
        assertEquals(Gender.MALE, user.gender)
        assertEquals("john.doe@example.com", user.email)
        assertTrue(user.phones.isEmpty())
        assertTrue(user.addresses.isEmpty())
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    @DisplayName("이름이 비어있으면 예외 발생")
    fun emptyNameShouldThrowException() {
        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com"
            )
        }

        assertEquals("The name must not be empty and must be at most 1024 characters long.", exception.message)
    }

    @Test
    @DisplayName("이름이 1024자를 초과하면 예외 발생")
    fun nameTooLongShouldThrowException() {
        val longName = "a".repeat(1025)

        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = longName,
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com"
            )
        }

        assertEquals("The name must not be empty and must be at most 1024 characters long.", exception.message)
    }

    @Test
    @DisplayName("나이가 음수이면 예외 발생")
    fun negativeAgeShouldThrowException() {
        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "John Doe",
                age = -1,
                gender = Gender.MALE,
                email = "john.doe@example.com"
            )
        }

        assertEquals("Age must be at least 0 or null.", exception.message)
    }

    @Test
    @DisplayName("나이는 null 값 허용")
    fun nullAgeIsAccepted() {
        val user = UserAggregate(
            name = "John Doe",
            age = null,
            gender = Gender.MALE,
            email = "john.doe@example.com"
        )

        assertNull(user.age)
    }

    @Test
    @DisplayName("성별은 null 값 허용")
    fun nullGenderIsAccepted() {
        val user = UserAggregate(
            name = "John Doe",
            age = 30,
            gender = null,
            email = "john.doe@example.com"
        )

        assertNull(user.gender)
    }

    @Test
    @DisplayName("이메일이 1024자를 초과하면 예외 발생")
    fun emailTooLongShouldThrowException() {
        val longEmail = "a".repeat(1024) + "@example.com"

        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = longEmail
            )
        }

        assertEquals("Email must be in a valid format and at most 1024 characters long.", exception.message)
    }

    @Test
    @DisplayName("이메일 형식이 유효하지 않으면 예외 발생")
    fun invalidEmailFormatShouldThrowException() {
        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "invalid-email"
            )
        }

        assertEquals("Email must be in a valid format and at most 1024 characters long.", exception.message)
    }

    @Test
    @DisplayName("이메일은 null 값 허용")
    fun nullEmailIsAccepted() {
        val user = UserAggregate(
            name = "John Doe",
            age = 30,
            gender = Gender.MALE,
            email = null
        )

        assertNull(user.email)
    }

    @Test
    @DisplayName("전화번호가 8개를 초과하면 예외 발생")
    fun moreThan8PhonesShouldThrowException() {
        val phones = MutableList(9) {
            Phone.of(
                id = it.toLong(),
                label = Label.HOME,
                number = "+821012345678",
                countryCode = "KR",
                isVerified = false
            )
        }

        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = phones
            )
        }

        assertEquals("A maximum of 8 phone numbers can be registered.", exception.message)
    }

    @Test
    @DisplayName("주소가 8개를 초과하면 예외 발생")
    fun moreThan8AddressesShouldThrowException() {
        val addresses = MutableList(9) {
            Address(
                id = it.toLong(),
                label = Label.HOME,
                street = "123 Main St",
                city = "Seoul",
                zipCode = "12345"
            )
        }

        val exception = assertThrows<IllegalArgumentException> {
            UserAggregate(
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                addresses = addresses
            )
        }

        assertEquals("A maximum of 8 addresses can be registered.", exception.message)
    }

    @Test
    @DisplayName("전화번호 객체 생성 및 유효성 검증 성공")
    fun createAndValidatePhoneSuccessfully() {
        val phone = Phone.of(
            label = Label.HOME,
            number = "+821012345678",
            countryCode = "KR",
            isVerified = false
        )

        assertEquals(Label.HOME, phone.label)
        assertEquals("+821012345678", phone.number)
        assertEquals("KR", phone.countryCode)
        assertFalse(phone.isVerified)
    }

    @Test
    @DisplayName("전화번호가 E164 형식이 아니면 예외 발생")
    fun phoneNumberNotInE164FormatShouldThrowException() {
        val exception = assertThrows<IllegalArgumentException> {
            Phone.of(
                label = Label.HOME,
                number = "1234567890", // + 접두사 누락
                countryCode = "KR",
                isVerified = false
            )
        }

        assertEquals("Phone number must be in E.164 format (e.g., +821012345678), but got: 1234567890", exception.message)
    }

    @ParameterizedTest
    @ValueSource(strings = ["INVALID", "K", "KOR"])
    @DisplayName("국가 코드가 2자가 아니면 예외 발생")
    fun countryCodeNotTwoCharsShouldThrowException(countryCode: String) {
        val exception = assertThrows<IllegalArgumentException> {
            Phone.of(
                label = Label.HOME,
                number = "+821012345678",
                countryCode = countryCode,
                isVerified = false
            )
        }

        assertEquals("Country code must be two uppercase letters (ISO 3166-1 alpha-2), but got: $countryCode", exception.message)
    }

    @Test
    @DisplayName("국가 코드가 대문자가 아니면 예외 발생")
    fun countryCodeNotUppercaseShouldThrowException() {
        val exception = assertThrows<IllegalArgumentException> {
            Phone.of(
                label = Label.HOME,
                number = "+821012345678",
                countryCode = "kr", // 대문자여야 함
                isVerified = false
            )
        }

        assertEquals("Country code must be two uppercase letters (ISO 3166-1 alpha-2), but got: kr", exception.message)
    }

    @Test
    @DisplayName("주소 객체 생성 및 유효성 검증 성공")
    fun createAndValidateAddressSuccessfully() {
        val address = Address(
            label = Label.HOME,
            street = "123 Main St",
            city = "Seoul",
            zipCode = "12345"
        )

        assertEquals(Label.HOME, address.label)
        assertEquals("123 Main St", address.street)
        assertEquals("Seoul", address.city)
        assertEquals("12345", address.zipCode)
    }


    @Test
    @DisplayName("full 주소명이 1024자를 초과하면 예외 발생")
    fun streetTooLongShouldThrowException() {
        val longStreet = "a".repeat(1025)

        val exception = assertThrows<IllegalArgumentException> {
            Address(
                label = Label.HOME,
                street = longStreet,
                city = "Seoul",
                zipCode = "12345"
            )
        }

        assertEquals("The full address must be at most 1024 characters long.", exception.message)
    }

}
