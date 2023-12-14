package com.example.myblog.infra.security.jwt

import com.example.myblog.infra.security.UserPrincipal
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.SecurityContextHolderFilter
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = jwtPlugin.getTokenFromHeader(request)
        if (jwt != null) { //TODO : 어차피 인증필요하면 인증객체가 필요한데 토큰 없으면 인증객체 인증못해서 거기서 예외발생함
            jwtPlugin.validateToken(jwt).onSuccess {

                val principal = UserPrincipal(
                    id = it.body["uid"].toString().toLong(),
                    userName = it.body["unm"].toString(),
                    authorities = mutableSetOf(SimpleGrantedAuthority(it.body["role"].toString()))
                )

                //유저 정보 획득 후 Authentication 객체생성
                val authentication = JwtAuthenticationToken(
                    principal = principal,
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                )

                //security Context 에 set
                SecurityContextHolder.getContext().authentication = authentication
            }
            
        }
        filterChain.doFilter(request, response)
        //TODO: 실패시에는 어떤 exception 발생시키는지 확인하고 기간 만료시를 찾아내자
    }


}