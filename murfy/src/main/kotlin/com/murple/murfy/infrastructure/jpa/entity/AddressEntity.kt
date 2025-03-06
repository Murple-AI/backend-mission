package com.murple.murfy.infrastructure.jpa.entity


import com.murple.murfy.domain.enums.Label
import jakarta.persistence.*

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

    @Column(length = 800)
    val street: String,

    @Column(length = 100)
    val city: String,

    @Column(name = "zip_code", length = 20)
    val zipCode: String,

    // 전체 주소도 저장
    @Column(name = "full_address", length = 1024)
    val fullAddress: String
) {
    constructor(id: Long?, user: UserEntity?, label: Label, street: String, city: String, zipCode: String) : this(
        id = id,
        user = user,
        label = label,
        street = street,
        city = city,
        zipCode = zipCode,
        fullAddress = "$street, $city, $zipCode"
    )
}
