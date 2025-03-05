package app.boboc.chatserver.service

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserEntity
import app.boboc.chatserver.repository.UserAddressRepository
import app.boboc.chatserver.repository.UserPhoneNumberRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userAddressRepository: UserAddressRepository,
    private val userPhoneNumberRepository: UserPhoneNumberRepository
) {
    suspend fun getUsers(): List<Responses.User> {
        return userRepository.findAllByDeletedFalseOrderByCreatedAt().map {
            Responses.User.from(it)
        }
    }

    suspend fun getUser(userId: Long): Responses.UserDetail {
        return userRepository.findById(userId)?.run {
            if(this.isDeleted == true) throw RuntimeException("User not found")

            Responses.UserDetail.from(
                user = this,
                addresses = userAddressRepository.findByUserIdAndDeletedFalseOrderById(userId),
                phoneNumbers = userPhoneNumberRepository.findByUserIdAndDeletedFalseOrderById(userId)
            )
        } ?: throw RuntimeException("User not found")
    }

    @Transactional
    suspend fun registerUser(request: Requests.User) {
        userRepository.save(
            UserEntity(
                name = request.name,
                email = request.email,
                age = request.age,
                gender = request.gender,
            )
        )
    }

    @Transactional
    suspend fun updateUser(userId: Long, request: Requests.User) {
        userRepository.findById(userId)?.also {
            userRepository.save(
                it.copy(
                    name = request.name,
                    email = it.email,
                    age = it.age,
                    gender = request.gender
                )
            )
        }
    }

    suspend fun deleteUser(userId: Long) {
        userRepository.findById(userId)?.also {
            userRepository.save(
                it.copy(
                    isDeleted = true
                )
            )
        }
    }
}
