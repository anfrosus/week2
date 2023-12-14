package com.example.myblog.domain.post.dto

import java.time.LocalDateTime

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val authorName: String,
    val commentList: List<CommentResponseDto>,
    val createdAt: LocalDateTime
)