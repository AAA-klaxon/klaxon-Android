package com.bestdriver.aaa_klaxon.viewmodel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bestdriver.aaa_klaxon.user.mypage.Notice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class UserViewModel : ViewModel() {
    // 사용자 이름과 자동차 번호를 저장하는 MutableLiveData
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _carNumber = MutableLiveData<String>()
    val carNumber: LiveData<String> get() = _carNumber

    // 사용자 정보를 업데이트하는 메서드
    fun updateUser(name: String, car: String) {
        _userName.value = name
        _carNumber.value = car
    }
}

//class CommunityWriteScreenViewModel : ViewModel() {
//    private val _posts = MutableStateFlow(
//        listOf(
//            Post(
//                id = UUID.randomUUID().toString(), // 예제 ID
//                title = "예제 제목",
//                body = "예제 본문 내용입니다.",
//                userName = "미리보기 사용자",
//                timestamp = "2024-09-09",
//                likeCount = 20,
//                commentCount = 0
//            )
//        )
//    )
//    val posts: StateFlow<List<Post>> get() = _posts
//
//    private val _comments = MutableStateFlow<Map<String, List<Comment>>>(emptyMap())
//    val comments: StateFlow<Map<String, List<Comment>>> get() = _comments
//
//    // 게시글 추가 메소드
//    fun addPost(title: String, body: String, userName: String, timestamp: String): String {
//        val postId = UUID.randomUUID().toString() // 각 게시글에 고유 ID 부여
//        val newPost = Post(
//            id = postId,
//            title = title,
//            body = body,
//            userName = userName,
//            timestamp = timestamp,
//            likeCount = 0,
//            commentCount = 0
//        )
//
//        _posts.update { currentPosts -> currentPosts + newPost }
//        _comments.update { currentComments -> currentComments + (postId to emptyList()) } // 새로운 게시글에 대해 빈 댓글 리스트 초기화
//
//        return postId // 새로 추가된 게시글의 ID를 반환
//    }
//
//    // 좋아요 수 업데이트 메소드
//    fun updateLikeCount(postId: String, increment: Boolean) {
//        _posts.update { currentPosts ->
//            currentPosts.map { post ->
//                if (post.id == postId) {
//                    post.copy(likeCount = post.likeCount + if (increment) 1 else -1)
//                } else {
//                    post
//                }
//            }
//        }
//    }
//
//    // 댓글 추가 메소드
//    fun addComment(postId: String, comment: Comment) {
//        _comments.update { currentComments ->
//            val postComments = currentComments[postId]?.toMutableList() ?: mutableListOf()
//            postComments.add(comment)
//            currentComments + (postId to postComments)
//        }
//
//        // 댓글 수 업데이트
//        _posts.update { currentPosts ->
//            currentPosts.map { post ->
//                if (post.id == postId) {
//                    post.copy(commentCount = post.commentCount + 1)
//                } else {
//                    post
//                }
//            }
//        }
//    }
//
//    // 이 메서드는 주어진 ID의 댓글 리스트를 반환합니다.
//    fun getCommentsForPost(postId: String): List<Comment> {
//        // 예시: 실제 구현은 데이터 소스(예: 데이터베이스, 네트워크 등)에서 댓글을 가져오는 로직을 포함합니다.
//        // 아래는 더미 데이터입니다.
//        return listOf(
//            Comment("User1", "This is a comment", "2024-09-10", "12:00"),
//            Comment("User2", "This is a comment", "2024-09-10", "12:00")
//        )
//    }
//
//    // 하트 수가 가장 많은 게시글을 반환하는 메소드
//    fun getMostLikedPost(): Post? {
//        return _posts.value.maxByOrNull { it.likeCount }
//    }
//}
//



class NoticeViewModel : ViewModel() {
    // Notice 데이터 리스트
    private val _notices = MutableLiveData<List<Notice>>()
    val notices: LiveData<List<Notice>> get() = _notices

    init {
        loadNotices()
    }

    private fun loadNotices() {
        // 실제로는 데이터베이스 또는 API에서 데이터를 가져옵니다.
        // 여기서는 예시로 임시 데이터를 사용합니다.
        _notices.value = listOf(
            Notice(
                id = "1",
                title = "[공지] 서울특별시 강북구 4.19로 표지판 업데이트",
                date = "2024.08.07",
                body = "안녕하세요, 기아자동차입니다.\n서울특별시 강북구 4.19로 우회전 표지판 확인 결과\n표지판이 손상되어 오류가 발생하는 것으로 확인됐습니다.\n빠른 시일 내에 새로운 표지판으로 교체될 예정이오니\n양해 부탁드립니다.\n\n문의 사항이 있으신 경우, 아래의 연락처로 문의 바랍니다.\n02-000-0000\n\n감사합니다."
            ),
            Notice(
                id = "2",
                title = "[공지] AI 보안패치 업데이트",
                date = "2024.08.05",
                body = "기존 AI 보안 패치를 업데이트하였습니다.\n자세한 사항은 문서 참조 부탁드립니다."
            ),
            // 추가 공지사항...
        )
    }
}