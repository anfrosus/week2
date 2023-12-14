package com.example.myblog.infra.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<String> {
        return ResponseEntity
            .status(e.errorCode.httpStatus)
            .body(e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity
            .status(e.statusCode)
            .body(e.fieldErrors.map {
                it.field + it.defaultMessage
            }.toString())
    }


}