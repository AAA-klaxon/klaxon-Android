package com.bestdriver.aaa_klaxon.network.community

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestdriver.aaa_klaxon.network.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
class CommunityWriteScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> get() = _posts

    private val _comments = MutableStateFlow<Map<Int, List<Comment>>>(emptyMap())
    val comments: StateFlow<Map<Int, List<Comment>>> get() = _comments

    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> get() = _selectedPost

    private val apiService: CommunityApiService
    private val tokenManager: TokenManager

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.202.104.135:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(CommunityApiService::class.java)
        tokenManager = TokenManager(application) // Application context를 전달

        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            val token = tokenManager.getToken() // 저장된 토큰을 가져옴
            if (token == null) {
                Log.e("CommunityViewModel", "Token is null")
                return@launch
            }
            try {
                val response = apiService.getPosts(token)
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val postsResult = response.body()?.result
                    Log.d("CommunityViewModel", "Fetched posts: $postsResult")

                    postsResult?.let { postResults ->
                        _posts.value = postResults.map { postResult ->
                            Post(
                                post_id = postResult.post_id,
                                user_id = postResult.user_id,
                                nickname = postResult.nickname,
                                title = postResult.title ?: "",
                                main_text = postResult.main_text ?: "",
                                createdAt = postResult.createdAt,
                                like_count = postResult.like_count,
                                comment_count = postResult.comment_count
                            )
                        }
                    } ?: run {
                        Log.d("CommunityViewModel", "No posts found")
                        _posts.value = emptyList()
                    }
                } else {
                    Log.e("CommunityViewModel", "게시글 조회 오류: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "게시글 조회 중 예외 발생: ${e.localizedMessage}")
            }
        }
    }

    suspend fun fetchPostById(postId: Int) {
        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return
        }
        try {
            val response = apiService.getPostById(token, postId)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                response.body()?.result?.let { result ->
                    _selectedPost.value = Post(
                        post_id = result.post_id,
                        user_id = result.user_id,
                        nickname = result.nickname,
                        title = result.title,
                        main_text = result.main_text,
                        createdAt = result.createdAt,
                        like_count = 0,
                        comment_count = 0
                    )
                }
            } else {
                Log.e("CommunityViewModel", "Error fetching post: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception fetching post: ${e.localizedMessage}")
        }
    }

    suspend fun addPost(title: String, body: String, nickname: String): Int? {
        var newPostId: Int? = null

        if (title.isBlank() || body.isBlank()) {
            Log.e("CommunityViewModel", "제목이나 본문이 비어있습니다.")
            return null
        }

        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return null
        }

        try {
            val postRequest = PostRequest(
                title = title,
                main_text = body,
                nickname = nickname
            )

            val response = apiService.createPost(token, postRequest)

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val result = response.body()?.result

                result?.let {
                    newPostId = it.post_id
                    _posts.update { currentPosts ->
                        currentPosts + Post(
                            post_id = newPostId!!,
                            user_id = it.user_id,
                            nickname = it.nickname,
                            title = it.title,
                            main_text = it.main_text,
                            createdAt = it.createdAt,
                            like_count = 0,
                            comment_count = 0
                        )
                    }
                    _comments.update { currentComments ->
                        currentComments + (newPostId!! to emptyList())
                    }
                } ?: run {
                    Log.e("CommunityViewModel", "Result is null")
                }
            } else {
                Log.e("CommunityViewModel", "게시글 작성 오류: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "게시글 작성 중 예외 발생: ${e.localizedMessage}", e)
        }
        return newPostId
    }

    suspend fun addComment(postId: Int, request: CommentRequest): Boolean {
        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return false
        }

        try {
            Log.d("CommunityViewModel", "CommentRequest: $request")

            val response = apiService.addComment(token, postId, request)

            if (response.isSuccessful) {
                val commentResponse = response.body()
                if (commentResponse != null && commentResponse.isSuccess) {
                    val result = commentResponse.result

                    result?.let {
                        val newComment = Comment(
                            commentId = it.comment_id,
                            postId = it.post_id,
                            userId = it.user_id,
                            nickname = it.nickname,
                            body = it.text,
                            createdAt = it.createdAt
                        )

                        _comments.update { currentComments ->
                            val postComments = currentComments[postId]?.toMutableList() ?: mutableListOf()
                            postComments.add(newComment)
                            currentComments + (postId to postComments)
                        }
                    } ?: run {
                        Log.e("CommunityViewModel", "Result is null")
                    }
                    return true // 댓글 추가 성공
                } else {
                    Log.e("CommunityViewModel", "Error adding comment: ${commentResponse?.code} - ${commentResponse?.message}")
                }
            } else {
                Log.e("CommunityViewModel", "Error adding comment: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception adding comment: ${e.localizedMessage}")
        }
        return false // 댓글 추가 실패
    }


    suspend fun fetchCommentsForPost(postId: Int) {
        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return
        }

        try {
            val response = apiService.getCommentsByPostId(token, postId)
            if (response.isSuccessful) {
                val commentsResponse = response.body() as? CommentsResponse
                if (commentsResponse != null && commentsResponse.isSuccess) {
                    val commentResults: List<CommentResult> = commentsResponse.result ?: emptyList()

                    val newComments = commentResults.map { commentResult ->
                        Comment(
                            commentId = commentResult.comment_id,
                            postId = commentResult.post_id,
                            userId = commentResult.user_id,
                            nickname = "User ${commentResult.user_id}",
                            body = commentResult.text,
                            createdAt = commentResult.createdAt
                        )
                    }

                    _comments.update { currentComments ->
                        val updatedComments = (currentComments[postId]?.toMutableList() ?: mutableListOf()).apply {
                            addAll(newComments)
                        }
                        currentComments + (postId to updatedComments)
                    }
                } else {
                    Log.e("Comments", "Error fetching comments: ${commentsResponse?.message ?: "Unknown error"}")
                }
            } else {
                Log.e("Comments", "Error fetching comments: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Comments", "Exception fetching comments: ${e.localizedMessage}")
        }
    }

    fun getCommentsForPost(postId: Int): List<Comment> {
        return _comments.value[postId] ?: emptyList()
    }

    fun getMostLikedPost(): Post? {
        return _posts.value.maxByOrNull { it.like_count }
    }

    suspend fun addLike(postId: Int) {
        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return
        }

        try {
            val response = apiService.addLike(token, postId)

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                updateLikeCount(postId, true)
            } else {
                Log.e("CommunityViewModel", "Error adding like: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception adding like: ${e.localizedMessage}")
        }
    }

    suspend fun removeLike(postId: Int) {
        val token = tokenManager.getToken() // 저장된 토큰을 가져옴
        if (token == null) {
            Log.e("CommunityViewModel", "Token is null")
            return
        }

        try {
            val response = apiService.removeLike(token, postId)

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                updateLikeCount(postId, false)
            } else {
                Log.e("CommunityViewModel", "Error removing like: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception removing like: ${e.localizedMessage}")
        }
    }

    private fun updateLikeCount(postId: Int, increment: Boolean) {
        _posts.update { currentPosts ->
            currentPosts.map { post ->
                if (post.post_id == postId) {
                    post.copy(like_count = post.like_count + if (increment) 1 else -1)
                } else {
                    post
                }
            }
        }
    }
}




