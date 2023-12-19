package com.example.myblog.domain.user.dto

import com.example.myblog.domain.user.model.User

data class UserResponseDto(
    val id: Long,
    val userName: String,
    val userRole: String
) {
    constructor(user: User) : this(user.id!!, user.userName, user.role.toString())
}