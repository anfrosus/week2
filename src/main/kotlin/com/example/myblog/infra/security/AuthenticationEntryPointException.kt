package com.example.myblog.infra.security

import com.example.myblog.infra.exception.CustomException
import com.example.myblog.infra.exception.ErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthenticationEntryPointException: AuthenticationEntryPoint {
    //security 인증객체가 필요한데 없을 때
    override fun commence(

        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        println("엔투리포인투")
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(
            ObjectMapper().writeValueAsString("엔투리포인투고요 " + authException.message))

    }
}