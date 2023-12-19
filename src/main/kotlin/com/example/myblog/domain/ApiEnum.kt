package com.example.myblog.domain

import org.springframework.http.HttpMethod

enum class ApiEnum(val method: HttpMethod, val api: String) {

    SIGNUP(HttpMethod.POST, "/api/users/signup"),
    LOGIN(HttpMethod.POST, "/api/users/login"),
    GET_ALL_POST(HttpMethod.GET, "/api/posts"),


}