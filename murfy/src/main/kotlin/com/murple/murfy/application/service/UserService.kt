package com.murple.murfy.application.service

import com.murple.murfy.application.dto.UserDto
import com.murple.murfy.domain.model.Address
import com.murple.murfy.domain.model.Gender
import com.murple.murfy.domain.model.Label
import com.murple.murfy.domain.model.Phone
import com.murple.murfy.domain.model.User
import com.murple.murfy.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

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
            gender = request.gender?.let { Gender.valueOf(it) },
            email = request.email,
            phones = request.phones?.map { dto ->
                val countryCode = dto.countryCode ?:
                phoneNumberService.extractCountryCode(dto.number) ?:
                throw IllegalArgumentException("A country code must be provided or extracted from the phone number.")

                Phone(
                    label = Label.valueOf(dto.label),
                    number = dto.number,
                    countryCode = countryCode,
                    isVerified = dto.isVerified
                )
            }?.toMutableList() ?: mutableListOf(),
            addresses = request.addresses?.map { dto ->
                Address(
                    label = Label.valueOf(dto.label),
                    address = dto.address
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
    fun updateUser(id: Long, request: UserDto): User {
        val existingUser = userRepository.findById(id) ?: throw NoSuchElementException("User with ID $id not found.")

        val updatedUser = User(
            id = existingUser.id,
            name = request.name,
            age = request.age,
            gender = request.gender?.let { Gender.valueOf(it) },
            email = request.email,
            phones = request.phones?.map { dto ->
                val countryCode = dto.countryCode ?:
                phoneNumberService.extractCountryCode(dto.number) ?:
                throw IllegalArgumentException("A country code must be provided or extracted from the phone number.")

                Phone(
                    label = Label.valueOf(dto.label),
                    number = dto.number,
                    countryCode = countryCode,
                    isVerified = dto.isVerified
                )
            }?.toMutableList() ?: mutableListOf(),
            addresses = request.addresses?.map { dto ->
                Address(
                    label = Label.valueOf(dto.label),
                    address = dto.address
                )
            }?.toMutableList() ?: mutableListOf(),
            createdAt = existingUser.createdAt,
            updatedAt = ZonedDateTime.now()
        )

        val savedUser = userRepository.update(updatedUser)
        return savedUser
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
}
