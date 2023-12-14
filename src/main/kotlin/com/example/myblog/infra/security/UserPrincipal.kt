package com.example.myblog.infra.security

import org.springframework.security.core.GrantedAuthority

data class UserPrincipal (
    val id: Long,
    val userName: String,
    //AbstractAuth~추상클래스가 아래 타입으로 프로퍼티를 가지고있다.
    val authorities: MutableCollection<GrantedAuthority>
)