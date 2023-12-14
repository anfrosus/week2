package com.example.myblog.domain.post.dto

data class PostRequestDto(
    //TODO: 모든 Request valid, 예외처리
    val title: String,
    val content: String
)