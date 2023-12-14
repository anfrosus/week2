package com.example.myblog.domain.user.repository

import com.example.myblog.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByUserName(userName: String): Boolean
    fun findUserByUserName(userName: String): User?
}