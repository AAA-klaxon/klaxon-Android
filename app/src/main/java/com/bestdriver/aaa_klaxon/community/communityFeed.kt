package com.bestdriver.aaa_klaxon.community

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.network.TokenManager
import com.bestdriver.aaa_klaxon.network.community.Comment
import com.bestdriver.aaa_klaxon.network.community.CommentRequest
import com.bestdriver.aaa_klaxon.network.community.CommunityWriteScreenViewModel
import com.bestdriver.aaa_klaxon.network.community.Post
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun CommunityFeedScreen(
    navController: NavController,
    viewModel: CommunityWriteScreenViewModel,
    postId: Int // 게시글 ID
) {
    var isNavigatingBack by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(postId) {
        viewModel.fetchPostById(postId)
        viewModel.fetchCommentsForPost(postId)
    }

    LaunchedEffect(isNavigatingBack) {
        if (isNavigatingBack) {
            viewModel.fetchPostById(postId)
            navController.navigate("communityHome") {
                popUpTo("communityHome") { inclusive = true }
            }
            isNavigatingBack = false
        }
    }

    val post by viewModel.selectedPost.collectAsState()
    val comments by viewModel.comments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide() // 외부 터치 시 키보드 숨기기
                })
            }
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(35.dp)
                        .padding(top = 10.dp)
                        .clickable { isNavigatingBack = true },
                    tint = Color.Black
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            post?.let { validPost ->
                item {
                    PostItem(post = validPost, viewModel = viewModel)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                comments[validPost.post_id]?.forEach { comment ->
                    item {
                        CommentItem(comment)
                    }
                }
            } ?: run {
                item {
                    Text("게시글을 불러오는 중입니다...")
                }
            }
        }

        CommentSection(viewModel = viewModel, postId = post?.post_id ?: -1)
    }
}

@Composable
fun PostItem(post: Post, viewModel: CommunityWriteScreenViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        CircleCanvas(modifier = Modifier.size(60.dp))
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                text = post.nickname,
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text = post.createdAt.split(" ")[0],
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }

    Spacer(modifier = Modifier.height(15.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = post.title,
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Icon(
            imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    coroutineScope.launch {
                        if (post.isLiked) {
                            viewModel.removeLike(post.post_id)
                        } else {
                            viewModel.addLike(post.post_id)
                        }
                    }
                },
            tint = MyPurple
        )
    }

    Text(
        text = post.main_text,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        color = Color.Black,
        modifier = Modifier
            .padding(vertical = 10.dp)
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
            text = post.like_count.toString(),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 4.dp)
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
            text = post.comment_count.toString(),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 10.dp)
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
            modifier = Modifier.fillMaxWidth()
        ) {
            CircleCanvas(
                modifier = Modifier.size(35.dp) // Canvas의 크기를 지정
            )
            Text(
                text = comment.nickname,
                fontSize = 19.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
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
            modifier = Modifier.padding(top = 10.dp)
        )
        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        ) {
            Text(
                text = comment.createdAt, // createdAt으로 변경
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.width(5.dp)) // 여백 추가
        }
        ThinHorizontalLine()
    }
}


@Composable
fun CommentSection(viewModel: CommunityWriteScreenViewModel, postId: Int) {
    val commentText = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val comments = viewModel.comments.collectAsState() // ViewModel의 댓글 상태 관찰

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
                Text(text = "댓글을 입력하세요", fontFamily = FontFamily(Font(R.font.pretendard_medium)))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = MyPurple.copy(alpha = 0.2f)
            )
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp, 62.dp)
                .padding(bottom = 4.dp)
                .background(MyPurple)
                .clickable {
                    coroutineScope.launch {
                        val request = CommentRequest(text = commentText.value)
                        // 댓글 추가 요청
                        val success = viewModel.addComment(postId, request)
                        if (success) {
                            // 댓글 목록 갱신
                            viewModel.fetchCommentsForPost(postId)
                            commentText.value = "" // 텍스트 필드 비우기
                        }
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "등록",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
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
        color = Color.Black.copy(alpha = 0.3f)
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCommunityFeedScreen() {
//    val dummyViewModel = CommunityWriteScreenViewModel().apply {
//        // 필요한 경우 ViewModel에 더미 데이터를 설정합니다.
//        // 예: posts.add(Post(...))
//    }
//}