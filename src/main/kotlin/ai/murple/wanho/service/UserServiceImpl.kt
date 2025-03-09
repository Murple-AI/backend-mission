package ai.murple.wanho.service

import ai.murple.wanho.code.CountryCodeType
import ai.murple.wanho.code.ErrorType
import ai.murple.wanho.code.SexType
import ai.murple.wanho.code.UserStatusType
import ai.murple.wanho.config.ReadOnly
import ai.murple.wanho.dto.*
import ai.murple.wanho.exception.CustomException
import ai.murple.wanho.repository.UserAddrRepository
import ai.murple.wanho.repository.UserInfoRepository
import ai.murple.wanho.repository.UserTelRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserServiceImpl(
    private val userInfoRepository: UserInfoRepository,
    private val userAddrRepository: UserAddrRepository,
    private val userTelRepository: UserTelRepository
) : UserService {

    @Transactional
    override fun createUser(
        userInfoDto: UserInfoDto,
        userAddrDtoList: List<UserAddrDto>,
        userTelDtoList: List<UserTelDto>
    ): Long? {

        validateUser(userInfoDto, userAddrDtoList, userTelDtoList)

        val userEntity = userInfoDto.toEntity()
        val savedUserEntity = userInfoRepository.save(userEntity)

        val userIdx = savedUserEntity.idx ?: throw CustomException(ErrorType.USER_NOT_FOUND)

        val telephonesEntity = userTelDtoList.map {
            it.copy(
                countryCode = resolveCountryCode(it.tel, it.countryCode),
            ).toEntity(userIdx)
        }

        userTelRepository.saveAll(telephonesEntity)

        val addressesEntity = userAddrDtoList.map { it.toEntity(userIdx) }
        userAddrRepository.saveAll(addressesEntity)

        return savedUserEntity.idx
    }


    @ReadOnly
    override fun readActiveUser(userIdx: Long): UserDto {
        val userInfoEntity = userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE)
        val userInfoDto = userInfoEntity?.toDTO() ?: throw CustomException(ErrorType.USER_NOT_FOUND)

        val addressesEntity = userAddrRepository.findByUserIdx(userIdx)
        val userAddrDtoList = addressesEntity.map { it.toDTO() }

        val telephonesEntity = userTelRepository.findByUserIdx(userIdx)
        val userTelDtoList = telephonesEntity.map { it.toDTO() }


        return UserDto.fromDto(userInfoDto, userAddrDtoList, userTelDtoList)
    }

    @Transactional
    override fun updateUser(
        userIdx: Long,
        updatedUserInfoDto: UserInfoDto,
        updatedUserAddrDtoList: List<UserAddrDto>,
        updatedUserTelDtoList: List<UserTelDto>
    ): Long? {

        validateUser(updatedUserInfoDto, updatedUserAddrDtoList, updatedUserTelDtoList)


        val userInfoEntity = userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE)
        val userInfoDto = userInfoEntity?.toDTO() ?: throw CustomException(ErrorType.USER_NOT_FOUND)


        userInfoRepository.save(
            updatedUserInfoDto.copy(
                idx = userIdx,
                updatedAt = Instant.now()
            ).toEntity()
        )

        userAddrRepository.deleteAll(userAddrRepository.findByUserIdx(userIdx))
        userAddrRepository.saveAll(updatedUserAddrDtoList.map {
            it.copy(
                updatedAt = Instant.now()
            ).toEntity(userIdx)
        })

        userTelRepository.deleteAll(userTelRepository.findByUserIdx(userIdx))
        userTelRepository.saveAll(updatedUserTelDtoList.map {
            it.copy(
                countryCode = resolveCountryCode(it.tel, it.countryCode),
                updatedAt = Instant.now()
            ).toEntity(userIdx)
        })

        return userInfoDto.idx
    }

    @Transactional
    override fun deleteUser(userIdx: Long): Boolean {
        val userEntity = userInfoRepository.findByIdxAndStatus(userIdx, UserStatusType.ACTIVE)
        val userDto = userEntity?.toDTO() ?: throw CustomException(ErrorType.USER_NOT_FOUND)

        userInfoRepository.save(
            userDto.copy(
                status = UserStatusType.DELETED,
                updatedAt = Instant.now(),
            ).toEntity()
        )

        userTelRepository.deleteAll(userTelRepository.findByUserIdx(userIdx))
        userAddrRepository.deleteAll(userAddrRepository.findByUserIdx(userIdx))

        return true
    }

    /**
     * 전화번호와 입력된 국가코드에서 대문자 국가코드를 만들어주는 메서드
     *
     * @param tel
     * @param inputCode
     * @return 생성된 국가코드
     */
    private fun resolveCountryCode(tel: String, inputCode: String?): String {
        return inputCode
            ?.uppercase()
            ?.takeIf { it.length == 2 }
            ?: CountryCodeType.fromDialCode(tel)
            ?: "**"
    }

    /**
     * User에 대한 유효성 검증을 하는 메서드
     */
    private fun validateUser(
        userInfoDto: UserInfoDto,
        userAddrDtoList: List<UserAddrDto>,
        userTelDtoList: List<UserTelDto>
    ) {
        if (userInfoDto.name == "") {
            throw CustomException(ErrorType.REQUIRED_USER_NAME)
        }

        if (userInfoDto.name.length > 1024) {
            throw CustomException(ErrorType.NAME_TOO_LONG)
        }

        if (userInfoDto.age != null &&
            userInfoDto.age < 0
        ) {
            throw CustomException(ErrorType.INVALID_AGE)
        }

        if (userInfoDto.email != null &&
            !userInfoDto.email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        ) {
            throw CustomException(ErrorType.INVALID_EMAIL)
        }

        if (userInfoDto.email != null &&
            userInfoDto.email.length > 1024
        ) {
            throw CustomException(ErrorType.EMAIL_TOO_LONG)
        }

        if (userInfoDto.sex != SexType.M && userInfoDto.sex != SexType.W) {
            throw CustomException(ErrorType.INVALID_SEX)
        }

        if (userAddrDtoList.size >= 9) {
            throw CustomException(ErrorType.TOO_MANY_ADDRESSES)
        }

        userAddrDtoList.forEach {
            if (it.addr.length > 1024) {
                throw CustomException(ErrorType.TOO_LONG_ADDRESS)
            }
        }

        if (userTelDtoList.size >= 9) {
            throw CustomException(ErrorType.TOO_MANY_TELEPHONES)
        }

        userTelDtoList.forEach {
            if (!it.tel.matches(Regex("^\\+[1-9]\\d{1,14}\$"))) {
                throw CustomException(ErrorType.INVALID_TEL_E164)
            }

            if (it.tel.length >= 16) {
                throw CustomException(ErrorType.TOO_LONG_TELEPHONE)
            }
        }


    }

}