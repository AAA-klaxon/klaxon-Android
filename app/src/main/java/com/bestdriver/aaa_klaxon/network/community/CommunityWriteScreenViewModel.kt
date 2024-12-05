package com.bestdriver.aaa_klaxon.network.community

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestdriver.aaa_klaxon.network.RetrofitClient
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

    // 좋아요 상태와 개수 관리
    private val _likeStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val likeStates: StateFlow<Map<Int, Boolean>> get() = _likeStates

    private val apiService: CommunityApiService = RetrofitClient.getCommunityApiService(application)

    private val _likeCounts = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val likeCounts: StateFlow<Map<Int, Int>> get() = _likeCounts


    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val response = apiService.getPosts()
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val postsResult = response.body()?.result
                    postsResult?.let { postResults ->
                        _posts.value = postResults.map { postResult ->
                            Post(
                                post_id = postResult.post_id,
                                user_id = postResult.user_id,
                                nickname = postResult.nickname,
                                title = postResult.title,
                                main_text = postResult.main_text,
                                createdAt = postResult.createdAt,
                                like_count = postResult.like_count,
                                comment_count = postResult.comment_count,
                                isLiked = postResult.isLiked // 추가된 필드 매핑
                            )
                        }
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
        try {
            val response = apiService.getPostById(postId)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                response.body()?.result?.let { result ->
                    _selectedPost.value = Post(
                        post_id = result.post_id,
                        user_id = result.user_id,
                        nickname = result.nickname,
                        title = result.title,
                        main_text = result.main_text,
                        createdAt = result.createdAt,
                        like_count = result.like_count,
                        comment_count = result.comment_count,
                        isLiked = result.isLiked // 추가된 필드 매핑
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
        if (title.isBlank() || body.isBlank()) {
            Log.e("CommunityViewModel", "제목이나 본문이 비어있습니다.")
            return null
        }

        return try {
            val postRequest = PostRequest(title = title, main_text = body, nickname = nickname)
            val response = apiService.createPost(postRequest)

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val result = response.body()?.result
                result?.let {
                    _posts.update { currentPosts ->
                        currentPosts + Post(
                            post_id = it.post_id,
                            user_id = it.user_id,
                            nickname = it.nickname,
                            title = it.title,
                            main_text = it.main_text,
                            createdAt = it.createdAt,
                            like_count = 0,
                            comment_count = 0,
                            isLiked = false // 새 게시글은 기본적으로 좋아요가 없는 상태로 초기화
                        )
                    }
                    it.post_id
                }
            } else {
                Log.e("CommunityViewModel", "게시글 작성 오류: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "게시글 작성 중 예외 발생: ${e.localizedMessage}", e)
            null
        }
    }


    suspend fun addComment(postId: Int, request: CommentRequest): Boolean {
        return try {
            val response = apiService.addComment(postId, request)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val result = response.body()?.result
                Log.d("CommunityViewModel", "API Response Nickname: ${result?.nickname}") // nickname 로그 확인
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
                }
                true
            } else {
                Log.e("CommunityViewModel", "Error adding comment: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception adding comment: ${e.localizedMessage}")
            false
        }
    }


    suspend fun fetchCommentsForPost(postId: Int) {
        try {
            val response = apiService.getCommentsByPostId(postId)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val commentResults = response.body()?.result ?: emptyList()
                val newComments = commentResults.map { commentResult ->
                    Comment(
                        commentId = commentResult.comment_id,
                        postId = commentResult.post_id,
                        userId = commentResult.user_id,
                        nickname = commentResult.nickname,
                        body = commentResult.text,
                        createdAt = commentResult.createdAt
                    )
                }
                _comments.update { currentComments ->
                    currentComments + (postId to newComments)
                }
            } else {
                Log.e("Comments", "Error fetching comments: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Comments", "Exception fetching comments: ${e.localizedMessage}")
        }
    }


    fun getCommentsForPost(postId: Int): List<Comment> = _comments.value[postId] ?: emptyList()

    fun getMostLikedPost(): Post? = _posts.value.maxByOrNull { it.like_count }


    suspend fun addLike(postId: Int): Boolean {
        return try {
            val response = apiService.addLike(postId)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                updateLikeState(postId, true)
                updatePostLikeStatus(postId, isLiked = true, increment = true)
                true
            } else {
                Log.e("CommunityViewModel", "Error adding like: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception adding like: ${e.localizedMessage}")
            false
        }
    }

    suspend fun removeLike(postId: Int): Boolean {
        return try {
            val response = apiService.removeLike(postId)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                updateLikeState(postId, false)
                updatePostLikeStatus(postId, isLiked = false, increment = false)
                true
            } else {
                Log.e("CommunityViewModel", "Error removing like: ${response.code()} - ${response.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e("CommunityViewModel", "Exception removing like: ${e.localizedMessage}")
            false
        }
    }



    // 좋아요 상태 업데이트 메서드
    private fun updateLikeState(postId: Int, isLiked: Boolean) {
        _likeStates.update { currentStates ->
            currentStates + (postId to isLiked)
        }
    }

    private fun updatePostLikeStatus(postId: Int, isLiked: Boolean, increment: Boolean) {
        _selectedPost.update { post ->
            post?.takeIf { it.post_id == postId }?.copy(
                isLiked = isLiked,
                like_count = if (increment) post.like_count + 1 else post.like_count - 1
            )
        }
        _posts.update { currentPosts ->
            currentPosts.map { post ->
                if (post.post_id == postId) {
                    post.copy(
                        isLiked = isLiked,
                        like_count = if (increment) post.like_count + 1 else post.like_count - 1
                    )
                } else post
            }
        }
    }



    fun isPostLiked(postId: Int): Boolean {
        return _likeStates.value[postId] ?: false
    }
}