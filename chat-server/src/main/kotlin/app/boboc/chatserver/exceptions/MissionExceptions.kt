package app.boboc.chatserver.exceptions

import org.springframework.http.HttpStatus

class MissionExceptions {
    abstract class MissionException(
        val status: HttpStatus,
        val code: String,
        override val message: String,
        val ex: Exception? = null
    ) : RuntimeException() {
        constructor(status: HttpStatus, message: String, ex: Exception? = null) : this(
            status,
            status.name,
            message,
            ex
        )
    }

    class BadRequestException(message: String, ex: Exception? = null): MissionException(HttpStatus.BAD_REQUEST, message, ex)
    class NotFoundException(message: String, ex: Exception? = null): MissionException(HttpStatus.NOT_FOUND, message, ex)

    class UserNotFoundException(message: String = "") : MissionException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found. $message")
    class AddressNotFoundException(message: String = "") : MissionException(HttpStatus.NOT_FOUND, "ADDRESS_NOT_FOUND", "Address not found. $message")
    class PhoneNumberNotFoundException(message: String = "") : MissionException(HttpStatus.NOT_FOUND, "PHONE_NUMBER_NOT_FOUND", "PhoneNumber not found. $message")

    class ExceedLimitAddressException(message: String = "") : MissionException(HttpStatus.CONFLICT, "EXCEED_ADDRESS_LIMIT", "Exceed limit of address. $message")
    class ExceedLimitPhoneNumberException(message: String = "") : MissionException(HttpStatus.CONFLICT, "EXCEED_PHONE_NUMBER_LIMIT", "Exceed limit of phone number. $message")

    class InvalidCountryCode(message: String = ""): MissionException(HttpStatus.BAD_REQUEST, "INVALID_COUNTRY_CODE", "Invalid country code. $message")
}
