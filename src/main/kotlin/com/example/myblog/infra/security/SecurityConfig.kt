package com.example.myblog.infra.security

import com.example.myblog.infra.security.jwt.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
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
                    "/h2/**"
                ).permitAll()
                    .requestMatchers(
                        HttpMethod.GET,"/api/posts/**",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            //안넣으면 안타네
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

            .headers { it.disable() } //for h2
            .build()
    }
}