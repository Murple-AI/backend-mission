package com.murple.murfy.common.exception

import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.ZonedDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = e.message ?: "Invalid request",
            timestamp = ZonedDateTime.now()
        )
        logger.error(e) {"${e.message}: bad request"}
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e: NoSuchElementException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = e.message ?: "Resource not found",
            timestamp = ZonedDateTime.now()
        )
        logger.error(e) {"${e.message}: Not found error"}
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<ErrorResponse> {
        val rootCause = e.rootCause?.message ?: e.message ?: ""

        val userFriendlyMessage = when {
            rootCause.contains("phone_numbers_number_key") -> "Phone number already registered."
            rootCause.contains("users_email_key") -> "Email already registered."
            rootCause.contains("users_username_key") -> "Username already in use."
            rootCause.contains("addresses_address_user_id_key") -> "Address already registered."
            rootCause.contains("unique constraint") -> "Duplicate data exists."
            rootCause.contains("foreign key constraint") -> "Referential integrity constraint violation."
            else -> "Error occurred during data processing."
        }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = userFriendlyMessage,
            timestamp = ZonedDateTime.now()
        )

        logger.error(e) {"Data integrity violation: $rootCause"}

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Server error occurred.",
            timestamp = ZonedDateTime.now()
        )
        logger.error(e) {"server error: ${e.message}"}
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: ZonedDateTime
)
