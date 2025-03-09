package ai.murple.wanho.exception

import ai.murple.wanho.code.ErrorType
import org.springframework.http.HttpStatus

class CustomException(errorType: ErrorType) : RuntimeException(errorType.message) {
    val status: HttpStatus = errorType.status
}