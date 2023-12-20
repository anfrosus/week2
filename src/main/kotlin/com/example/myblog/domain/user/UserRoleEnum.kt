package com.example.myblog.domain.user

import org.springframework.security.core.GrantedAuthority

enum class UserRoleEnum {
    USER,
    ADMIN;

//    USER(UserRoles.USER),
//    ADMIN(UserRoles.ADMIN);

//    companion object ROLES {
//        const val USER = "USER"
//        const val ADMIN = "ADMIN"
//    }

}
//Ambigiuos access to companion's property 'ADMIN' in enum is deprecated. please add explicit Companion qualifier to the class name
//이런 경고가 나타나는 이유는 코틀린 1.5.0 버전에서 동반 객체의 프로퍼티에 대한 접근 방법이 변경되었기 때문
//해결 방법은 경고 메시지에서 제안한 대로 명시적인 동반 객체 지정자를 클래스 이름 뒤에 추가하는 것
//object UserRoles {
//    const val USER = "USER"
//    const val ADMIN = "ADMIN"
//}