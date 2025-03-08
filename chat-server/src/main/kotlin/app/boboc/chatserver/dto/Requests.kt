package app.boboc.chatserver.dto

import app.boboc.chatserver.data.CountryCode
import app.boboc.chatserver.data.GenderType
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
    )

    data class PhoneNumber(
        val label: String,
        val countryCode: CountryCode? = null,
        @field:Pattern(regexp = "\\+[1-9]\\d{1,14}", message = "Invalid phone number format")
        val phoneNumber: String,
        val isVerified: Boolean = false
    )

    data class Address(
        val label: String,
        @field:Length(min = 1, max = 1024)
        val address: String,
    )

}
