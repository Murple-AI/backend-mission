package com.murple.murfy.infrastructure.jpa.repository

import com.murple.murfy.domain.model.Address
import com.murple.murfy.domain.model.Phone
import com.murple.murfy.domain.model.User
import com.murple.murfy.domain.repository.UserRepository
import com.murple.murfy.infrastructure.jpa.entity.AddressEntity
import com.murple.murfy.infrastructure.jpa.entity.PhoneEntity
import com.murple.murfy.infrastructure.jpa.entity.UserEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    @Transactional
    override fun save(user: User): User {
        val userEntity = toUserEntity(user)
        val savedEntity = userJpaRepository.save(userEntity)
        return toUser(savedEntity)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): User? {
        return userJpaRepository.findById(id).map { toUser(it) }.orElse(null)
    }



    @Transactional
    override fun delete(id: Long) {
        userJpaRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun findByName(name: String): List<User> {
        return userJpaRepository.findByName(name).map { toUser(it) }
    }

    @Transactional(readOnly = true)
    override fun findByNameOrderByCreatedAtLimit(name: String): List<User> {
        return userJpaRepository.findTop5ByNameOrderByCreatedAtAsc(name).map { toUser(it) }
    }

    private fun toUserEntity(user: User): UserEntity {
        val userEntity = UserEntity(
            id = user.id,
            name = user.name,
            age = user.age,
            gender = user.gender,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )

        // 전화번호 변환
        val phoneEntities = user.phones.map { phone ->
            PhoneEntity(
                id = phone.id,
                user = userEntity,
                label = phone.label,
                number = phone.number,
                countryCode = phone.countryCode,
                isVerified = phone.isVerified
            )
        }.toMutableList()

        // 주소 변환
        val addressEntities = user.addresses.map { address ->
            AddressEntity(
                id = address.id,
                user = userEntity,
                label = address.label,
                street = address.street,
                city = address.city,
                zipCode = address.zipCode
            )
        }.toMutableList()

        userEntity.phones.addAll(phoneEntities)
        userEntity.addresses.addAll(addressEntities)

        return userEntity
    }

    private fun toUser(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            age = entity.age,
            gender = entity.gender,
            email = entity.email,
            phones = entity.phones.map { phone ->
                Phone(
                    id = phone.id,
                    label = phone.label,
                    number = phone.number,
                    countryCode = phone.countryCode,
                    isVerified = phone.isVerified
                )
            }.toMutableList(),
            addresses = entity.addresses.map { address ->
                Address(
                    id = address.id,
                    label = address.label,
                    street = address.street,
                    city = address.city,
                    zipCode = address.zipCode
                )
            }.toMutableList(),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
