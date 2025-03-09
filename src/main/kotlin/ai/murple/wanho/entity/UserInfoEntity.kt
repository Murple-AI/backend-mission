package ai.murple.wanho.entity

import ai.murple.wanho.code.SexType
import ai.murple.wanho.code.UserStatusType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant


/**
 * 사용자 엔티티
 *
 * @property idx 인덱스
 * @property name 이름
 * @property age 나이
 * @property sex 성별 (M: 남자, W: 여자)
 * @property email 이메일
 * @property status 상태 (ACTIVE: 활성화, DELETED: 삭제)
 * @property createdAt 생성 일자
 * @property updatedAt 수정 일자
 */
@Entity
@Table(
    name = "user_info",
    schema = "public",
)
data class UserInfoEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false) val idx: Long? = null,

    @Column(name = "name", length = 1024, nullable = false) val name: String,

    @Column(name = "age") val age: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", length = 1) val sex: SexType? = null,

    @Column(name = "email", length = 1024) val email: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50) val status: UserStatusType = UserStatusType.ACTIVE,

    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false,
        columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT now()"
    )
    val createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val updatedAt: Instant = Instant.now(),

    )


