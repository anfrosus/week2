package com.example.myblog.domain.user.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern


data class UserRequestDto(
    @field:NotNull
    @field:Pattern(regexp = "^[a-z0-9]{4,10}$", message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 한다.")
    val userName: String?,

    @field:NotNull
    @field:Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+=-]{4,10}$", message = "최소 4자 이상, 10자 이하이며 알파벳 대소문자(a~z, A~Z), 특수문자로 구성되어야 한다.")
    val password: String?,
    val passwordCheck: String?
)