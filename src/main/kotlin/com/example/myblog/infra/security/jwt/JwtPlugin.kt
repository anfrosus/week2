package com.example.myblog.infra.security.jwt

import com.example.myblog.domain.user.model.User
import com.example.myblog.infra.exception.CustomException
import com.example.myblog.infra.exception.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtPlugin(
    @Value("\${auth.jwt.secret.key}") private val SECRET_KEY: String,
    @Value("\${auth.jwt.exp}") private val accessTokenExpirationHour: Long,
    //Todo
    @Value("24")private val refreshTokenExpirationHour: Long
) {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))

    fun validateToken(accessToken: String): Result<Jws<Claims>> {
        return runCatching {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
            Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken)
        }
    }

    fun generateAccessToken(user: User, response: HttpServletResponse) {
        val generatedToken = generateToken(user, Duration.ofHours(accessTokenExpirationHour))
        setTokenAtHeaderWithBearer(response, generatedToken)
    }

    fun generateRefreshToken(user: User) : String {
        return generateToken(user, Duration.ofHours(refreshTokenExpirationHour))
    }

    fun getTokenFromHeader(request: HttpServletRequest): Result<String> {
//        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
//        return substringToken(bearerToken)
        return kotlin.runCatching {
            val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION) ?: throw CustomException("null", ErrorCode.NOT_MATCH)
            substringToken(bearerToken)
        }
    }


    private fun substringToken(bearerToken: String): String {
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            throw CustomException("bearer", ErrorCode.NOT_MATCH)
        }
        return bearerToken.substring(7)
    }

    fun claimToSet(inputString: String): Set<String> {
        val result = inputString
            .trim('[', ']')
            .split(",")
            .map { it.trim() }
            .toSet()
        return result
    }

    private fun generateToken(user: User, expiration: Duration): String {
        //이것도 사실 claim 으로 넣으면 되긴 함
//        var mutableMap = mutableMapOf<String, MutableSet<String>>()
//        val claims: Claims = Jwts.claims(mutableMap)

//        claims["role"] = mutableSetOf("운영자", "관리자", "일반유저")

        val now = Instant.now()
        Jwts.parser()

        return Jwts.builder()
            .id("jti 사용")
            .issuer("발급자")
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expiration)))
            .subject(user.userName) //여기다 아이디 넣는게 나을듯
            //            .claim("키1", "Value(Obj)")
            .claim("uid", user.id)
            .claim("rol", mutableSetOf(user.role, "ADMIN"))
//            .claims(claims)
            .signWith(key)
            .compact()
    }

    fun setTokenAtHeaderWithBearer(response: HttpServletResponse, token: String){
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
    }



}