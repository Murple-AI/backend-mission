package com.murple.murfy.infrastructure.jpa.entity


import com.murple.murfy.domain.model.Label
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "phone_numbers")
class PhoneEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null,

    @Column(nullable = false)
    @Convert(converter = LabelConverter::class)
    val label: Label,

    @Column(name = "number", nullable = false)
    val number: String,

    @Column(name = "country_code", nullable = false, length = 2)
    val countryCode: String,

    @Column(name = "is_verified", nullable = false)
    val isVerified: Boolean = false,

)
