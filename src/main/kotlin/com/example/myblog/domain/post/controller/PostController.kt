package com.example.myblog.domain.post.controller

import com.example.myblog.domain.post.dto.CommentRequestDto
import com.example.myblog.domain.post.dto.CommentResponseDto
import com.example.myblog.domain.post.dto.PostRequestDto
import com.example.myblog.domain.post.dto.PostResponseDto
import com.example.myblog.domain.post.service.PostService
import com.example.myblog.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/posts")
@RestController
class PostController(
    private val postService: PostService
) {

    //전체 조회
    @GetMapping
    fun getPostList(): ResponseEntity<List<PostResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getAllPostList())
    }

    //단건 조회
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): ResponseEntity<PostResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostById(postId))
    }

    //게시글 작성
    @PostMapping
    fun createPost(
        @RequestBody postRequest: PostRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PostResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createPost(postRequest, userPrincipal))
    }

    //수정
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postRequest: PostRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PostResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.updatePost(postId, postRequest, userPrincipal))
    }

    //삭제
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(postService.deletePost(postId, userPrincipal))
    }

    //댓글 작성
    @PostMapping("/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentRequest : CommentRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<CommentResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createComment(postId, commentRequest, userPrincipal))
    }
}