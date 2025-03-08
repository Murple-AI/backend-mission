package com.murple.murfy.infrastructure.user.jpa.repository

import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic
import com.murple.murfy.domain.user.repository.UserRepository
import com.murple.murfy.infrastructure.user.jpa.entiy.AddressEntity
import com.murple.murfy.infrastructure.user.jpa.entiy.PhoneEntity
import com.murple.murfy.infrastructure.user.jpa.entiy.UserEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    @Transactional
    override fun save(user: UserAggregate): UserAggregate {
        val userEntity = toUserEntity(user)
        val savedEntity = userJpaRepository.save(userEntity)
        return toUser(savedEntity)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): UserAggregate? {
        return userJpaRepository.findById(id).map { toUser(it) }.orElse(null)
    }

    @Transactional(readOnly = true)
    override fun findUserBasicById(id: Long): UserBasic? {
        return userJpaRepository.findById(id).map { toUserBasic(it) }.orElse(null)
    }

    @Transactional(readOnly = true)
    override fun findByNamesLimitedByCreatedAt(names: List<String>): List<UserBasic> {
        return userJpaRepository.findByNamesLimitedByCreatedAt(names).map { x -> toUserBasic(x) }
    }

    @Transactional(readOnly = true)
    override fun findTop5ByNameOrderByCreatedAtAsc(name: String): List<UserBasic> {
        return userJpaRepository.findTop5ByNameOrderByCreatedAtAsc(name).map { toUserBasic(it) }
    }


    @Transactional
    override fun updateUserBasicInfo(updatedUserBasic: UserBasic): UserBasic {
        val userEntity = userJpaRepository.findById(updatedUserBasic.id!!)
            .orElseThrow { NoSuchElementException("User with ID ${updatedUserBasic.id} not found.") }

        userEntity.apply {
            name = updatedUserBasic.name
            age = updatedUserBasic.age
            email = updatedUserBasic.email
            gender = updatedUserBasic.gender
        }

        userJpaRepository.saveAndFlush(userEntity)
        return toUserBasic(userEntity)
    }

    @Transactional
    override fun updateUserPhone(userId: Long, updatedPhone: Phone): Phone {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        userEntity.phones.find { it.id == updatedPhone.id }
            ?: throw NoSuchElementException("Phone with ID ${updatedPhone.id} not found for user $userId.")


        userEntity.phones.removeIf{it.id == updatedPhone.id}

        val mergedPhoneEntity = toPhoneEntity(updatedPhone, userEntity)

        userEntity.phones.add(mergedPhoneEntity)
        userEntity.updatedAt = LocalDateTime.now()

        userJpaRepository.save(userEntity)

        return toPhone(mergedPhoneEntity)
    }


    @Transactional
    override fun updateUserAddress(userId: Long, updatedAddress: Address): Address {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        val addressId = updatedAddress.id
        userEntity.addresses.find { it.id == addressId }
            ?: throw NoSuchElementException("Address with ID $addressId not found for user $userId.")

        userEntity.addresses.removeIf { it.id == addressId }

        val mergedAddressEntity = toAddressEntity(updatedAddress, userEntity)

        userEntity.addresses.add(mergedAddressEntity)
        userEntity.updatedAt = LocalDateTime.now()

        userJpaRepository.save(userEntity)

        return toAddress(mergedAddressEntity)
    }


    @Transactional
    override fun addNewPhone(userId: Long, newPhone: Phone): Phone {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        val phoneEntity = toPhoneEntity(newPhone, userEntity)
        userEntity.phones.add(phoneEntity)
        userEntity.updatedAt = LocalDateTime.now()

        // 저장 및 플러시
        userJpaRepository.saveAndFlush(userEntity)

        // 마지막으로 추가된 전화번호 찾기 (ID가 할당된 상태)
        val addedPhone = userEntity.phones.last()
        return toPhone(addedPhone)
    }


    @Transactional
    override fun addNewAddress(userId: Long, newAddress: Address): Address {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        val addressEntity = toAddressEntity(newAddress, userEntity)

        userEntity.addresses.add(addressEntity)
        userEntity.updatedAt = LocalDateTime.now()

        userJpaRepository.saveAndFlush(userEntity)

        val addedAddress = userEntity.addresses.last()
        return toAddress(addedAddress)
    }


    @Transactional
    override fun delete(id: Long) {
        userJpaRepository.findById(id)
            .orElseThrow { NoSuchElementException("User with ID $id not found.") }
        userJpaRepository.deleteById(id)
    }

    @Transactional
    override fun deleteUserAddress(userId: Long, addressId: Long) {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        userEntity.addresses.find { it.id == addressId }
            ?: throw NoSuchElementException("Address with ID $addressId not found for user $userId.")

        userEntity.addresses.removeIf { it.id == addressId }
        userEntity.updatedAt = LocalDateTime.now()
        userJpaRepository.save(userEntity)
    }


    @Transactional
    override fun deleteUserPhone(userId: Long, phoneId: Long) {
        val userEntity = userJpaRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User with ID $userId not found.") }

        userEntity.phones.find { it.id == phoneId }
            ?: throw NoSuchElementException("Phone with ID $phoneId not found for user $userId.")

        userEntity.phones.removeIf{it.id == phoneId}
        userEntity.updatedAt = LocalDateTime.now()
        userJpaRepository.save(userEntity)
    }


    private fun toUserEntity(user: UserAggregate): UserEntity {
        val userEntity = UserEntity(
            id = user.id,
            name = user.name,
            age = user.age,
            gender = user.gender,
            email = user.email,
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

    private fun toUser(entity: UserEntity): UserAggregate {
        return UserAggregate(
            id = entity.id,
            name = entity.name,
            age = entity.age,
            gender = entity.gender,
            email = entity.email,
            phones = entity.phones.map { phone ->
                Phone.of(
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


    private fun toUserBasic(entity: UserEntity): UserBasic {
        return UserBasic(
            id = entity.id,
            name = entity.name,
            age = entity.age,
            gender = entity.gender,
            email = entity.email,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }


    private fun toPhoneEntity(phone: Phone, userEntity: UserEntity): PhoneEntity {
        return PhoneEntity(
            id = phone.id,
            user = userEntity,
            label = phone.label,
            number = phone.number,
            countryCode = phone.countryCode,
            isVerified = phone.isVerified
        )
    }

    private fun toPhone(phoneEntity: PhoneEntity): Phone {
        return Phone.of(
            id = phoneEntity.id,
            label = phoneEntity.label,
            number = phoneEntity.number,
            countryCode = phoneEntity.countryCode,
            isVerified = phoneEntity.isVerified
        )
    }

    private fun toAddressEntity(address: Address, userEntity: UserEntity): AddressEntity {
        return AddressEntity(
            id = address.id,
            user = userEntity,
            label = address.label,
            street = address.street,
            city = address.city,
            zipCode = address.zipCode
        )
    }

    private fun toAddress(addressEntity: AddressEntity): Address {
        return Address(
            id = addressEntity.id,
            label = addressEntity.label,
            street = addressEntity.street,
            city = addressEntity.city,
            zipCode = addressEntity.zipCode
        )
    }

}
