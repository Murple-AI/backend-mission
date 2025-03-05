package com.murple.murfy.infrastructure.jpa.entity


import com.murple.murfy.domain.model.Label
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "addresses")
class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null,

    @Column(nullable = false)
    @Convert(converter = LabelConverter::class)
    val label: Label,

    @Column(name = "address", nullable = false, length = 1024)
    val address: String,

)

