package com.ecommerce.invoice.exceptions
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

class ApiExceptionResponse(
        val message: String?,
        val httpStatus: HttpStatus,
        val timestamp: ZonedDateTime
)