package app.boboc.chatserver.dto

import app.boboc.chatserver.data.CountryCode
import app.boboc.chatserver.data.GenderType
import app.boboc.chatserver.entity.UserAddressEntity
import app.boboc.chatserver.entity.UserEntity
import app.boboc.chatserver.entity.UserPhoneNumberEntity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import org.hibernate.validator.constraints.Length

class Requests {
    data class User(
        @field:Length(min = 1, max = 1024)
        val name: String,
        @field:Email(message = "Invalid email format")
        @field:Length(min = 1, max = 1024)
        val email: String,
        @field:Positive(message = "Age must be positive")
        val age: Int? = null,
        val gender: GenderType? = null,
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
                    age = user.age,
                    gender = user.gender,
                    addresses = addresses.map { Address.from(it) },
                    phoneNumbers = phoneNumbers.map { PhoneNumber.from(it) }
                )
            }
        }
    }

    data class PhoneNumber(
        val label: String,
        @field:Length(min = 2, max = 2)
        val countryCode: CountryCode? = null,
        @field:Pattern(regexp = "^\\+\\[1-9]\\d{1,14}\$", message = "Invalid phone number format")
        val phoneNumber: String,
        val isVerified: Boolean
    ) {
        companion object {
            fun from(entity: UserPhoneNumberEntity): PhoneNumber {
                return PhoneNumber(
                    label = entity.label,
                    countryCode = entity.countryCode,
                    phoneNumber = entity.phoneNumber,
                    isVerified = entity.isVerified,
                )
            }
        }
    }

    data class Address(
        val label: String,
        @field:Length(min = 1, max = 1024)
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

}

