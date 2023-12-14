package com.example.myblog.domain.post.dto

import java.time.LocalDateTime

data class CommentResponseDto(
    val id: Long,
    val author: String,
    val content: String,
    val createdAt: LocalDateTime
)