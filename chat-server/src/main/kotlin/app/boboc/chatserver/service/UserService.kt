package app.boboc.chatserver.service

import app.boboc.chatserver.dto.Requests
import app.boboc.chatserver.dto.Responses
import app.boboc.chatserver.entity.UserEntity
import app.boboc.chatserver.exceptions.MissionExceptions
import app.boboc.chatserver.repository.UserAddressRepository
import app.boboc.chatserver.repository.UserPhoneNumberRepository
import app.boboc.chatserver.repository.UserRepository
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userAddressRepository: UserAddressRepository,
    private val userPhoneNumberRepository: UserPhoneNumberRepository
) {
    suspend fun getUsers(): List<Responses.User> {
        return userRepository.findAllByIsDeletedFalseOrderByCreatedAt().map {
            Responses.User.from(it)
        }
    }

    suspend fun getUsersByName(name: String): List<Responses.User> {
        return userRepository.findAllByNameAndIsDeletedFalseOrderByCreatedAt(name)
            .map { Responses.User.from(it) }
    }

    suspend fun getUser(userId: Long): Responses.UserDetail {
        return userRepository.findById(userId)?.run {
            if(this.isDeleted) throw MissionExceptions.UserNotFoundException()

            Responses.UserDetail.from(
                user = this,
                addresses = userAddressRepository.findAllByUserIdAndIsDeletedFalseOrderById(userId),
                phoneNumbers = userPhoneNumberRepository.findAllByUserIdAndIsDeletedFalseOrderById(userId)
            )
        } ?: throw MissionExceptions.UserNotFoundException()
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
        } ?: throw MissionExceptions.UserNotFoundException()
    }

    suspend fun deleteUser(userId: Long) {
        userRepository.findById(userId)?.also {
            userRepository.save(
                it.copy(
                    isDeleted = true
                )
            )
        }  ?: throw MissionExceptions.UserNotFoundException()
    }
}
