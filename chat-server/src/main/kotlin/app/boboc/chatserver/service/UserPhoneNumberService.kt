package app.boboc.chatserver.service

import app.boboc.chatserver.data.CountryCode
import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserPhoneNumberEntity
import app.boboc.chatserver.repository.UserPhoneNumberRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserPhoneNumberService(
    private val userRepository: UserRepository,
    private val userPhoneNumberRepository: UserPhoneNumberRepository
) {
    suspend fun getPhoneNumbers(userId: Long): List<Responses.PhoneNumber> {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        return userPhoneNumberRepository.findAllByUserIdAndDeletedFalseOrderById(userId)
            .map { Responses.PhoneNumber.from(it) }
    }

    suspend fun getPhoneNumber(userId: Long, phoneNumberId: Long): Responses.PhoneNumber {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        return userPhoneNumberRepository.findByIdAndUserIdAndDeletedFalse(userId, phoneNumberId)
            ?.run { Responses.PhoneNumber.from(this) } ?: throw RuntimeException("PhoneNumber not found")
    }

    suspend fun registerUserPhoneNumber(userId: Long, req: Requests.PhoneNumber) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")
        if (userPhoneNumberRepository.countByUserIdAndDeletedFalse(userId) > 8) throw RuntimeException("Exceeded maximum")

        userPhoneNumberRepository.save(
            UserPhoneNumberEntity(
                userId = userId,
                phoneNumber = req.phoneNumber,
                label = req.label,
                countryCode = req.countryCode ?: CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber)
                ?: throw RuntimeException("Invalid phone number")
            )
        )
    }

    suspend fun updateUserPhoneNumber(userId: Long, phoneNumberId: Long, req: Requests.PhoneNumber) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        userPhoneNumberRepository.findByIdAndUserIdAndDeletedFalse(userId, phoneNumberId)?.also {
            userPhoneNumberRepository.save(
                it.copy(
                    phoneNumber = req.phoneNumber,
                    label = req.label,
                    countryCode = req.countryCode ?: CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber)
                    ?: throw RuntimeException("Invalid phone number"),
                )
            )
        } ?: throw RuntimeException("Address phone number")
    }

    suspend fun deleteUserPhoneNumber(userId: Long, phoneNumberId: Long) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        userPhoneNumberRepository.findByIdAndUserIdAndDeletedFalse(userId, phoneNumberId)?.also {
            userPhoneNumberRepository.save(it.copy(isDeleted = true))
        } ?: throw RuntimeException("Phone number not found")
    }

}
