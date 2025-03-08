package com.murple.murfy.user

import com.murple.murfy.application.user.dto.AddressDto
import com.murple.murfy.application.user.dto.PhoneDto
import com.murple.murfy.application.user.dto.UserDto
import com.murple.murfy.application.user.service.UserService
import com.murple.murfy.domain.user.enums.Gender
import com.murple.murfy.domain.user.enums.Label
import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic
import com.murple.murfy.domain.user.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Nested
    @DisplayName("사용자 생성 테스트")
    inner class CreateUserTests {

        @Test
        @DisplayName("유효한 정보로 사용자를 생성해야 함")
        fun shouldCreateUserWithValidInformation() {
            // Given
            val phoneDto = PhoneDto(
                label = "HOME",
                number = "+821012345678",
                countryCode = "KR",
                isVerified = false
            )

            val addressDto = AddressDto(
                label = "HOME",
                street = "123 Main St",
                city = "Seoul",
                zipcode = "12345"
            )

            val userDto = UserDto(
                name = "John Doe",
                age = 30,
                gender = "MALE",
                email = "john.doe@example.com",
                phones = listOf(phoneDto),
                addresses = listOf(addressDto)
            )

            val expectedPhone = Phone.of(
                label = Label.HOME,
                number = "+821012345678",
                countryCode = "KR",
                isVerified = false
            )

            val expectedAddress = Address(
                label = Label.HOME,
                street = "123 Main St",
                city = "Seoul",
                zipCode = "12345"
            )

            val expectedUserAggregate = UserAggregate(
                id = 1L,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = mutableListOf(expectedPhone),
                addresses = mutableListOf(expectedAddress)
            )

            every { userRepository.save(any()) } returns expectedUserAggregate

            // When
            val result = userService.createUser(userDto)

            // Then
            verify(exactly = 1) { userRepository.save(any()) }
            assertEquals(expectedUserAggregate.id, result.id)
            assertEquals(expectedUserAggregate.name, result.name)
            assertEquals(expectedUserAggregate.age, result.age)
            assertEquals(expectedUserAggregate.gender, result.gender)
            assertEquals(expectedUserAggregate.email, result.email)
            assertEquals(1, result.phones.size)
            assertEquals(expectedUserAggregate.phones.first().number, result.phones.first().number)
            assertEquals(1, result.addresses.size)
            assertEquals(expectedUserAggregate.addresses.first().street, result.addresses.first().street)
        }

        @Test
        @DisplayName("전화번호와 주소 없이 사용자를 생성해야 함")
        fun shouldCreateUserWithoutPhonesAndAddresses() {
            // Given
            val userDto = UserDto(
                name = "Jane Doe",
                age = 25,
                gender = "FEMALE",
                email = "jane.doe@example.com",
                phones = null,
                addresses = null
            )

            val expectedUserAggregate = UserAggregate(
                id = 2L,
                name = "Jane Doe",
                age = 25,
                gender = Gender.FEMALE,
                email = "jane.doe@example.com",
                phones = mutableListOf(),
                addresses = mutableListOf()
            )

            every { userRepository.save(any()) } returns expectedUserAggregate

            // When
            val result = userService.createUser(userDto)

            // Then
            verify(exactly = 1) { userRepository.save(any()) }
            assertEquals(expectedUserAggregate.id, result.id)
            assertEquals(expectedUserAggregate.name, result.name)
            assertTrue(result.phones.isEmpty())
            assertTrue(result.addresses.isEmpty())
        }
    }

    @Nested
    @DisplayName("사용자 조회 테스트")
    inner class GetUserTests {

        @Test
        @DisplayName("사용자가 존재할 때 ID로 사용자를 조회해야 함")
        fun shouldGetUserByIdWhenUserExists() {
            // Given
            val userId = 1L
            val expectedUser = UserAggregate(
                id = userId,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = mutableListOf(),
                addresses = mutableListOf()
            )

            every { userRepository.findById(userId) } returns expectedUser

            // When
            val result = userService.getUserById(userId)

            // Then
            verify(exactly = 1) { userRepository.findById(userId) }
            assertEquals(expectedUser.id, result.id)
            assertEquals(expectedUser.name, result.name)
        }

        @Test
        @DisplayName("사용자가 존재하지 않을 때 예외를 발생시켜야 함")
        fun shouldThrowExceptionWhenUserDoesNotExist() {
            // Given
            val userId = 999L
            every { userRepository.findById(userId) } returns null

            // When, Then
            val exception = assertThrows<NoSuchElementException> {
                userService.getUserById(userId)
            }
            assertEquals("User with ID $userId not found.", exception.message)
            verify(exactly = 1) { userRepository.findById(userId) }
        }

        @Test
        @DisplayName("이름으로 상위 사용자를 조회해야 함")
        fun shouldFindTopUsersByName() {
            // Given
            val name = "John"
            val expectedUsers = listOf(
                UserBasic(id = 1L, name = "John", age = 30, gender = Gender.MALE, email = "john.doe@example.com"),
                UserBasic(id = 2L, name = "John", age = 25, gender = Gender.MALE, email = "john.smith@example.com")
            )

            every { userRepository.findTop5ByNameOrderByCreatedAtAsc(name) } returns expectedUsers

            // When
            val result = userService.findTopUsersByName(name)

            // Then
            verify(exactly = 1) { userRepository.findTop5ByNameOrderByCreatedAtAsc(name) }
            assertEquals(2, result.size)
            assertEquals("John", result[0].name)
            assertEquals("John", result[1].name)
        }

        @Test
        @DisplayName("이름 목록으로 사용자를 조회해야 함")
        fun shouldFindUsersByNameList() {
            // Given
            val names = listOf("John", "Jane")
            val expectedUsers = listOf(
                UserBasic(id = 1L, name = "John", age = 30, gender = Gender.MALE, email = "john.doe@example.com"),
            )

            every { userRepository.findByNamesLimitedByCreatedAt(names) } returns expectedUsers

            // When
            val result = userService.findTopUsersByNameList(names)

            // Then
            verify(exactly = 1) { userRepository.findByNamesLimitedByCreatedAt(names) }
            assertEquals(1, result.size)
            assertEquals("John", result[0].name)
        }
    }

    @Nested
    @DisplayName("사용자 정보 수정 테스트")
    inner class UpdateUserTests {

        @Test
        @DisplayName("사용자 기본 정보를 수정해야 함")
        fun shouldUpdateUserBasicInfo() {
            // Given
            val userId = 1L
            val userDto = UserDto(
                name = "John Updated",
                age = 31,
                gender = "MALE",
                email = "john.updated@example.com",
                phones = null,
                addresses = null
            )

            val expectedUserBasic = UserBasic(
                id = userId,
                name = "John Updated",
                age = 31,
                gender = Gender.MALE,
                email = "john.updated@example.com"
            )

            every { userRepository.updateUserBasicInfo(any()) } returns expectedUserBasic

            // When
            val result = userService.updateUserInfo(userId, userDto)

            // Then
            verify(exactly = 1) { userRepository.updateUserBasicInfo(any()) }
            assertEquals(expectedUserBasic.id, result.id)
            assertEquals(expectedUserBasic.name, result.name)
            assertEquals(expectedUserBasic.age, result.age)
            assertEquals(expectedUserBasic.email, result.email)
        }

        @Test
        @DisplayName("사용자 전화번호를 수정해야 함")
        fun shouldUpdateUserPhone() {
            // Given
            val userId = 1L
            val phoneId = 1L
            val phoneDto = PhoneDto(
                label = "WORK",
                number = "+821098765432",
                countryCode = "KR",
                isVerified = true
            )

            val expectedPhone = Phone.of(
                id = phoneId,
                label = Label.WORK,
                number = "+821098765432",
                countryCode = "KR",
                isVerified = true
            )

            every { userRepository.updateUserPhone(userId, any()) } returns expectedPhone

            // When
            val result = userService.updateUserPhone(userId, phoneId, phoneDto)

            // Then
            verify(exactly = 1) { userRepository.updateUserPhone(userId, any()) }
            assertEquals(expectedPhone.id, result.id)
            assertEquals(expectedPhone.label, result.label)
            assertEquals(expectedPhone.number, result.number)
            assertEquals(expectedPhone.countryCode, result.countryCode)
            assertEquals(expectedPhone.isVerified, result.isVerified)
        }

        @Test
        @DisplayName("사용자 주소를 수정해야 함")
        fun shouldUpdateUserAddress() {
            // Given
            val userId = 1L
            val addressId = 1L
            val addressDto = AddressDto(
                label = "WORK",
                street = "456 Business Ave",
                city = "Seoul",
                zipcode = "54321"
            )

            val expectedAddress = Address(
                id = addressId,
                label = Label.WORK,
                street = "456 Business Ave",
                city = "Seoul",
                zipCode = "54321"
            )

            every { userRepository.updateUserAddress(userId, any()) } returns expectedAddress

            // When
            val result = userService.updateUserAddress(userId, addressId, addressDto)

            // Then
            verify(exactly = 1) { userRepository.updateUserAddress(userId, any()) }
            assertEquals(expectedAddress.id, result.id)
            assertEquals(expectedAddress.label, result.label)
            assertEquals(expectedAddress.street, result.street)
            assertEquals(expectedAddress.city, result.city)
            assertEquals(expectedAddress.zipCode, result.zipCode)
        }
    }

    @Nested
    @DisplayName("전화번호 관리 테스트")
    inner class PhoneManagementTests {

        @Test
        @DisplayName("사용자에게 전화번호를 추가해야 함")
        fun shouldAddPhoneToUser() {
            // Given
            val userId = 1L
            val phoneDto = PhoneDto(
                label = "home",
                number = "+821055556666",
                countryCode = "KR",
                isVerified = false
            )

            val expectedPhone = Phone.of(
                id = 1L,
                label = Label.HOME,
                number = "+821055556666",
                countryCode = "KR",
                isVerified = false
            )

            every { userRepository.addNewPhone(userId, any()) } returns expectedPhone

            // When
            val result = userService.addUserPhone(userId, phoneDto)

            // Then
            verify(exactly = 1) { userRepository.addNewPhone(userId, any()) }
            assertEquals(expectedPhone.id, result.id)
            assertEquals(expectedPhone.label, result.label)
            assertEquals(expectedPhone.number, result.number)
        }

        @Test
        @DisplayName("사용자의 전화번호를 삭제해야 함")
        fun shouldDeletePhoneFromUser() {
            // Given
            val userId = 1L
            val phoneId = 1L
            every { userRepository.deleteUserPhone(userId, phoneId) } just runs

            // When
            userService.deleteUserPhone(userId, phoneId)

            // Then
            verify(exactly = 1) { userRepository.deleteUserPhone(userId, phoneId) }
        }
    }

    @Nested
    @DisplayName("주소 관리 테스트")
    inner class AddressManagementTests {

        @Test
        @DisplayName("사용자에게 주소를 추가해야 함")
        fun shouldAddAddressToUser() {
            // Given
            val userId = 1L
            val addressDto = AddressDto(
                label = "home",
                street = "789 Beach Rd",
                city = "Busan",
                zipcode = "67890"
            )

            val expectedAddress = Address(
                id = 1L,
                label = Label.HOME,
                street = "789 Beach Rd",
                city = "Busan",
                zipCode = "67890"
            )

            every { userRepository.addNewAddress(userId, any()) } returns expectedAddress

            // When
            val result = userService.addUserAddress(userId, addressDto)

            // Then
            verify(exactly = 1) { userRepository.addNewAddress(userId, any()) }
            assertEquals(expectedAddress.id, result.id)
            assertEquals(expectedAddress.label, result.label)
            assertEquals(expectedAddress.street, result.street)
            assertEquals(expectedAddress.city, result.city)
        }

        @Test
        @DisplayName("사용자의 주소를 삭제해야 함")
        fun shouldDeleteAddressFromUser() {
            // Given
            val userId = 1L
            val addressId = 1L
            every { userRepository.deleteUserAddress(userId, addressId) } just runs

            // When
            userService.deleteUserAddress(userId, addressId)

            // Then
            verify(exactly = 1) { userRepository.deleteUserAddress(userId, addressId) }
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    inner class DeleteUserTests {

        @Test
        @DisplayName("ID로 사용자를 삭제해야 함")
        fun shouldDeleteUserById() {
            // Given
            val userId = 1L
            every { userRepository.delete(userId) } just runs

            // When
            userService.deleteUser(userId)

            // Then
            verify(exactly = 1) { userRepository.delete(userId) }
        }
    }
}
