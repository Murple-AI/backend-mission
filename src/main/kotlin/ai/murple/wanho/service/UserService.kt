package ai.murple.wanho.service

import ai.murple.wanho.dto.UserAddrDto
import ai.murple.wanho.dto.UserDto
import ai.murple.wanho.dto.UserInfoDto
import ai.murple.wanho.dto.UserTelDto

interface UserService {

    /**
     * 사용자를 생성하는 함수
     *
     * @param UserInfoDto
     * @param userAddrDtoList
     * @param UserTelDtoList
     * @return 생성된 사용자 인덱스 번호
     */
    fun createUser(userInfoDto: UserInfoDto, userAddrDtoList: List<UserAddrDto>, userTelDtoList: List<UserTelDto>): Long?

    /**
     * 사용자를 조회하는 함수
     *
     * @param UserIdx
     * @return 조회하는 사용자의 정보
     */
    fun readActiveUser(userIdx: Long): UserDto

    /**
     * 사용자 정보를 수정하는 함수
     *
     * @param UserInfoDto
     * @param userAddrDtoList
     * @param UserTelDtoList
     * @return 업데이트된 사용자 인덱스 번호
     */
    fun updateUser(userIdx: Long, updatedUserInfoDto: UserInfoDto, updatedUserAddrDtoList: List<UserAddrDto>, updatedUserTelDtoList: List<UserTelDto>): Long?

    /**
     * 사용자를 삭제하는 함수
     *
     * @param UserIdx
     * @return 삭제 성공 여부
     */
    fun deleteUser(userIdx: Long): Boolean
}