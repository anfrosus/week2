package com.example.myblog.domain.post.model

import com.example.myblog.domain.TimeStamped
import com.example.myblog.domain.post.dto.PostResponseDto
import com.example.myblog.domain.user.model.User
import jakarta.persistence.*

@Entity
@Table(name = "post")
class Post (

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "author", nullable = false)
    var author: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val commentList: MutableList<Comment> = mutableListOf()

) : TimeStamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun addComment(comment: Comment){
        commentList.add(comment)
    }
}

fun Post.toResponse(): PostResponseDto {
    return PostResponseDto(
        id = id!!,
        title = title,
        content = content,
        authorName = author,
        commentList = commentList.map { it.toResponse() },
        createdAt = createdDate!!
    )
}