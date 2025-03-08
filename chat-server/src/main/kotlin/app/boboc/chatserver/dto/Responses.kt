package app.boboc.chatserver.dto

import app.boboc.chatserver.data.CountryCode
import app.boboc.chatserver.data.GenderType
import app.boboc.chatserver.entity.UserAddressEntity
import app.boboc.chatserver.entity.UserEntity
import app.boboc.chatserver.entity.UserPhoneNumberEntity

class Responses {
    data class User(
        val id: Long,
        val name: String,
        val email: String,
        val age: Int? = null,
        val gender: GenderType? = null,
    ) {
        companion object {
            fun from(
                user: UserEntity,
            ): User {
                return User(
                    id = user.id!!,
                    name = user.name,
                    email = user.email,
                    age = user.age,
                    gender = user.gender,
                )
            }
        }
    }

    data class UserDetail(
        val id: Long,
        val name: String,
        val email: String,
        val age: Int? = null,
        val gender: GenderType? = null,
        val addresses: List<Address>,
        val phoneNumbers: List<PhoneNumber>,
    ) {
        companion object {
            fun from(
                user: UserEntity,
                addresses: List<UserAddressEntity> = listOf(),
                phoneNumbers: List<UserPhoneNumberEntity> = listOf(),
            ): UserDetail {
                return UserDetail(
                    id = user.id!!,
                    name = user.name,
                    email = user.email,
                    age = user.age,
                    gender = user.gender,
                    addresses = addresses.map { Address.from(it) },
                    phoneNumbers = phoneNumbers.map { PhoneNumber.from(it) }
                )
            }
        }
    }

    data class PhoneNumber(
        val id: Long,
        val label: String,
        val countryCode: CountryCode? = null,
        val phoneNumber: String,
        val isVerified: Boolean,
    ) {
        companion object {
            fun from(entity: UserPhoneNumberEntity): PhoneNumber {
                return PhoneNumber(
                    id = entity.id!!,
                    label = entity.label,
                    countryCode = entity.countryCode,
                    phoneNumber = entity.phoneNumber,
                    isVerified = entity.isVerified,
                )
            }
        }
    }

    data class Address(
        val id: Long,
        val label: String,
        val address: String,
    ) {
        companion object {
            fun from(entity: UserAddressEntity): Address {
                return Address(
                    id = entity.id!!,
                    label = entity.label,
                    address = entity.address,
                )
            }
        }
    }
}