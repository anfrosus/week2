package com.example.myblog.infra.security.jwt

import com.example.myblog.infra.security.UserPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable


class JwtAuthenticationToken(
    private val principal: UserPrincipal,
    //요청한 Adress 정보, SessionId 등을 담음(로깅 용도)
    details: WebAuthenticationDetails
): AbstractAuthenticationToken(principal.authorities), Serializable {//ToDO:Serializable 이란?
//Authentication 의 구현체를 상속
    init {
        //JWT 검증 성공 시 바로 생성할 예정이므로 생성시 authenticated 를 true로
        super.setAuthenticated(true)
        super.setDetails(details)
    }

    //읽어보니 principal이 올바른지 증명할 수 있는건데 보통 비밀번호를 넣고, 아무거나 넣어도 된다(Ojb리턴)
    //우리는 비밀번호 안쓸거니 쓰지말자
    override fun getCredentials() = null

    /*@AuthenticationPrincipal의 리졸버 AuthenticationPrincipalArgumentResolver 는
    SecurityContextHolder.getContext().getAuthentication 후 Authentication 객체의 get principal 로 Object 를 반환하는데
    어노테이션의 반환값으로 이 getPrincipal() Object 를 반환한다.
    따라서 Authentication 객체에 principal 변수명으로 어떠한 객체를 만들어도 꺼내어 사용 가능하다는 뜻.*/
    //Authentication interface 자체가 getPrincipal, getCredentials, getDetails 라는 메소드를 가지고 있네
    //그런데 이제 그 구현체 중 하나인 AbstractAuthenticationToken 객체가 getDetails는 정의를 했고, 나머지 둘을 안해놨네
    //그말은 principal 객체를 자유롭게 쓸 수 있다는 말.
    override fun getPrincipal() = principal

    override fun isAuthenticated(): Boolean {
        return true
    }
}