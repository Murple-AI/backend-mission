package ai.murple.wanho.dto

import ai.murple.wanho.entity.UserTelEntity
import java.time.Instant

/**
 * 사용자 전화번호 DTO
 *
 * @property idx 전화번호 인덱스
 * @property userIdx 사용자 인덱스
 * @property tel 전화번호 (E.164 포맷)
 * @property label 전화번호 라벨 (자택, 직장 등)
 * @property countryCode 국가 코드 (ISO 3166-1)
 * @property isIdentity 본인 인증 여부 (Y:인증, N: 미인증)
 * @property createdAt 생성 시간
 * @property updatedAt 수정 시간
 */
data class UserTelDto(
    val idx: Long? = null,
    val userIdx: Long? = null,
    val tel: String,
    val label: String? = null,
    val countryCode: String,
    val isIdentity: String = "N",
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    /**
     * DTO를 Entity로 변환하는 메서드
     */
    fun toEntity(userIdx: Long): UserTelEntity {
        return UserTelEntity(
            idx = this.idx,
            userIdx = userIdx,
            tel = this.tel,
            label = this.label,
            countryCode = this.countryCode,
            isIdentity = this.isIdentity,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    /**
     * DTO를 Response로 변환하는 메서드
     */
    fun toResponse(): UserTelResponse {
        return UserTelResponse(
            tel = this.tel,
            label = this.label,
            countryCode = this.countryCode,
            isIdentity = this.isIdentity,
        )
    }
}


/**
 * 사용자 전화번호 Request
 *
 * @property tel 전화번호 (E.164 포맷)
 * @property label 전화번호 라벨 (자택, 직장 등)
 * @property countryCode 국가 코드 (ISO 3166-1)
 * @property isIdentity 본인 인증 여부 (Y:인증, N: 미인증)
 */
data class UserTelRequest(
    val tel: String,
    val label: String? = null,
    val countryCode: String = "INIT",
    val isIdentity: String,
) {
    fun toDto(): UserTelDto {
        return UserTelDto(
            tel = this.tel,
            label = this.label,
            countryCode = this.countryCode,
            isIdentity = this.isIdentity,
        )
    }
}

/**
 * 사용자 전화번호 Response
 *
 * @property tel 전화번호 (E.164 포맷)
 * @property label 전화번호 라벨 (자택, 직장 등)
 * @property countryCode 국가 코드 (ISO 3166-1)
 * @property isIdentity 본인 인증 여부 (Y:인증, N: 미인증)
 */
data class UserTelResponse(
    val tel: String,
    val label: String? = null,
    val countryCode: String,
    val isIdentity: String,
)

/**
 * Entity를 DTO로 변환하는 확장 함수
 */
fun UserTelEntity.toDTO(): UserTelDto {
    return UserTelDto(
        idx = this.idx,
        userIdx = this.userIdx,
        tel = this.tel,
        label = this.label,
        countryCode = this.countryCode,
        isIdentity = this.isIdentity,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
