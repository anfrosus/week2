package com.example.myblog.domain.user.dto

import com.example.myblog.domain.user.model.RefreshToken
import com.example.myblog.domain.user.model.User
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponseDto(
    val id: Long,
    val userName: String,
    val userRole: String,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var refreshToken: String?
) {
}