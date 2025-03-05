package app.boboc.chatserver.service

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserAddressEntity
import app.boboc.chatserver.repository.UserAddressRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserAddressService(
    val userRepository: UserRepository,
    val userAddressRepository: UserAddressRepository
) {
    suspend fun getUserAddresses(userId: Long): List<Responses.Address> {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        return userAddressRepository.findByUserIdAndDeletedFalseOrderById(userId)
            .map {
                Responses.Address.from(it)
            }
    }

    suspend fun getUserAddress(userId: Long, addressId: Long): Responses.Address {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        return userAddressRepository.findByIdAndUserIdAndDeletedFalse(userId, addressId)
            ?.run { Responses.Address.from(this) } ?: throw RuntimeException("Address not found")
    }

    suspend fun registerUserAddress(userId: Long, req: Requests.Address) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")
        if (userAddressRepository.countByUserIdAndDeletedFalse(userId) > 8) throw RuntimeException("Exceeded maximum")

        userAddressRepository.save(
            UserAddressEntity(
                userId = userId,
                label = req.label,
                address = req.address,
            )
        )
    }

    suspend fun updateUserAddress(userId: Long, addressId: Long, req: Requests.Address) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        userAddressRepository.findByIdAndUserIdAndDeletedFalse(userId, addressId)?.also {
            userAddressRepository.save(it.copy(address = req.address, label = req.label))
        } ?: throw RuntimeException("Address not found")
    }

    suspend fun deleteUserAddress(userId: Long, addressId: Long) {
        if (!userRepository.existsByIdAndDeletedFalse(userId)) throw RuntimeException("User not found")

        userAddressRepository.findByIdAndUserIdAndDeletedFalse(userId, addressId)?.also {
            userAddressRepository.save(it.copy(isDeleted = true))
        } ?: throw RuntimeException("Address not found")
    }
}
