package ai.murple.wanho.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "korean_last_name",
    schema = "public",
)
data class KoreanLastNameEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false) val idx: Long? = null,

    @Column(name = "last_name", nullable = false, length = 2, unique = true)
    val lastName: String
)
