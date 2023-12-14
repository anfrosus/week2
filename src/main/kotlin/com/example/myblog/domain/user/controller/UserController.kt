package com.example.myblog.domain.user.controller

import com.example.myblog.domain.user.dto.LoginRequestDto
import com.example.myblog.domain.user.dto.UserRequestDto
import com.example.myblog.domain.user.dto.UserResponseDto
import com.example.myblog.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "회원")
@RequestMapping("/api/users")
@RestController
class UserController(
    val userService : UserService
) {
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid userRequest : UserRequestDto): ResponseEntity<UserResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.signUp(userRequest))
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDto : LoginRequestDto,
        response: HttpServletResponse): ResponseEntity<UserResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.login(loginRequestDto, response))
    }

}