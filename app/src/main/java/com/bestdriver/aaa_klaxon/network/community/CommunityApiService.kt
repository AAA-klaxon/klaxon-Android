package com.bestdriver.aaa_klaxon.network.community

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CommunityApiService {
    @GET("/community/posts")
    suspend fun getPosts(): Response<PostsResponse>

    @POST("/community/posts")
    suspend fun createPost(
        @Body post: PostRequest
    ): Response<PostResponse>

    @GET("/community/posts/{postId}")
    suspend fun getPostById(
        @Path("postId") postId: Int
    ): Response<PostResponse>

    @POST("/community/posts/{postId}/comments")
    suspend fun addComment(
        @Path("postId") postId: Int,
        @Body body: CommentRequest
    ): Response<CommentResponse>

    @GET("/community/posts/{postId}/comments")
    suspend fun getCommentsByPostId(
        @Path("postId") postId: Int
    ): Response<CommentsResponse>

    @POST("/community/posts/{postId}/likes")
    suspend fun addLike(
        @Path("postId") postId: Int
    ): Response<LikeResponse>

    @DELETE("/community/posts/{postId}/likes")
    suspend fun removeLike(
        @Path("postId") postId: Int
    ): Response<LikeResponse>
}
