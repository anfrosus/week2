package com.example.myblog.infra.security

import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(
    val id: Long,
    val userName: String,
    //AbstractAuth~추상클래스가 final 로 authorities 를 가지고있다.
    //따라서 Authentication 을구현한 AbstractAuthenticationToken 을 상속할 때 생성인자에 아래 프로퍼티를 넘겨준다.
    val authorities: MutableCollection<GrantedAuthority>
    //만약 roles 와 구분하려면 ROLES 안붙이고 넣으면 됨.
){

//    constructor(id: Long, userName: String, roles: MutableSet<String>) : this(
//        id,
//        userName,
//        roles?.map {
//            SimpleGrantedAuthority("ROLE_$it")
//        }.toMutableSet()
//    )

    constructor(id: Long, userName: String, roles: Set<String>) : this(
        id,
        userName,
        roles.map {
            println(it)
            SimpleGrantedAuthority("ROLE_$it")
        }.toMutableSet()
    )



    //UserDetails 를 구현하면 아래 메서드를 override 해야하는데
//    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        TODO("Not yet implemented")
//    }

}