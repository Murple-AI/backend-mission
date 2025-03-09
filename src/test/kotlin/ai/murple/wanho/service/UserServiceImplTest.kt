package ai.murple.wanho.service

import ai.murple.wanho.code.CountryCodeType
import ai.murple.wanho.code.ErrorType
import ai.murple.wanho.code.SexType
import ai.murple.wanho.code.UserStatusType
import ai.murple.wanho.dto.UserAddrDto
import ai.murple.wanho.dto.UserInfoDto
import ai.murple.wanho.dto.UserTelDto
import ai.murple.wanho.entity.UserAddrEntity
import ai.murple.wanho.entity.UserInfoEntity
import ai.murple.wanho.entity.UserTelEntity
import ai.murple.wanho.exception.CustomException
import ai.murple.wanho.repository.UserAddrRepository
import ai.murple.wanho.repository.UserInfoRepository
import ai.murple.wanho.repository.UserTelRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.time.Instant

@ExtendWith(MockKExtension::class)
class UserServiceImplTest {

    @MockK
    lateinit var userInfoRepository: UserInfoRepository

    @MockK
    lateinit var userAddrRepository: UserAddrRepository

    @MockK
    lateinit var userTelRepository: UserTelRepository

    @InjectMockKs
    lateinit var userService: UserServiceImpl

    private val userIdx = 1L
    private val now = Instant.now()

    private val userInfoDto = UserInfoDto(
        idx = userIdx,
        name = "이완호",
        age = 35,
        sex = SexType.M,
        email = "test@test.com",
        status = UserStatusType.ACTIVE,
        createdAt = now,
        updatedAt = now
    )

    private val userInfoEntity = userInfoDto.toEntity()

    private val userAddrDtoList = listOf(
        UserAddrDto(addr = "서울시 송파구", label = "집"),
        UserAddrDto(addr = "경기도 성남시", label = "회사")
    )

    private val userTelDtoList = listOf(
        UserTelDto(tel = "+821012345678", label = "휴대폰", countryCode = CountryCodeType.KOREA.isoCode, isIdentity = "Y")
    )

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkObject(CountryCodeType)
    }


    @Test
    fun `유저 생성 - 성공`() {
        // Given
        every { userInfoRepository.save(any<UserInfoEntity>()) } returns userInfoEntity
        every { userTelRepository.saveAll(any<List<UserTelEntity>>()) } returns emptyList()
        every { userAddrRepository.saveAll(any<List<UserAddrEntity>>()) } returns emptyList()
        every { CountryCodeType.fromDialCode(any()) } returns CountryCodeType.KOREA.isoCode

        // When
        val result = userService.createUser(userInfoDto, emptyList(), emptyList())

        // Then
        assertEquals(userIdx, result)
        verify(exactly = 1) { userInfoRepository.save(any<UserInfoEntity>()) }
        verify(exactly = 1) { userTelRepository.saveAll(any<List<UserTelEntity>>()) }
        verify(exactly = 1) { userAddrRepository.saveAll(any<List<UserAddrEntity>>()) }

    }

    @Test
    fun `유저 생성 - 실패 - 전화번호 갯수 초과`() {
        val manyTels = List(10) {
            UserTelDto(
                tel = "+821012345678",
                countryCode = CountryCodeType.KOREA.isoCode,
                isIdentity = "Y"
            )
        }

        val exception = assertThrows<CustomException> {
            userService.createUser(userInfoDto, userAddrDtoList, manyTels)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        assertEquals(ErrorType.TOO_MANY_TELEPHONES.message, exception.message)
    }

    @Test
    fun `유저 조회 - 성공`() {
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns userInfoEntity
        every { userAddrRepository.findByUserIdx(userIdx) } returns userAddrDtoList.map { it.toEntity(userIdx) }
        every { userTelRepository.findByUserIdx(userIdx) } returns userTelDtoList.map { it.toEntity(userIdx) }

        val result = userService.readActiveUser(userIdx)

        assertEquals("이완호", result.name)
        verify(exactly = 1) { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) }
    }

    @Test
    fun `유저 조회 - 실패 - 존재하지 않는 사용자`() {
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns null

        val exception = assertThrows<CustomException> {
            userService.readActiveUser(userIdx)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals(ErrorType.USER_NOT_FOUND.message, exception.message)
    }

    @Test
    fun `유저 수정 - 성공`() {
        // Given - Mock 설정
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns userInfoEntity
        every { userInfoRepository.save(any<UserInfoEntity>()) } returns userInfoEntity
        every { userAddrRepository.findByUserIdx(userIdx) } returns emptyList()
        every { userTelRepository.findByUserIdx(userIdx) } returns emptyList()
        every { userAddrRepository.deleteAll(any<List<UserAddrEntity>>()) } just Runs
        every { userAddrRepository.saveAll(any<List<UserAddrEntity>>()) } returns emptyList()
        every { userTelRepository.deleteAll(any<List<UserTelEntity>>()) } just Runs
        every { userTelRepository.saveAll(any<List<UserTelEntity>>()) } returns emptyList()

        every { CountryCodeType.fromDialCode(any()) } returns CountryCodeType.KOREA.isoCode

        // When
        val result = userService.updateUser(userIdx, userInfoDto, userAddrDtoList, userTelDtoList)

        // Then
        assertEquals(userIdx, result)

        verify(exactly = 1) { userInfoRepository.save(any<UserInfoEntity>()) }
        verify(exactly = 1) { userAddrRepository.deleteAll(any<List<UserAddrEntity>>()) }
        verify(exactly = 1) { userAddrRepository.saveAll(any<List<UserAddrEntity>>()) }
        verify(exactly = 1) { userTelRepository.deleteAll(any<List<UserTelEntity>>()) }
        verify(exactly = 1) { userTelRepository.saveAll(any<List<UserTelEntity>>()) }
    }

    @Test
    fun `유저 수정 - 실패 - 존재하지 않는 사용자`() {
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns null

        val exception = assertThrows<CustomException> {
            userService.updateUser(userIdx, userInfoDto, userAddrDtoList, userTelDtoList)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals(ErrorType.USER_NOT_FOUND.message, exception.message)
    }

    @Test
    fun `유저 삭제 - 성공`() {
        // Given
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns userInfoEntity
        every { userInfoRepository.save(any()) } returns userInfoEntity
        every { userTelRepository.findByUserIdx(userIdx) } returns emptyList()
        every { userAddrRepository.findByUserIdx(userIdx) } returns emptyList()
        every { userAddrRepository.deleteAll(any()) } just Runs
        every { userTelRepository.deleteAll(any()) } just Runs

        // When
        val result = userService.deleteUser(userIdx)

        // Then
        assertTrue(result)
        verify(exactly = 1) { userInfoRepository.save(any()) }
        verify(exactly = 1) { userTelRepository.deleteAll(any()) }
        verify(exactly = 1) { userAddrRepository.deleteAll(any()) }
    }

    @Test
    fun `유저 삭제 - 실패 - 존재하지 않는 사용자`() {
        every { userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE) } returns null

        val exception = assertThrows<CustomException> {
            userService.deleteUser(userIdx)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals(ErrorType.USER_NOT_FOUND.message, exception.message)
    }
}
