package com.murple.murfy.application.user.service

import com.murple.murfy.application.user.dto.AddressDto
import com.murple.murfy.application.user.dto.PhoneDto
import com.murple.murfy.application.user.dto.UserDto
import com.murple.murfy.domain.user.enums.EnumUtils
import com.murple.murfy.domain.user.model.Address
import com.murple.murfy.domain.user.enums.Gender
import com.murple.murfy.domain.user.enums.Label
import com.murple.murfy.domain.user.model.Phone
import com.murple.murfy.domain.user.model.UserAggregate
import com.murple.murfy.domain.user.model.UserBasic
import com.murple.murfy.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun createUser(request: UserDto): UserAggregate {
        val user = UserAggregate(
            name = request.name,
            age = request.age,
            gender = request.gender?.let { EnumUtils.fromString<Gender>(it) },
            email = request.email,
            phones = request.phones?.map { dto ->

                Phone.of(
                    label = EnumUtils.fromString<Label>(dto.label),
                    number = dto.number,
                    countryCode = dto.countryCode,
                    isVerified = dto.isVerified
                )
            }?.toMutableList() ?: mutableListOf(),
            addresses = request.addresses?.map { dto ->
                Address(
                    label = EnumUtils.fromString<Label>(dto.label),
                    street = dto.street,
                    city = dto.city,
                    zipCode = dto.zipcode
                )
            }?.toMutableList() ?: mutableListOf()
        )

        val savedUser = userRepository.save(user)
        return savedUser
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserAggregate {
        val user = userRepository.findById(id) ?: throw NoSuchElementException("User with ID $id not found.")
        return user
    }


    @Transactional
    fun updateUserInfo(id: Long, request: UserDto): UserBasic {
        val updatedUser = UserBasic(
            id = id,
            name = request.name,
            age = request.age,
            gender = request.gender?.let { EnumUtils.fromString<Gender>(it) },
            email = request.email
        )

        return userRepository.updateUserBasicInfo(updatedUser)
    }


    @Transactional
    fun updateUserPhone(id: Long, phoneId: Long, phoneDto: PhoneDto): Phone {
        // 유효성 검증
        val updatedPhone = Phone.of(
            id = phoneId, // 기존 ID 유지
            label = EnumUtils.fromString(phoneDto.label),
            number = phoneDto.number,
            countryCode = phoneDto.countryCode,
            isVerified = phoneDto.isVerified
        )

        val result = userRepository.updateUserPhone(userId = id, updatedPhone = updatedPhone)
        return result
    }


    @Transactional
    fun updateUserAddress(id: Long, addressId: Long, addressDto: AddressDto): Address {

        val updatedAddress = Address(
            id = addressId, // 기존 ID 유지
            label = EnumUtils.fromString(addressDto.label),
            street = addressDto.street,
            city = addressDto.city,
            zipCode = addressDto.zipcode
        )

        val result = userRepository.updateUserAddress(userId = id, updatedAddress)
        return result
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.delete(id)
    }


    @Transactional(readOnly = true)
    fun findTopUsersByName(name: String): List<UserBasic> {
        return userRepository.findTop5ByNameOrderByCreatedAtAsc(name)
    }

    @Transactional(readOnly = true)
    fun findTopUsersByNameList(name: List<String>): List<UserBasic> {
        return userRepository.findByNamesLimitedByCreatedAt(name)
    }

    @Transactional
    fun addUserPhone(id: Long, phoneDto: PhoneDto): Phone {
        val newPhone = Phone.of(
            label = EnumUtils.fromString(phoneDto.label),
            number = phoneDto.number,
            countryCode = phoneDto.countryCode,
            isVerified = phoneDto.isVerified
        )

        return userRepository.addNewPhone(userId = id, newPhone = newPhone)
    }

    @Transactional
    fun deleteUserPhone(id: Long, phoneId: Long) {
        userRepository.deleteUserPhone(userId = id, phoneId = phoneId)
    }

    @Transactional
    fun addUserAddress(id: Long, addressDto: AddressDto): Address {
        val newAddress = Address(
            label = EnumUtils.fromString(addressDto.label),
            street = addressDto.street,
            city = addressDto.city,
            zipCode = addressDto.zipcode
        )

        return userRepository.addNewAddress(userId = id, newAddress = newAddress)
    }

    @Transactional
    fun deleteUserAddress(id: Long, addressId: Long) {
        userRepository.deleteUserAddress(userId = id, addressId = addressId)
    }

}
