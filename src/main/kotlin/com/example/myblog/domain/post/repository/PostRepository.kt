package com.example.myblog.domain.post.repository

import com.example.myblog.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByOrderByCreatedDateDesc(): List<Post>
    @Query("select p from Post as p left join fetch p.commentList c order by p.createdDate desc, c.createdDate desc")
    fun findAllPostFetchComment(): MutableList<Post>

    @Query("select p from Post as p left join fetch p.commentList c where p.id = :postId")
    fun findPostByIdFetchComment(postId: Long): Post?

//    @Query("select p from Post p left join fetch p.commentList c order by p.createdDate desc, c.createdDate desc")
//    정렬은 application 단에서 해결하자!
//    fun test(): List<Post>
}