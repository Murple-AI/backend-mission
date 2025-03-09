package ai.murple.wanho.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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
@Entity
@Table(
    name = "user_addr",
    schema = "public",
)
data class UserAddrEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false) val idx: Long? = null,

    @Column(name = "user_idx", nullable = false)
    val userIdx: Long,

    @Column(name = "addr", length = 1024, nullable = false) val addr: String,

    @Column(name = "label", length = 1024) val label: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT now()")
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val updatedAt: Instant = Instant.now()
)