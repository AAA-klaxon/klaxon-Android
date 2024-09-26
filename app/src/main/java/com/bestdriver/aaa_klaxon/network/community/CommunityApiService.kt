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
    @GET("community/posts")
    suspend fun getPosts(
        @Header("Authorization") token: String
    ): Response<PostsResponse>



    @POST("community/posts")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body post: PostRequest
    ): Response<PostResponse>



    @GET("/community/posts/{postId}")
    suspend fun getPostById(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int
    ): Response<PostResponse>


    @POST("/community/posts/{postId}/comments")
    suspend fun addComment(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int,
        @Body body: CommentRequest
    ): Response<CommentResponse>


    @GET("/community/posts/{postId}/comments")
    suspend fun getCommentsByPostId(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int
    ): Response<CommentsResponse>


    @POST("community/posts/{postId}/likes")
    suspend fun addLike(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int
    ): Response<LikeResponse>


    @DELETE("community/posts/{postId}/likes")
    suspend fun removeLike(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int
    ): Response<LikeResponse>

}