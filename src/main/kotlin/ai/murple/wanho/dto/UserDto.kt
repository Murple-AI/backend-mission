package ai.murple.wanho.dto

import ai.murple.wanho.code.SexType
import ai.murple.wanho.code.UserStatusType
import java.time.Instant


/**
 * 사용자 DTO
 *
 * @property idx 인덱스
 * @property name 이름
 * @property age 나이
 * @property sex 성별 (M: 남자, W: 여자)
 * @property email 이메일
 * @property status 상태 (ACTIVE: 활성화, DELETED: 삭제)
 * @property createdAt 생성 일자
 * @property updatedAt 수정 일자
 * @property addressList 주소 목록
 * @property telephoneList 전화번호 목록
 */
data class UserDto (
    val idx: Long? = null,
    val name: String,
    val age: Int? = null,
    val sex: SexType? = null,
    val email: String? = null,
    val status: UserStatusType = UserStatusType.ACTIVE,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val addressList: List<UserAddrDto> = emptyList(),
    val telephoneList: List<UserTelDto> = emptyList(),
) {

    /**
     * 각 DTO들을 UserDTO로 변환하는 메서드
     */
    companion object {
        fun fromDto(userInfoDto: UserInfoDto, addressDtoList: List<UserAddrDto>, telephoneDtoList: List<UserTelDto>): UserDto {
            return UserDto(
                idx = userInfoDto.idx,
                name = userInfoDto.name,
                age = userInfoDto.age,
                sex = userInfoDto.sex,
                email = userInfoDto.email,
                status = userInfoDto.status,
                createdAt = userInfoDto.createdAt,
                updatedAt = userInfoDto.updatedAt,
                addressList = addressDtoList,
                telephoneList = telephoneDtoList,
            )
        }
    }

    /**
     * DTO를 Response로 변환하는 메서드
     */
    fun toResponse(): UserResponseDto {
        return UserResponseDto(
            idx = idx,
            name = name,
            age = age,
            sex = sex,
            email = email,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt,
            addressList = addressList.map { it.toResponse() },
            telephoneList = telephoneList.map { it.toResponse() }
        )
    }
}


/**
 * 사용자 요청 DTO
 *
 * @property name 이름
 * @property age 나이
 * @property sex 성별 (M: 남자, W: 여자)
 * @property email 이메일
 * @property addressList 주소 목록
 * @property telephoneList 전화번호 목록
 */
data class UserRequestDto (
    val name: String,
    val age: Int? = null,
    val sex: SexType? = null,
    val email: String? = null,
    val addressList: List<UserAddrRequest> = emptyList(),
    val telephoneList: List<UserTelRequest> = emptyList(),
) {

    /**
     * Request를 UserInfoDTO로 변환해주는 메서드
     */
    fun toUserInfoDto(): UserInfoDto {
        return UserInfoDto(
            name = this.name,
            age = this.age,
            sex = this.sex,
            email = this.email,
            status = UserStatusType.ACTIVE,
        )
    }

    /**
     * Request를 UserAddressDTOList로 변환해주는 메서드
     */
    fun toUserAddrDtoList(): List<UserAddrDto> {
        return this.addressList.map { it.toDto() }
    }

    /**
     * Request를 UserTelephoneDTOList로 변환해주는 메서드
     */
    fun toUserTelDtoList(): List<UserTelDto> {
        return this.telephoneList.map { it.toDto() }
    }

}

/**
 * 사용자 응답 DTO
 *
 * @property idx 인덱스
 * @property name 이름
 * @property age 나이
 * @property sex 성별 (M: 남자, W: 여자)
 * @property email 이메일
 * @property status 상태 (ACTIVE: 활성화, DELETED: 삭제)
 * @property createdAt 생성 일자
 * @property updatedAt 수정 일자
 * @property addressList 주소 목록
 * @property telephoneList 전화번호 목록
 */
data class UserResponseDto (
    val idx: Long? = null,
    val name: String,
    val age: Int? = null,
    val sex: SexType? = null,
    val email: String? = null,
    val status: UserStatusType = UserStatusType.ACTIVE,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val addressList: List<UserAddrResponse> = emptyList(),
    val telephoneList: List<UserTelResponse> = emptyList(),
)