package ai.murple.wanho.dto

import ai.murple.wanho.entity.UserAddrEntity
import java.time.Instant

/**
 * 사용자 주소 엔티티
 *
 * @property idx 주소 인덱스
 * @property userIdx 사용자 인덱스
 * @property addr 주소
 * @property label 주소 라벨 (자택, 직장 등)
 * @property createdAt 생성 시간
 * @property updatedAt 수정 시간
 */
data class UserAddrDto(
    val idx: Long? = null,
    val userIdx: Long? = null,
    val addr: String,
    val label: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    /**
     * DTO를 Entity로 변환하는 메서드
     */
    fun toEntity(userIdx: Long): UserAddrEntity {
        return UserAddrEntity(
            idx = this.idx,
            userIdx = userIdx,
            addr = this.addr,
            label = this.label,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }

    /**
     * DTO를 Response로 변환하는 메서드
     */
    fun toResponse(): UserAddrResponse {
        return UserAddrResponse(
            addr = this.addr,
            label = this.label,
        )
    }
}


/**
 * 사용자 주소 요청 엔티티
 *
 * @property addr 주소
 * @property label 주소 라벨 (자택, 직장 등)
 */
data class UserAddrRequest(
    val addr: String,
    val label: String? = null,
) {

    /**
     * Request를 DTO로 변환하는 메서드
     */
    fun toDto(): UserAddrDto {
        return UserAddrDto(
            addr = this.addr,
            label = this.label,
        )

    }
}


/**
 * 사용자 주소 응답 엔티티
 *
 * @property addr 주소
 * @property label 주소 라벨 (자택, 직장 등)
 */

data class UserAddrResponse(
    val addr: String,
    val label: String? = null,
)

/**
 * Entity를 DTO로 변환하는 확장 함수
 */
fun UserAddrEntity.toDTO(): UserAddrDto {
    return UserAddrDto(
        idx = this.idx,
        userIdx = this.userIdx,
        addr = this.addr,
        label = this.label,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}

