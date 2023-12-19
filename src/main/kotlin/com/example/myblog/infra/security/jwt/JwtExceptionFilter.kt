package com.example.myblog.infra.security.jwt

import com.example.myblog.infra.exception.CustomException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

//@Component
//class JwtExceptionFilter: GenericFilterBean() {
//    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
//        try {
//            chain!!.doFilter(request, response)
//        }catch (e: CustomException){
//            println("엔투리포인투")
//            response.status = HttpStatus.UNAUTHORIZED.value()
//            response.contentType = MediaType.APPLICATION_JSON_VALUE
//            response.characterEncoding = "UTF-8"
//            response.writer.write(
//                ObjectMapper().writeValueAsString("엔투리포인투고요 " + authException.message))
//        }
//    }
//}