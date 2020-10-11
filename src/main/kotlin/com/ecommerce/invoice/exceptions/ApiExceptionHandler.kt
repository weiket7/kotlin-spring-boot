package com.ecommerce.invoice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.ZoneId
import java.time.ZonedDateTime
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(value = [(ApiException::class)])
    fun handleApiRequestException(e:ApiException, webRequest:WebRequest):ResponseEntity<Any> {
        val badRequest = HttpStatus.BAD_REQUEST
        val apiException = ApiExceptionResponse(
                e.message,
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        )
        return ResponseEntity(apiException, badRequest)
    }
}
