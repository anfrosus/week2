package com.example.myblog.domain.user.model

import com.example.myblog.domain.TimeStamped
import com.example.myblog.domain.post.model.Comment
import com.example.myblog.domain.post.model.Post
import com.example.myblog.domain.user.UserRoleEnum
import com.example.myblog.domain.user.dto.UserResponseDto
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(name = "user_name", nullable = false, unique = true)
    var userName: String,

    @Column(name = "user_password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    var role: UserRoleEnum

//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
//    var postList: MutableList<Post> = mutableListOf(),
//
//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
//    var commentList: MutableList<Comment> = mutableListOf()

) : TimeStamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

fun User.toResponse(): UserResponseDto {
    return UserResponseDto(
        id = id!!,
        userName = userName,
        userRole = role.toString(),
        refreshToken = null
    )
}

fun User.toResponse(refreshToken: String): UserResponseDto {
    return UserResponseDto(
        id = id!!,
        userName = userName,
        userRole = role.toString(),
        refreshToken = refreshToken
    )
}