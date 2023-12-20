package com.example.myblog.domain.user.repository

import com.example.myblog.domain.user.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByUserId(userId: Long): RefreshToken?
    fun existsByUserId(userId: Long): Boolean
    fun deleteByUserId(userId: Long)
}