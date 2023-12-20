package com.example.myblog.domain.post.controller

import com.example.myblog.domain.post.dto.CommentRequestDto
import com.example.myblog.domain.post.dto.CommentResponseDto
import com.example.myblog.domain.post.dto.PostRequestDto
import com.example.myblog.domain.post.dto.PostResponseDto
import com.example.myblog.domain.post.service.PostService
import com.example.myblog.domain.user.UserRoleEnum
import com.example.myblog.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "게시글")
@RequestMapping("/api/posts")
@RestController
class PostController(
    private val postService: PostService,
) {
    @Operation(summary = "목록 조회")
    @GetMapping
    fun getPostList(): ResponseEntity<List<PostResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getAllPostList())
    }

    @Operation(summary = "게시글 조회")
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): ResponseEntity<PostResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPostById(postId))
    }

    @Operation(summary = "게시글 작성")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    @Secured(UserRoleEnum.USER.name)
    @PostMapping
    fun createPost(
        @RequestBody postRequest: PostRequestDto,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PostResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(postService.createPost(postRequest, userPrincipal))
    }

    @Operation(summary = "게시글 수정")
//    @PreAuthorize("hasRole('')")
//    @Secured(UserRoleEnum.USER, UserRoleEnum.ADMIN)
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

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(postService.deletePost(postId, userPrincipal))
    }

    @Operation(summary = "댓글 작성")
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