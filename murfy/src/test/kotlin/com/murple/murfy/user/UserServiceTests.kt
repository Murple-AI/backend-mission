

package com.murple.murfy.user

import com.murple.murfy.application.user.dto.AddressDto
import com.murple.murfy.application.user.dto.PhoneDto
import com.murple.murfy.application.user.dto.UserDto
import com.murple.murfy.application.user.service.PhoneNumberService
import com.murple.murfy.application.user.service.UserService
import com.murple.murfy.domain.user.enums.Gender
import com.murple.murfy.domain.user.enums.Label
import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic
import com.murple.murfy.domain.user.repository.UserRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.ZonedDateTime

@DisplayName("사용자 서비스 테스트")
class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var phoneNumberService: PhoneNumberService
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        userRepository = mockk(relaxed = true)
        phoneNumberService = mockk(relaxed = true)
        userService = UserService(userRepository, phoneNumberService)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Nested
    @DisplayName("사용자 생성 테스트")
    inner class CreateUserTest {
        @Test
        @DisplayName("DTO를 애그리게이트로 매핑하고 저장해야 함")
        fun createUserShouldMapDtoToAggregateAndSaveIt() {
            // Given
            val userDto = UserDto(
                name = "John Doe",
                age = 30,
                gender = "MALE",
                email = "john.doe@example.com",
                phones = listOf(
                    PhoneDto(
                        label = "HOME",
                        number = "+821012345678",
                        countryCode = "KR",
                        isVerified = false
                    )
                ),
                addresses = listOf(
                    AddressDto(
                        label = "HOME",
                        street = "123 Main St",
                        city = "New York",
                        zipcode = "10001"
                    )
                )
            )

            val savedUser = UserAggregate(
                id = 1L,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = mutableListOf(
                    Phone(
                        id = 1L,
                        label = Label.HOME,
                        number = "+821012345678",
                        countryCode = "KR",
                        isVerified = false
                    )
                ),
                addresses = mutableListOf(
                    Address(
                        id = 1L,
                        label = Label.HOME,
                        street = "123 Main St",
                        city = "New York",
                        zipCode = "10001"
                    )
                ),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val userSlot = slot<UserAggregate>()
            every { userRepository.save(capture(userSlot)) } returns savedUser
            every { phoneNumberService.extractCountryCode(any()) } returns "1"

            // When
            val result = userService.createUser(userDto)

            // Then
            verify { userRepository.save(any()) }
            assertEquals("John Doe", userSlot.captured.name)
            assertEquals(30, userSlot.captured.age)
            assertEquals(Gender.MALE, userSlot.captured.gender)
            assertEquals("john.doe@example.com", userSlot.captured.email)
            assertEquals(1, userSlot.captured.phones.size)
            assertEquals(1, userSlot.captured.addresses.size)
            assertEquals(savedUser, result)
        }


        @Test
        @DisplayName("국가 코드가 제공되지 않을 때 번호에서 추출해야 함")
        fun createUserShouldExtractCountryCodeWhenNotProvided() {
            // Given
            val userDto = UserDto(
                name = "John Doe",
                age = 30,
                gender = "MALE",
                email = "john.doe@example.com",
                phones = listOf(
                    PhoneDto(
                        label = "HOME",
                        number = "+821012345678",
                        countryCode = null,
                        isVerified = false
                    )
                )
            )

            val savedUser = UserAggregate(
                id = 1L,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                phones = mutableListOf(
                    Phone(
                        id = 1L,
                        label = Label.HOME,
                        number = "+821012345678",
                        countryCode = "KR",
                        isVerified = false
                    )
                ),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            every { userRepository.save(any()) } returns savedUser
            every { phoneNumberService.extractCountryCode("+821012345678") } returns "KR"

            // When
            val result = userService.createUser(userDto)

            // Then
            verify { phoneNumberService.extractCountryCode("+821012345678") }
            verify { userRepository.save(any()) }
            assertEquals(savedUser, result)
        }

        @Test
        @DisplayName("국가 코드를 추출할 수 없을 때 예외가 발생해야 함")
        fun createUserShouldThrowExceptionWhenCountryCodeCannotBeExtracted() {
            // Given
            val userDto = UserDto(
                name = "John Doe",
                age = 30,
                gender = "MALE",
                email = "john.doe@example.com",
                phones = listOf(
                    PhoneDto(
                        label = "HOME",
                        number = "1234567890",
                        countryCode = null,
                        isVerified = false
                    )
                )
            )

            every { phoneNumberService.extractCountryCode(any()) } returns null

            // When & Then
            assertThrows<IllegalArgumentException> {
                userService.createUser(userDto)
            }
        }
    }

    @Nested
    @DisplayName("사용자 조회 테스트")
    inner class GetUserTest {
        @Test
        @DisplayName("사용자가 존재할 때 사용자를 반환해야 함")
        fun getUserByIdShouldReturnUserWhenExists() {
            // Given
            val userId = 1L
            val user = UserAggregate(
                id = userId,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            every { userRepository.findById(userId) } returns user

            // When
            val result = userService.getUserById(userId)

            // Then
            verify { userRepository.findById(userId) }
            assertEquals(user, result)
        }

        @Test
        @DisplayName("사용자가 존재하지 않을 때 예외가 발생해야 함")
        fun getUserByIdShouldThrowExceptionWhenUserNotFound() {
            // Given
            val userId = 999L
            every { userRepository.findById(userId) } returns null

            // When & Then
            assertThrows<NoSuchElementException> {
                userService.getUserById(userId)
            }
        }
    }

    @Nested
    @DisplayName("사용자 정보 업데이트 테스트")
    inner class UpdateUserInfoTest {
        @Test
        @DisplayName("기본 사용자 정보를 업데이트해야 함")
        fun updateUserInfoShouldUpdateBasicUserInformation() {
            // Given
            val userId = 1L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                age = 30,
                gender = Gender.MALE,
                email = "john.doe@example.com",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val updatedUserDto = UserDto(
                name = "Jane Doe",
                age = 32,
                gender = "FEMALE",
                email = "jane.doe@example.com"
            )

            val updatedUser = existingUser.copy(
                name = "Jane Doe",
                age = 32,
                gender = Gender.FEMALE,
                email = "jane.doe@example.com"
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.updateUserInfo(userId, updatedUserDto)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals("Jane Doe", result.name)
            assertEquals(32, result.age)
            assertEquals(Gender.FEMALE, result.gender)
            assertEquals("jane.doe@example.com", result.email)
        }
    }

    @Nested
    @DisplayName("사용자 전화번호 관리 테스트")
    inner class UserPhoneManagementTest {
        @Test
        @DisplayName("전화번호가 존재할 때 업데이트해야 함")
        fun updateUserPhoneShouldUpdatePhoneWhenExists() {
            // Given
            val userId = 1L
            val phoneId = 5L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                phones = mutableListOf(
                    Phone(id = phoneId, label = Label.HOME, number = "+821012345678", countryCode = "KR", isVerified = false)
                ),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val phoneDto = PhoneDto(
                label = "WORK",
                number = "+14155552671",
                countryCode = "US",
                isVerified = true
            )

            val updatedPhone = Phone(
                id = phoneId,
                label = Label.WORK,
                number = "+14155552671",
                countryCode = "US",
                isVerified = true
            )

            val updatedUser = existingUser.copy(
                phones = mutableListOf(updatedPhone)
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.updateUserPhone(userId, phoneId, phoneDto)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(Label.WORK, result.label)
            assertEquals("+14155552671", result.number)
            assertEquals("US", result.countryCode)
            assertEquals(true, result.isVerified)
        }

        @Test
        @DisplayName("사용자에게 전화번호를 추가해야 함")
        fun addUserPhoneShouldAddPhoneToExistingUser() {
            // Given
            val userId = 1L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                phones = mutableListOf(),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val phoneDto = PhoneDto(
                label = "HOME",
                number = "+14155552671",
                countryCode = "US",
                isVerified = false
            )

            val newPhone = Phone(
                id = 5L,
                label = Label.HOME,
                number = "+14155552671",
                countryCode = "US",
                isVerified = false
            )

            val updatedUser = existingUser.copy(
                phones = mutableListOf(newPhone)
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.addUserPhone(userId, phoneDto)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(Label.HOME, result.label)
            assertEquals("+14155552671", result.number)
            assertEquals("US", result.countryCode)
            assertEquals(false, result.isVerified)
        }

        @Test
        @DisplayName("사용자에게서 전화번호를 제거해야 함")
        fun deleteUserPhoneShouldRemovePhoneFromExistingUser() {
            // Given
            val userId = 1L
            val phoneId = 5L
            val phoneToDelete = Phone(
                id = phoneId,
                label = Label.HOME,
                number = "+14155552671",
                countryCode = "US",
                isVerified = false
            )

            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                phones = mutableListOf(phoneToDelete),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val updatedUser = existingUser.copy(
                phones = mutableListOf()
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.deleteUserPhone(userId, phoneId)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(phoneToDelete, result)
        }
    }

    @Nested
    @DisplayName("사용자 주소 관리 테스트")
    inner class UserAddressManagementTest {
        @Test
        @DisplayName("주소가 존재할 때 업데이트해야 함")
        fun updateUserAddressShouldUpdateAddressWhenExists() {
            // Given
            val userId = 1L
            val addressId = 5L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                addresses = mutableListOf(
                    Address(id = addressId, label = Label.HOME, street = "123 Main St", city = "New York", zipCode = "10001")
                ),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val addressDto = AddressDto(
                label = "WORK",
                street = "456 Office Blvd",
                city = "San Francisco",
                zipcode = "94107"
            )

            val updatedAddress = Address(
                id = addressId,
                label = Label.WORK,
                street = "456 Office Blvd",
                city = "San Francisco",
                zipCode = "94107"
            )

            val updatedUser = existingUser.copy(
                addresses = mutableListOf(updatedAddress)
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.updateUserAddress(userId, addressId, addressDto)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(Label.WORK, result.label)
            assertEquals("456 Office Blvd", result.street)
            assertEquals("San Francisco", result.city)
            assertEquals("94107", result.zipCode)
        }

        @Test
        @DisplayName("사용자에게 주소를 추가해야 함")
        fun addUserAddressShouldAddAddressToExistingUser() {
            // Given
            val userId = 1L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                addresses = mutableListOf(),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val addressDto = AddressDto(
                label = "HOME",
                street = "123 Main St",
                city = "New York",
                zipcode = "10001"
            )

            val newAddress = Address(
                id = 5L,
                label = Label.HOME,
                street = "123 Main St",
                city = "New York",
                zipCode = "10001"
            )

            val updatedUser = existingUser.copy(
                addresses = mutableListOf(newAddress)
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.addUserAddress(userId, addressDto)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(Label.HOME, result.label)
            assertEquals("123 Main St", result.street)
            assertEquals("New York", result.city)
            assertEquals("10001", result.zipCode)
        }

        @Test
        @DisplayName("사용자에게서 주소를 제거해야 함")
        fun deleteUserAddressShouldRemoveAddressFromExistingUser() {
            // Given
            val userId = 1L
            val addressId = 5L
            val addressToDelete = Address(
                id = addressId,
                label = Label.HOME,
                street = "123 Main St",
                city = "New York",
                zipCode = "10001"
            )

            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                addresses = mutableListOf(addressToDelete),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            val updatedUser = existingUser.copy(
                addresses = mutableListOf()
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(any()) } returns updatedUser

            // When
            val result = userService.deleteUserAddress(userId, addressId)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.save(any()) }
            assertEquals(addressToDelete, result)
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    inner class DeleteUserTest {
        @Test
        @DisplayName("사용자가 존재할 때 삭제해야 함")
        fun deleteUserShouldDeleteUserWhenExists() {
            // Given
            val userId = 1L
            val existingUser = UserAggregate(
                id = userId,
                name = "John Doe",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )

            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.delete(userId) } returns Unit

            // When
            userService.deleteUser(userId)

            // Then
            verify { userRepository.findById(userId) }
            verify { userRepository.delete(userId) }
        }

        @Test
        @DisplayName("사용자가 존재하지 않을 때 예외가 발생해야 함")
        fun deleteUserShouldThrowExceptionWhenUserNotFound() {
            // Given
            val userId = 999L
            every { userRepository.findById(userId) } returns null

            // When & Then
            assertThrows<NoSuchElementException> {
                userService.deleteUser(userId)
            }
        }
    }

    @Nested
    @DisplayName("사용자 검색 테스트")
    inner class FindUsersTest {
        @Test
        @DisplayName("이름으로 필터링된 사용자를 반환해야 함")
        fun findTopUsersByNameShouldReturnUsersFilteredByName() {
            // Given
            val name = "John"
            val users = listOf(
                UserBasic(id = 1L, name = "John Doe", createdAt = ZonedDateTime.now(), updatedAt = ZonedDateTime.now()),
                UserBasic(id = 2L, name = "John Smith", createdAt = ZonedDateTime.now(), updatedAt = ZonedDateTime.now())
            )

            every { userRepository.findTop5ByNameOrderByCreatedAtAsc(name) } returns users

            // When
            val result = userService.findTopUsersByName(name)

            // Then
            verify { userRepository.findTop5ByNameOrderByCreatedAtAsc(name) }
            assertEquals(2, result.size)
            assertEquals("John Doe", result[0].name)
            assertEquals("John Smith", result[1].name)
        }

        @Test
        @DisplayName("이름 목록으로 필터링된 사용자를 반환해야 함")
        fun findTopUsersByNameListShouldReturnUsersFilteredByNameList() {
            // Given
            val names = listOf("John", "Jane")
            val users = listOf(
                UserBasic(id = 1L, name = "John Doe", createdAt = ZonedDateTime.now(), updatedAt = ZonedDateTime.now()),
                UserBasic(id = 2L, name = "Jane Smith", createdAt = ZonedDateTime.now(), updatedAt = ZonedDateTime.now())
            )

            every { userRepository.findByNamesLimitedByCreatedAt(names) } returns users

            // When
            val result = userService.findTopUsersByNameList(names)

            // Then
            verify { userRepository.findByNamesLimitedByCreatedAt(names) }
            assertEquals(2, result.size)
            assertEquals("John Doe", result[0].name)
            assertEquals("Jane Smith", result[1].name)
        }
    }
}
