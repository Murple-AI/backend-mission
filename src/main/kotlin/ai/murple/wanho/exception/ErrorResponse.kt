package ai.murple.wanho.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val status: HttpStatus,
    val error: String,
    val message: String,
)
