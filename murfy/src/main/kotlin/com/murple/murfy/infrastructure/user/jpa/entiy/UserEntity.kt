package com.murple.murfy.infrastructure.user.jpa.entiy

import com.murple.murfy.domain.user.enums.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 1024)
    var name: String,

    @Column
    var age: Int? = null,

    @Column
    @Convert(converter = GenderConverter::class)
    var gender: Gender? = null,

    @Column(length = 1024, unique = true)
    var email: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val phones: MutableList<PhoneEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val addresses: MutableList<AddressEntity> = mutableListOf(),

): BaseTimeEntity()
