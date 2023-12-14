package com.example.myblog.domain.user.model

import com.example.myblog.domain.TimeStamped
import com.example.myblog.domain.post.model.Comment
import com.example.myblog.domain.post.model.Post
import com.example.myblog.domain.user.dto.UserResponseDto
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(name = "user_name", nullable = false)
    var userName: String,

    @Column(name = "user_password", nullable = false)
    var password: String,

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var postList: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var commentList: MutableList<Comment> = mutableListOf()
) : TimeStamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

fun User.toResponse(): UserResponseDto {
    return UserResponseDto(
        id = id!!,
        userName = userName
    )
}