package com.example.myblog.infra.security

import com.example.myblog.domain.ApiEnum
import com.example.myblog.infra.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
) {


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            // BasicAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .httpBasic { it.disable() }
            // UsernamePasswordAuthenticationFilter, DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter 제외
            .formLogin { it.disable() }
            // CsrfFilter 제외
            .csrf { it.disable() }
            .authorizeHttpRequests {
//                it.anyRequest().permitAll()
                it.requestMatchers(
                    "/api/users/login",
                    "/api/users/signup",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/h2/**",
                    "/error"
//                    "api/users/reissue"
                ).permitAll()
                    .requestMatchers(
                        ApiEnum.REISSUE.method, ApiEnum.REISSUE.api
                    ).permitAll()
//                    .requestMatchers(
//                        HttpMethod.GET, "/api/posts/**",
//                        ApiEnum.REISSUE.method, ApiEnum.REISSUE.api
//                    ).permitAll()
//                it.requestMatchers(
//                    "api"
//                ).hasRole(UserRoleEnum.ADMIN.toString())
                    .anyRequest().authenticated()
            }
            //jwtAuthFilter 추가
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .addFilterBefore(jwtExceptionFilter, JwtAuthFilter::class.java)

            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }

            .headers { it.disable() } //for h2
            .build()
    }
}