package ai.murple.wanho.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

/**
 * 사용자 전화번호 엔티티
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
@Entity
@Table(
    name = "user_tel",
    schema = "public",
)
data class UserTelEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false) val idx: Long? = null,

    @Column(name = "user_idx", nullable = false)
    val userIdx: Long,

    @Column(name = "tel", length = 15, nullable = false) val tel: String,

    @Column(name = "label", length = 1024) val label: String? = null,

    @Column(name = "county_code", length = 2, nullable = false) val countryCode: String,

    @Column(name = "is_identity", length = 1, nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    val isIdentity: String = "N",

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT now()")
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val updatedAt: Instant = Instant.now()
)