package app.boboc.chatserver.service

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserAddressEntity
import app.boboc.chatserver.exceptions.MissionExceptions
import app.boboc.chatserver.repository.UserAddressRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserAddressService(
    val userRepository: UserRepository,
    val userAddressRepository: UserAddressRepository
) {
    suspend fun getUserAddresses(userId: Long): List<Responses.Address> {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        return userAddressRepository.findAllByUserIdAndIsDeletedFalseOrderById(userId)
            .map {
                Responses.Address.from(it)
            }
    }

    suspend fun getUserAddress(userId: Long, addressId: Long): Responses.Address {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        return userAddressRepository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)
            ?.run { Responses.Address.from(this) } ?: throw MissionExceptions.AddressNotFoundException()
    }

    suspend fun registerUserAddress(userId: Long, req: Requests.Address) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()
        if (userAddressRepository.countByUserIdAndIsDeletedFalse(userId) >= 8) throw MissionExceptions.ExceedLimitAddressException()

        userAddressRepository.save(
            UserAddressEntity(
                userId = userId,
                label = req.label,
                address = req.address,
            )
        )
    }

    suspend fun updateUserAddress(userId: Long, addressId: Long, req: Requests.Address) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        userAddressRepository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)?.also {
            userAddressRepository.save(it.copy(address = req.address, label = req.label))
        } ?: throw MissionExceptions.AddressNotFoundException()
    }

    suspend fun deleteUserAddress(userId: Long, addressId: Long) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) throw MissionExceptions.UserNotFoundException()

        userAddressRepository.findByIdAndUserIdAndIsDeletedFalse(addressId, userId)?.also {
            userAddressRepository.save(it.copy(isDeleted = true))
        } ?: throw MissionExceptions.AddressNotFoundException()
    }
}
