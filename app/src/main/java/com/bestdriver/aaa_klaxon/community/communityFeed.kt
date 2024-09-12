package com.bestdriver.aaa_klaxon.community

import Post
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.viewmodel.CommunityWriteScreenViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

data class Comment(
    val userName: String,
    val body: String,
    val date: String,
    val time: String
)

@Composable
fun CommunityFeedScreen(
    navController: NavController,
    viewModel: CommunityWriteScreenViewModel,
    postId: String, // 게시글 ID는 필수
    postTitle: String? = null,
    postBody: String? = null,
    timestamp: String? = null,
    likeCount: Int? = null,
    userName: String? = null
) {
    // StateFlow의 value를 사용하여 댓글 리스트를 가져옵니다.
    val comments by viewModel.comments.collectAsState() // StateFlow의 현재 값을 구독

    // 댓글 리스트를 가져옵니다.
    val postComments = comments[postId] ?: emptyList()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // UI를 구성합니다.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 10.dp)
            .clickable {
                // 빈 배경 클릭 시 키보드 숨기기
                focusManager.clearFocus()
                keyboardController?.hide()
            }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(30.dp)
                .clickable { navController.navigateUp() },
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (postTitle != null && postBody != null) {
            PostItem(
                post = Post(
                    id = postId,
                    title = postTitle,
                    body = postBody,
                    timestamp = timestamp ?: "",
                    likeCount = likeCount ?: 0,
                    userName = userName ?: "",
                    commentCount = postComments.size // 댓글 수를 전달
                ),
                viewModel = viewModel // ViewModel을 전달
            )
        } else {
            Text("Post details not available")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(postComments) { comment ->
                CommentItem(comment)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CommentSection(viewModel, postId)
    }
}





@Composable
fun PostItem(post: Post, viewModel: CommunityWriteScreenViewModel) {
    // 예를 들어 댓글 리스트를 가져오는 부분
    val comments = viewModel.getCommentsForPost(post.id)

    // 상태를 기억하고 초기화합니다.
    val isLiked = remember { mutableStateOf(false) }
    val likeCount = remember { mutableStateOf(post.likeCount) } // 좋아요 카운트 초기화

    // 로그 출력
    Log.d("PostItem", "Comments size: ${comments.size}")
    Log.d("PostItem", "First comment: ${comments.getOrNull(0)}")
    Log.d("PostItem", "Second comment: ${comments.getOrNull(1)}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        CircleCanvas(
            modifier = Modifier
                .size(70.dp) // Canvas의 크기를 지정
        )
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Text(
                text = post.userName,
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 5.dp)
 )

            Row {
                Text(
                    text = post.timestamp.split(" ")[0], // 날짜
                    fontSize = 17.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = Color.Black.copy(alpha = 0.5f)
                )
//                Text(
//                    text = post.timestamp.split(" ")[1], // 시간
//                    fontSize = 18.sp,
//                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
//                    color = Color.Black.copy(alpha = 0.5f),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 8.dp)
//                        .padding(bottom = 20.dp)
//                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = post.title,
            fontSize = 23.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = Color.Black
        )
        Icon(
            imageVector = if (isLiked.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(43.dp)
                .padding(start = 10.dp)
                .clickable {
                    isLiked.value = !isLiked.value
                    likeCount.value = if (isLiked.value) likeCount.value + 1 else likeCount.value - 1
                    viewModel.updateLikeCount(post.id, isLiked.value)
                },
            tint = MyPurple
        )
    }
    Text(
        text = post.body,
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        color = Color.Black,
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(bottom = 15.dp)
    )
    Row(
        modifier = Modifier
            .padding(end = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(25.dp)
                .padding(bottom = 4.dp),
            tint = MyPurple
        )

        Text(
            text = likeCount.value.toString(), // 하트 클릭 횟수를 표시
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            color = Color.Black,
            modifier = Modifier
                .padding(start = 4.dp)
                .padding(end = 4.dp),
        )
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Chat",
            modifier = Modifier
                .size(25.dp)
                .padding(bottom = 4.dp),
            tint = MyPurple
        )

        Text(
            text = post.commentCount.toString(), // 댓글 수를 표시
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 30.dp)
        )
    }
}





@Composable
fun CommentItem(comment: Comment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CircleCanvas(
                modifier = Modifier
                    .size(40.dp) // Canvas의 크기를 지정
            )
            Text(
                text = comment.userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(top = 5.dp)
            )
        }
        Text(
            text = comment.body,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Row (
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(
                text = comment.date,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.5f),
            )
            Text(
                text = comment.time,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
                    .padding(bottom = 20.dp)
            )
        }
        ThinHorizontalLine()
    }
}

@Composable
fun CommentSection(viewModel: CommunityWriteScreenViewModel, postId: String) {
    val commentText = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 69.dp),
            value = commentText.value,
            onValueChange = { newComment -> commentText.value = newComment },
            placeholder = {
                Text(
                    text = "댓글을 입력하세요",
                    fontFamily = FontFamily(Font(R.font.pretendard_medium))
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = MyPurple.copy(alpha = 0.2f)
            )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp, 60.dp)
                .padding(bottom = 4.dp)
                .background(MyPurple)
                .clickable {
                    // 날짜와 시간 포맷을 SimpleDateFormat을 이용해 설정합니다.
                    val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val currentDate = dateFormat.format(Calendar.getInstance().time)
                    val currentTime = timeFormat.format(Calendar.getInstance().time)

                    viewModel.addComment(
                        postId = postId,
                        comment = Comment(
                            userName = "임시 사용자", // 사용자 이름을 동적으로 설정할 수 있습니다.
                            body = commentText.value,
                            date = currentDate,
                            time = currentTime
                        )
                    )
                    commentText.value = "" // 댓글 등록 후 텍스트 필드를 비웁니다.
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "등록",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}







@Composable
fun CircleCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        drawCircle(
            color = Color.Gray,
            center = Offset(size.width / 2, size.height / 2),
            radius = radius
        )
    }
}

@Composable
fun ThinHorizontalLine() {
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 1.dp,
        color = Color.Black.copy(alpha = 0.4f)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCommunityFeedScreen() {
    val dummyViewModel = CommunityWriteScreenViewModel().apply {
        // 필요한 경우 ViewModel에 더미 데이터를 설정합니다.
        // 예: posts.add(Post(...))
    }
}