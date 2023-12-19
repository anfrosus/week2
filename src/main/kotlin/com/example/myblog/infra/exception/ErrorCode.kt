package com.example.myblog.infra.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String
) {
    MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, " 을/(를) 찾을 수 없습니다"),
    ALREADY_EXIST(HttpStatus.CONFLICT, "이/(가) 이미 존재합니다."),
    NOT_MATCH(HttpStatus.BAD_REQUEST, "이/(가) 일치하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "에 대한 권한이 없습니다."),

}