package com.bestdriver.aaa_klaxon.network.community

import java.util.UUID

data class PostResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: PostResult
)

data class Post(
    val post_id: Int,
    val user_id: Int,
    val nickname: String,
    val title: String,
    val main_text: String,
    val createdAt: String,
    val like_count: Int,
    val comment_count: Int
)

data class PostRequest(
    val title: String,
    val main_text: String,
    val nickname: String
)

data class Comment(
    val commentId: Int, // comment_id
    val postId: Int,    // post_id
    val userId: Int,    // user_id
    val nickname: String,
    val body: String,
    val createdAt: String // ISO 8601 형식으로 저장된 날짜
)


data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: T? // 제네릭 타입으로 변경
)

// PostsResponse 타입 정의
typealias PostsResponse = ApiResponse<List<PostResult>>

data class PostResult(
    val post_id: Int,
    val user_id: Int,
    val nickname: String,
    val title: String,
    val main_text: String,
    val createdAt: String,
    val like_count: Int, // 추가
    val comment_count: Int // 추가
)

// 요청 본체 데이터 클래스
data class CommentRequest(
    val text: String
)

// CommentResult 정의
data class CommentResult(
    val comment_id: Int,
    val post_id: Int,
    val user_id: Int,
    val nickname: String,
    val text: String,
    val createdAt: String
)

// CommentResponse를 ApiResponse의 형태로 변경
typealias CommentResponse = ApiResponse<CommentResult>

// CommentsResponse를 ApiResponse로 통일
typealias CommentsResponse = ApiResponse<List<CommentResult>>



data class LikeResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String
)

