package com.murple.murfy.application.service

import com.murple.murfy.application.dto.AddressDto
import com.murple.murfy.application.dto.PhoneDto
import com.murple.murfy.application.dto.UserDto
import com.murple.murfy.domain.enums.EnumUtils
import com.murple.murfy.domain.model.Address
import com.murple.murfy.domain.enums.Gender
import com.murple.murfy.domain.enums.Label
import com.murple.murfy.domain.model.Phone
import com.murple.murfy.domain.model.User
import com.murple.murfy.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val phoneNumberService: PhoneNumberService
) {

    @Transactional
    fun createUser(request: UserDto): User {
        val user = User(
            name = request.name,
            age = request.age,
            gender = request.gender?.let { EnumUtils.fromString<Gender>(it) },
            email = request.email,
            phones = request.phones?.map { dto ->
                val countryCode = dto.countryCode ?: phoneNumberService.extractCountryCode(dto.number)
                ?: throw IllegalArgumentException("A country code must be provided or extracted from the phone number.")

                Phone(
                    label = EnumUtils.fromString<Label>(dto.label),
                    number = dto.number,
                    countryCode = countryCode,
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
    fun getUserById(id: Long): User {
        val user = userRepository.findById(id) ?: throw NoSuchElementException("User with ID $id not found.")
        return user
    }


    @Transactional
    fun updateUserInfo(id: Long, request: UserDto): User {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val updatedUser = existingUser.copy(
            name = request.name,
            age = request.age,
            gender = request.gender?.let { EnumUtils.fromString<Gender>(it) },
            email = request.email
        )

        return userRepository.save(updatedUser)
    }

    @Transactional
    fun updateUserPhone(id: Long, phoneId: Long, phoneDto: PhoneDto): Phone {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val phoneIndex = existingUser.phones.indexOfFirst { it.id == phoneId }
        if (phoneIndex == -1) {
            throw NoSuchElementException("Phone with ID $phoneId not found for user with ID $id.")
        }

        val countryCode = phoneDto.countryCode ?: phoneNumberService.extractCountryCode(phoneDto.number)
        ?: throw IllegalArgumentException("A country code must be provided or extracted from the phone number.")

        val updatedPhone = Phone(
            id = phoneId, // 기존 ID 유지
            label = EnumUtils.fromString(phoneDto.label),
            number = phoneDto.number,
            countryCode = countryCode,
            isVerified = phoneDto.isVerified
        )

        val updatedPhones = existingUser.phones.toMutableList()
        updatedPhones[phoneIndex] = updatedPhone

        val updatedUser = existingUser.copy(phones = updatedPhones)
        val savedUser = userRepository.save(updatedUser)
        return savedUser.phones[phoneIndex]
    }

    @Transactional
    fun updateUserAddress(id: Long, addressId: Long, addressDto: AddressDto): Address {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val addressIndex = existingUser.addresses.indexOfFirst { it.id == addressId }
        if (addressIndex == -1) {
            throw NoSuchElementException("Address with ID $addressId not found for user with ID $id.")
        }

        val updatedAddress = Address(
            id = addressId, // 기존 ID 유지
            label = EnumUtils.fromString(addressDto.label),
            street = addressDto.street,
            city = addressDto.city,
            zipCode = addressDto.zipcode
        )

        val updatedAddresses = existingUser.addresses.toMutableList()
        updatedAddresses[addressIndex] = updatedAddress

        val updatedUser = existingUser.copy(addresses = updatedAddresses)
        val savedUser = userRepository.save(updatedUser)
        return savedUser.addresses[addressIndex]
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.findById(id) ?: throw NoSuchElementException("User with ID $id not found.")
        userRepository.delete(id)
    }

    @Transactional(readOnly = true)
    fun findUsersByName(name: String): List<User> {
        return userRepository.findByName(name)
    }

    @Transactional(readOnly = true)
    fun findTopUsersByName(name: String): List<User> {
        return userRepository.findByNameOrderByCreatedAtLimit(name)
    }

    @Transactional
    fun addUserPhone(id: Long, phoneDto: PhoneDto): Phone {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val countryCode = phoneDto.countryCode ?: phoneNumberService.extractCountryCode(phoneDto.number)
        ?: throw IllegalArgumentException("A country code must be provided or extracted from the phone number.")

        val newPhone = Phone(
            label = EnumUtils.fromString(phoneDto.label),
            number = phoneDto.number,
            countryCode = countryCode,
            isVerified = phoneDto.isVerified
        )

        val updatedPhones = existingUser.phones.toMutableList()
        updatedPhones.add(newPhone)

        val updatedUser = existingUser.copy(phones = updatedPhones)
        val savedUser = userRepository.save(updatedUser)

        return savedUser.phones.last()
    }

    @Transactional
    fun deleteUserPhone(id: Long, phoneId: Long): Phone {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val phoneToDelete = existingUser.phones.find { it.id == phoneId }
            ?: throw NoSuchElementException("Phone with ID $phoneId not found for user with ID $id.")

        val updatedPhones = existingUser.phones.toMutableList()
        updatedPhones.removeIf { it.id == phoneId }

        val updatedUser = existingUser.copy(phones = updatedPhones)
        userRepository.save(updatedUser)

        return phoneToDelete
    }

    @Transactional
    fun addUserAddress(id: Long, addressDto: AddressDto): Address {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val newAddress = Address(
            label = EnumUtils.fromString(addressDto.label),
            street = addressDto.street,
            city = addressDto.city,
            zipCode = addressDto.zipcode
        )

        val updatedAddresses = existingUser.addresses.toMutableList()
        updatedAddresses.add(newAddress)

        val updatedUser = existingUser.copy(addresses = updatedAddresses)
        val savedUser = userRepository.save(updatedUser)

        return savedUser.addresses.last()
    }

    @Transactional
    fun deleteUserAddress(id: Long, addressId: Long): Address {
        val existingUser = userRepository.findById(id)
            ?: throw NoSuchElementException("User with ID $id not found.")

        val addressToDelete = existingUser.addresses.find { it.id == addressId }
            ?: throw NoSuchElementException("Address with ID $addressId not found for user with ID $id.")

        val updatedAddresses = existingUser.addresses.toMutableList()
        updatedAddresses.removeIf { it.id == addressId }

        val updatedUser = existingUser.copy(addresses = updatedAddresses)
        userRepository.save(updatedUser)

        return addressToDelete
    }

}
