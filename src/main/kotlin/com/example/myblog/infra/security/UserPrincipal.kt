package com.example.myblog.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val userName: String,
    //AbstractAuth~추상클래스가 아래 타입으로 프로퍼티를 가지고있다.
    val authorities: MutableCollection<GrantedAuthority>
) {

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

}