package app.boboc.chatserver.dto

import app.boboc.chatserver.data.GenderType
import app.boboc.chatserver.entity.UserAddressEntity
import app.boboc.chatserver.entity.UserEntity
import app.boboc.chatserver.entity.UserPhoneNumberEntity
import jakarta.validation.constraints.Email

data class User(
    val name: String,
    @field:Email
    val email: String,
    val gender: GenderType,
    val addresses: List<Address>,
    val phoneNumbers: List<PhoneNumber>
) {
    companion object {
        fun from(
            user: UserEntity,
            addresses: List<UserAddressEntity> = listOf(),
            phoneNumbers: List<UserPhoneNumberEntity> = listOf()
        ): User {
            return User(
                name = user.name,
                email = user.email,
                gender = user.gender,
                addresses = addresses.map { Address.from(it) },
                phoneNumbers = phoneNumbers.map { PhoneNumber.from(it) }
            )
        }
    }
}

data class PhoneNumber(
    val label: String,
    val countryCode: String,
    val phoneNumber: String,
) {
    companion object {
        fun from(entity: UserPhoneNumberEntity): PhoneNumber {
            return PhoneNumber(
                label = entity.label,
                countryCode = entity.countryCode,
                phoneNumber = entity.phoneNumber
            )
        }
    }
}

data class Address(
    val label: String,
    val address: String,
) {
    companion object {
        fun from(entity: UserAddressEntity): Address {
            return Address(
                label = entity.label,
                address = entity.address,
            )
        }
    }
}
