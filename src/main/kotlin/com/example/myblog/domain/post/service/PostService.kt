package com.example.myblog.domain.post.service

import com.example.myblog.domain.post.dto.CommentRequestDto
import com.example.myblog.domain.post.dto.CommentResponseDto
import com.example.myblog.domain.post.dto.PostRequestDto
import com.example.myblog.domain.post.dto.PostResponseDto
import com.example.myblog.domain.post.model.Comment
import com.example.myblog.domain.post.model.Post
import com.example.myblog.domain.post.model.toResponse
import com.example.myblog.domain.post.repository.CommentRepository
import com.example.myblog.domain.post.repository.PostRepository
import com.example.myblog.domain.user.repository.UserRepository
import com.example.myblog.infra.exception.CustomException
import com.example.myblog.infra.exception.ErrorCode
import com.example.myblog.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    val postRepository: PostRepository,
    val userRepository: UserRepository,
    val commentRepository: CommentRepository
) {

    //전체 조회
    @Transactional(readOnly = true)
    fun getAllPostList(): List<PostResponseDto> {
        var postList = postRepository.findAllPostFetchComment()
        return postList.map { it.toResponse() }
    }

    //단건 조회
    @Transactional(readOnly = true)
    fun getPostById(postId: Long): PostResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException("post", ErrorCode.MODEL_NOT_FOUND)
        post.commentList.reverse()
        return post.toResponse()
    }

    //게시글 작성
    @Transactional
    fun createPost(postRequestDto: PostRequestDto, userPrincipal: UserPrincipal): PostResponseDto {
        val currentUser = userRepository.findByIdOrNull(userPrincipal.id)
            ?: throw CustomException("user", ErrorCode.MODEL_NOT_FOUND)
        val post = Post(postRequestDto.title, postRequestDto.content, currentUser.userName, currentUser)

        return postRepository.save(post).toResponse()
    }

    //수정
    @Transactional
    fun updatePost(postId: Long, postRequestDto: PostRequestDto, userPrincipal: UserPrincipal): PostResponseDto {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException("post", ErrorCode.MODEL_NOT_FOUND)
        if (post.user.id != userPrincipal.id) {
            throw CustomException("post", ErrorCode.FORBIDDEN)
        }
        val (title, content) = postRequestDto

        post.title = title
        post.content = content
        
        return postRepository.save(post).toResponse()
    }

    //삭제
    @Transactional
    fun deletePost(postId: Long, userPrincipal: UserPrincipal): String {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException("post", ErrorCode.MODEL_NOT_FOUND)
        if (post.user.id != userPrincipal.id) {
            throw CustomException("post", ErrorCode.FORBIDDEN)
        }
        postRepository.delete(post)
        return "게시글 삭제 완료"
    }

    @Transactional
    fun createComment(postId: Long, commentRequest: CommentRequestDto, userPrincipal: UserPrincipal): CommentResponseDto {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw CustomException("post", ErrorCode.MODEL_NOT_FOUND)
        val user = userRepository.findByIdOrNull(userPrincipal.id)
            ?: throw CustomException("user", ErrorCode.MODEL_NOT_FOUND)
        var comment = Comment(
            content = commentRequest.content,
            author = user.userName,
            user = user,
            post = post
        )
        return commentRepository.save(comment).toResponse()
    }


}
