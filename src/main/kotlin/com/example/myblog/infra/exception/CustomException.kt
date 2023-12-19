package com.example.myblog.infra.exception

data class CustomException(
    val modelName: String, val errorCode: ErrorCode
) : RuntimeException(modelName + errorCode.message) {
}