package com.example.myblog.infra.security.jwt

import com.example.myblog.domain.ApiEnum
import com.example.myblog.infra.security.UserPrincipal
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        //어찌됐건 간에 uri쭉 훑고 boolean만 리턴하면 돼
        ApiEnum.entries.map {
            if (it.api == request.requestURI && it.method.toString() == request.method){
                return true
            }
        }
        return false
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("아니 이거 자체를 타면 안되는디?")
//        val jwt = jwtPlugin.getTokenFromHeader(request)
        jwtPlugin.getTokenFromHeader(request)
            .onSuccess {token ->
                jwtPlugin.validateToken(token)
                    .onSuccess {claims ->
                        setAuthentication(claims, request)
//                        filterChain.doFilter(request, response)
                    }
                    .onFailure { exception ->
                        if (exception is ExpiredJwtException) {
                            filterExceptionHandler(exception, response)
                            return
//                            TODO("리프레쉬 토큰 재발급") 이렇게 할 필요없이 reissue api따로 두면되지
//                            filterChain.doFilter(request, response)
                        } else {
                            filterExceptionHandler(exception, response)
                            return
//                            filterChain.doFilter(request,response)
                        }
                    }
            }
            .onFailure {
                //ToDO:
                println("여기 탔지?")
                filterExceptionHandler(it, response)
                return
//                filterChain.doFilter(request, response)
            }
        println("마지막")
        filterChain.doFilter(request, response)

    }

    private fun setAuthentication(claims: Jws<Claims>, request: HttpServletRequest) {

        val principal = UserPrincipal(
            id = claims.body["uid"].toString().toLong(),
            userName = claims.body.subject,
            roles = jwtPlugin.claimToSet(claims.body["rol"].toString())
        )

        //유저 정보 획득 후 Authentication 객체생성
        val authentication = JwtAuthenticationToken(
            principal = principal,
            details = WebAuthenticationDetailsSource().buildDetails(request)
        )

        //security Context 에 set
        SecurityContextHolder.getContext().authentication = authentication
        //TODO:지우기
        println("여기까지 잘타는겨?")
        println(authentication.authorities)
    }


    private fun filterExceptionHandler(exception: Throwable, response: HttpServletResponse) {
        response.status = HttpStatus.BAD_REQUEST.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        if (exception is JwtException) {
            response.writer.write(
                ObjectMapper().writeValueAsString("또끈에 문제가 있어용 " + exception.message)
            )
        } else {
            println("여기 타는감??")
            response.writer.write(
                ObjectMapper().writeValueAsString( "뭔지몰랑" + exception.message)
            )
        }
    }



}