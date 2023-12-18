package com.example.myblog.domain.post.model

import com.example.myblog.domain.TimeStamped
import com.example.myblog.domain.post.dto.CommentResponseDto
import com.example.myblog.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "author", nullable = false)
    var author: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "post_id", nullable = false)
    var post: Post

) : TimeStamped(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null

    init {
        post.addComment(this)
    }

}

fun Comment.toResponse() : CommentResponseDto{
    return CommentResponseDto(
        id = id!!,
        author = author,
        content = content,
        createdAt = LocalDateTime.now()
    )
}