package app.boboc.chatserver.controller.advice

import app.boboc.chatserver.exceptions.MissionExceptions
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException

@RestControllerAdvice
class ExceptionHandlingController {
    data class ErrorResponse(
        val message: String,
        val code: String
    )


    companion object{
        val logger = KotlinLogging.logger {}

    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun validationExceptionHandler( e: WebExchangeBindException): ResponseEntity<ErrorResponse> {
        logger.info { "Validation Error: ${e.message.split(';')}" }
        var errorMessage = ""
        e.bindingResult.fieldErrors.forEach { fieldError ->
            errorMessage = fieldError.defaultMessage ?: "Invalid field"
        }


        return ResponseEntity(ErrorResponse(errorMessage, "FORMAT_ERROR"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissionExceptions.MissionException::class)
    fun missionExceptionHandler( e: MissionExceptions.MissionException): ResponseEntity<ErrorResponse> {
        logger.warn { e.message }

        return ResponseEntity(ErrorResponse(e.message, e.code), e.status)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandler( e: Exception): ResponseEntity<ErrorResponse> {
        logger.error { "Unknown Error: ${e.message}" }

        return ResponseEntity(ErrorResponse("UNKNOWN ERROR: Please contact us.", "UNKNOWN_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
