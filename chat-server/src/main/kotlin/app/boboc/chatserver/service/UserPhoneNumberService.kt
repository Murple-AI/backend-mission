package app.boboc.chatserver.service

import app.boboc.chatserver.data.CountryCode
import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserPhoneNumberEntity
import app.boboc.chatserver.exceptions.MissionExceptions
import app.boboc.chatserver.repository.UserPhoneNumberRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserPhoneNumberService(
    private val userRepository: UserRepository,
    private val userPhoneNumberRepository: UserPhoneNumberRepository,
) {
    suspend fun getPhoneNumbers(userId: Long): List<Responses.PhoneNumber> {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        return userPhoneNumberRepository.findAllByUserIdAndIsDeletedFalseOrderById(userId)
            .map { Responses.PhoneNumber.from(it) }
    }

    suspend fun getPhoneNumber(userId: Long, phoneNumberId: Long): Responses.PhoneNumber {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        return userPhoneNumberRepository.findByIdAndUserIdAndIsDeletedFalse(phoneNumberId, userId)
            ?.run { Responses.PhoneNumber.from(this) } ?: throw MissionExceptions.PhoneNumberNotFoundException()
    }

    suspend fun registerUserPhoneNumber(userId: Long, req: Requests.PhoneNumber) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()
        if (userPhoneNumberRepository.countByUserIdAndIsDeletedFalse(userId) >= 8) throw MissionExceptions.ExceedLimitPhoneNumberException()
        if (req.countryCode != null && CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber) != req.countryCode) throw MissionExceptions.InvalidCountryCode()

        userPhoneNumberRepository.save(
            UserPhoneNumberEntity(
                userId = userId,
                phoneNumber = req.phoneNumber,
                label = req.label,
                countryCode = req.countryCode ?: CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber)
                ?: throw MissionExceptions.InvalidCountryCode()
            )
        )
    }

    suspend fun updateUserPhoneNumber(userId: Long, phoneNumberId: Long, req: Requests.PhoneNumber) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()
        if (req.countryCode != null && CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber) != req.countryCode) throw MissionExceptions.InvalidCountryCode()

        userPhoneNumberRepository.findByIdAndUserIdAndIsDeletedFalse(phoneNumberId, userId)?.also {
            userPhoneNumberRepository.save(
                it.copy(
                    phoneNumber = req.phoneNumber,
                    label = req.label,
                    countryCode = req.countryCode ?: CountryCode.getCountryCodeFromPhoneNumber(req.phoneNumber)
                    ?: throw MissionExceptions.InvalidCountryCode(),
                )
            )
        } ?: throw MissionExceptions.PhoneNumberNotFoundException()
    }

    suspend fun deleteUserPhoneNumber(userId: Long, phoneNumberId: Long) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        userPhoneNumberRepository.findByIdAndUserIdAndIsDeletedFalse(phoneNumberId, userId)?.also {
            userPhoneNumberRepository.save(it.copy(isDeleted = true))
        } ?: throw MissionExceptions.PhoneNumberNotFoundException()
    }

}
