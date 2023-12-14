package com.example.myblog.domain.post.repository

import com.example.myblog.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
    fun findAllByOrderByCreatedDateDesc(): List<Post>
}