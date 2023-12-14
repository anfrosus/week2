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
    @Value("\${auth.jwt.secret.key}") private val secretKey: String,
    @Value("\${auth.jwt.exp}") private val accessTokenExpirationHour: Long,
) {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    //TODO: RESULT 클래스란? try catch 대신 사용했다고 하는데 잘 이해가 안간다.
    fun validateToken(accessToken: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
        }
    }

    fun generateAccessToken(user: User, response: HttpServletResponse) {
        val generatedToken = generateToken(user, Duration.ofHours(accessTokenExpirationHour))
        setTokenAtHeaderWithBearer(response, generatedToken)
    }

    fun getTokenFromHeader(request: HttpServletRequest): String? {
        //Todo 필터 예외처리 ? 필터에 uri 안넣으면 널포인터 뜨는디 그러면 걍 null리턴하자
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return substringToken(bearerToken)
    }

    private fun substringToken(bearerToken: String): String {
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            //ToDO 필터 예외처리
            throw CustomException("auth", ErrorCode.NOT_MATCH)
        }
        return bearerToken.substring(7)
    }

    private fun generateToken(user: User, expiration: Duration): String {
        //이것도 사실 claim 으로 넣으면 되긴 함
        var mutableMap = mutableMapOf<String, Any>()
        val claims: Claims = Jwts.claims(mutableMap)
        claims["role"] = mutableSetOf("운영자", "관리자", "일반유저")

        val now = Instant.now()

        return Jwts.builder()
            .setSubject("토큰 제목")
            .setId("jti??")
            .setIssuer("발급자")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(expiration)))
//            .claim("키1", "Value(Obj)")
            .claim("uid", user.id)
            .claim("unm", user.userName)
            .addClaims(claims)
            .signWith(key)
            .compact()
    }

    fun setTokenAtHeaderWithBearer(response: HttpServletResponse, token: String){
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
    }


}