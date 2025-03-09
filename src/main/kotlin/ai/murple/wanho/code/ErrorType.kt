package ai.murple.wanho.code

import org.springframework.http.HttpStatus

enum class ErrorType(val status: HttpStatus, val message: String) {
    // ERROR
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),


    // VALID
    REQUIRED_USER_NAME(HttpStatus.BAD_REQUEST, "User name is required."),
    NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "Name too long."),
    INVALID_AGE(HttpStatus.BAD_REQUEST, "Invalid age"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Invalid email format."),
    EMAIL_TOO_LONG(HttpStatus.BAD_REQUEST, "Email too long."),
    INVALID_SEX(HttpStatus.BAD_REQUEST, "Invalid sex type."),
    TOO_MANY_TELEPHONES(HttpStatus.BAD_REQUEST, "Telephones are too many numbers."),
    INVALID_TEL_E164(HttpStatus.BAD_REQUEST, "Invalid TEL E164."),
    TOO_LONG_TELEPHONE(HttpStatus.BAD_REQUEST, "Too long telephone."),
    TOO_MANY_ADDRESSES(HttpStatus.BAD_REQUEST, "addresses are too many addresses."),
    TOO_LONG_ADDRESS(HttpStatus.BAD_REQUEST, "addresses are too long."),

}