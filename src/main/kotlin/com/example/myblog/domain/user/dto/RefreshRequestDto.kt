package com.example.myblog.domain.user.dto

import org.springframework.web.bind.annotation.RequestBody

data class RefreshRequestDto(
    val userId: Long,
    val refreshToken: String
)