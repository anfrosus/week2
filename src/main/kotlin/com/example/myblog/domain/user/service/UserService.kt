package com.example.myblog.domain.user.service

import com.example.myblog.domain.user.UserRoleEnum
import com.example.myblog.domain.user.dto.LoginRequestDto
import com.example.myblog.domain.user.dto.RefreshRequestDto
import com.example.myblog.domain.user.dto.UserRequestDto
import com.example.myblog.domain.user.dto.UserResponseDto
import com.example.myblog.domain.user.model.RefreshToken
import com.example.myblog.domain.user.model.User
import com.example.myblog.domain.user.model.toResponse
import com.example.myblog.domain.user.repository.TokenRepository
import com.example.myblog.domain.user.repository.UserRepository
import com.example.myblog.infra.exception.CustomException
import com.example.myblog.infra.exception.ErrorCode
import com.example.myblog.infra.security.UserPrincipal
import com.example.myblog.infra.security.jwt.JwtPlugin
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
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

    @Transactional
    fun login(loginRequest: LoginRequestDto, response: HttpServletResponse): UserResponseDto? {
        val user = userRepository.findUserByUserName(loginRequest.userName)
            ?: throw CustomException("user", ErrorCode.MODEL_NOT_FOUND)
        if (!passwordEncoder.matches(loginRequest.password, user.password)){
            throw CustomException("password", ErrorCode.NOT_MATCH)
        }
        //ToDO: TTL이 관리된다면?? 있으나 마나 로그인 할 때는 새로 발급해줘야하는거아닌가?
        val refreshToken = tokenRepository.findByUserId(user.id!!)
        val refreshTokenValue = jwtPlugin.generateRefreshToken(user)
        if (refreshToken == null) {
            val newRefreshToken = RefreshToken(user, refreshTokenValue)
            tokenRepository.save(newRefreshToken)
        }else {
            refreshToken.tokenValue = refreshTokenValue
            refreshToken.isUsed = false
        }
        jwtPlugin.generateAccessToken(user, response)

            //TTL이 관리된다면 존재한다면 예외를 던져 reissue로 유도?하기엔 isUsed가 true인 경우 문제가 될터.

//        return user.toResponse(refreshTokenValue)
        return user.toResponse(refreshTokenValue)
    }

    @Transactional
    fun reIssue(refreshRequest: RefreshRequestDto, response: HttpServletResponse) : UserResponseDto{
        val user = userRepository.findByIdOrNull(refreshRequest.userId)
            ?: throw CustomException("user", ErrorCode.MODEL_NOT_FOUND)
        val refreshToken = tokenRepository.findByUserId(user.id!!)
            ?: throw CustomException("RefreshToken", ErrorCode.MODEL_NOT_FOUND)
        if (refreshToken.isUsed){
            throw CustomException("이미 사용했으", ErrorCode.FORBIDDEN)
        }
        if (refreshRequest.refreshToken != refreshToken.tokenValue){
            throw CustomException("달라", ErrorCode.FORBIDDEN)
        }
        jwtPlugin.validateToken(refreshToken.tokenValue)
            .onFailure {
                throw CustomException("검증실패", ErrorCode.NOT_MATCH)
            }
            .onSuccess {
                jwtPlugin.generateAccessToken(user, response)
                val refreshTokenValue = jwtPlugin.generateRefreshToken(user)
                refreshToken.tokenValue = refreshTokenValue
                refreshToken.isUsed = true
                return user.toResponse(refreshTokenValue)
            }
        return user.toResponse()
    }
}