package com.murple.murfy.infrastructure.jpa.entity

import com.murple.murfy.domain.model.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.ZonedDateTime


@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 1024)
    val name: String,

    @Column
    val age: Int? = null,

    @Column
    @Convert(converter = GenderConverter::class)
    val gender: Gender? = null,

    @Column(length = 1024)
    val email: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val phones: MutableList<PhoneEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val addresses: MutableList<AddressEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime = ZonedDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
)
