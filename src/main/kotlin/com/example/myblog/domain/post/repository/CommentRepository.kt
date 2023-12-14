package com.example.myblog.domain.post.repository

import com.example.myblog.domain.post.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
}