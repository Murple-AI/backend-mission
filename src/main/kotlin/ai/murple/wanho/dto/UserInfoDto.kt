package ai.murple.wanho.dto

import ai.murple.wanho.code.SexType
import ai.murple.wanho.code.UserStatusType
import ai.murple.wanho.entity.UserInfoEntity
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
 * @property addresses 주소 목록
 * @property telephones 전화번호 목록
 */
data class UserInfoDto(
    val idx: Long? = null,
    val name: String,
    val age: Int? = null,
    val sex: SexType? = null,
    val email: String? = null,
    val status: UserStatusType = UserStatusType.ACTIVE,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) {
    /**
     * DTO를 Entity로 변환하는 메서드
     */
    fun toEntity(): UserInfoEntity {
        return UserInfoEntity(
            idx = this.idx,
            name = this.name,
            age = this.age,
            sex = this.sex,
            email = this.email,
            status = this.status,
        )
    }
}


/**
 * UserEntity를 UserDTO로 변환하는 확장 함수
 */
fun UserInfoEntity.toDTO(): UserInfoDto {
    return UserInfoDto(
        idx = this.idx,
        name = this.name,
        age = this.age,
        sex = this.sex,
        email = this.email,
        status = this.status,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}



