package com.example.myblog.domain.user.service

import com.example.myblog.domain.user.UserRoleEnum
import com.example.myblog.domain.user.dto.LoginRequestDto
import com.example.myblog.domain.user.dto.UserRequestDto
import com.example.myblog.domain.user.dto.UserResponseDto
import com.example.myblog.domain.user.model.User
import com.example.myblog.domain.user.model.toResponse
import com.example.myblog.domain.user.repository.UserRepository
import com.example.myblog.infra.exception.CustomException
import com.example.myblog.infra.exception.ErrorCode
import com.example.myblog.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtPlugin: JwtPlugin
) {

    @Transactional
    fun signUp(userRequest: UserRequestDto): UserResponseDto {
        if (userRequest.password != userRequest.passwordCheck){
            throw CustomException("password", ErrorCode.NOT_MATCH)
        }

        if (userRepository.existsByUserName(userRequest.userName!!)){
            throw CustomException("user", ErrorCode.ALREADY_EXIST)
        }

        val userName = userRequest.userName
        val encodedPassword = passwordEncoder.encode(userRequest.password)
        val newUser = User(userName, encodedPassword, UserRoleEnum.USER)

        return userRepository.save(newUser).toResponse()
    }

    @Transactional(readOnly = true)
    fun login(loginRequest: LoginRequestDto, response: HttpServletResponse): UserResponseDto? {
        val user = userRepository.findUserByUserName(loginRequest.userName)
            ?: throw CustomException("user", ErrorCode.MODEL_NOT_FOUND)
        if (!passwordEncoder.matches(loginRequest.password, user.password)){
            throw CustomException("password", ErrorCode.NOT_MATCH)
        }
        jwtPlugin.generateAccessToken(user, response)

        return user.toResponse()

    }
}