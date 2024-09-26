import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.copy
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.network.community.CommunityWriteScreenViewModel
import com.bestdriver.aaa_klaxon.network.community.Post
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple


@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityWriteScreenViewModel,
    newPostId: Int? = null
) {
    val posts by viewModel.posts.collectAsState()
    val mostLikedPost by remember { derivedStateOf { viewModel.getMostLikedPost() } }

    LaunchedEffect(Unit) {
        viewModel.fetchPosts()
    }

    val filteredPosts = posts.filter {
        it.title != null || it.main_text != null
    }

    Log.d("CommunityScreen", "Filtered posts: $filteredPosts")
    Log.d("CommunityScreen", "All posts: $posts")

    LaunchedEffect(newPostId) {
        newPostId?.let { postId ->
            posts.find { it.post_id == postId }?.let { post ->
                navController.navigate("communityFeed/${Uri.encode(postId.toString())}/${Uri.encode(post.title)}/${Uri.encode(post.main_text)}/${Uri.encode(post.createdAt)}/${Uri.encode(post.like_count.toString())}/${Uri.encode(post.nickname)}") {
                    popUpTo("communityHome") { inclusive = true }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp).padding(top = 45.dp)) {
            item {
                Text(
                    text = "커뮤니티",
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(bottom = 30.dp)
                )
            }

            item {
                Text(
                    text = "인기 글",
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            mostLikedPost?.let { post ->
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                        navController.navigate("communityFeed/${Uri.encode(post.post_id.toString())}/${Uri.encode(post.title)}/${Uri.encode(post.main_text)}/${Uri.encode(post.createdAt)}/${Uri.encode(post.like_count.toString())}/${Uri.encode(post.nickname)}")
                    }) {
                        PopularCard(
                            title = post.title,
                            content = post.main_text,
                            date = post.createdAt,
                            favoriteCount = post.like_count,
                            commentCount = post.comment_count
                        )
                    }
                }
            } ?: run {
                item {
                    Text(
                        text = "인기 글이 없습니다.",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }

            if (filteredPosts.isEmpty()) {
                item {
                    Text(
                        text = "게시글이 없습니다.",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(filteredPosts) { post ->
                    Log.d("CommunityScreen", "Post: $post")
                    post?.let {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("communityFeed/${Uri.encode(it.post_id.toString())}/${Uri.encode(it.title)}/${Uri.encode(it.main_text)}/${Uri.encode(it.createdAt)}/${Uri.encode(it.like_count.toString())}/${Uri.encode(it.nickname)}")
                                }
                        ) {
                            CommunityPost(
                                title = it.title,
                                content = it.main_text,
                                date = it.createdAt,
                                favoriteCount = it.like_count,
                                commentCount = it.comment_count
                            )
                        }
                        ThinHorizontalLine()
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            DrawCircleWithCross {
                navController.navigate("communityWrite")
            }
        }
    }
}



@Composable
fun CommunityPost(
    title: String,
    content: String,
    date: String,
    favoriteCount: Int,
    commentCount: Int
) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 13.dp)
        )

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = content,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 13.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 13.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 5.dp),
                tint = MyPurple
            )

            Text(
                text = favoriteCount.toString(),
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                color = Color.Black,
                modifier = Modifier.padding(end = 5.dp)
            )

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Chat",
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 5.dp),
                tint = MyPurple
            )

            Text(
                text = commentCount.toString(),
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.Black,
                modifier = Modifier.padding(end = 10.dp)
            )

            SmallVerticalLine()

            Text(
                text = date,
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun PopularCard(
    title: String,
    content: String,
    date: String,
    favoriteCount: Int,
    commentCount: Int
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MyPurple.copy(alpha = 0.2f))
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight() // Box의 전체 높이를 사용
                    .padding(vertical = 13.dp),
                verticalArrangement = Arrangement.Center // 세로 가운데 정렬
            ) {
                Text(
                    text = title,
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Text(
                    text = content,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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
                        text = favoriteCount.toString(),
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Chat",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(bottom = 4.dp),
                        tint = MyPurple
                    )

                    Text(
                        text = commentCount.toString(),
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black
                    )
                }
            }
        }
    }
}



@Composable
fun ThinHorizontalLine() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),  // 전체 너비를 차지하게 설정
        thickness = 0.9.dp,    // 선의 두께 설정
        color = Color.Gray.copy(alpha = 0.4f) // 선의 색상 설정
    )
}

@Composable
fun SmallVerticalLine() {
    Box(
        modifier = Modifier
            .height(13.dp) // 선의 높이를 설정 (작게 설정)
            .width(1.dp) // 선의 두께를 설정 (얇게 설정)
            .background(Color.Black.copy(alpha = 0.3f)) // 선의 색상 및 투명도 설정
            .padding(top = 10.dp)
    )
}

@Composable
fun DrawCircleWithCross(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(90.dp) // 원의 크기와 같은 크기로 설정
            .clickable { onClick() } // 클릭 이벤트 처리
            .background(Color.Transparent) // 배경을 투명하게 설정
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.width / 2.4f
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            // 동그라미 그리기
            drawCircle(
                color = MyPurple,
                center = Offset(centerX, centerY),
                radius = radius
            )

            // 십자가 그리기
            drawLine(
                color = Color.White,
                start = Offset(centerX - radius / 2.3f, centerY),
                end = Offset(centerX + radius / 2.3f, centerY),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(centerX, centerY - radius / 2.3f),
                end = Offset(centerX, centerY + radius / 2.3f),
                strokeWidth = 12f,
                cap = StrokeCap.Round
            )
        }
    }
}

data class CommunityItem(
    val title: String,
    val content: String,
    val date: String,
    val favoriteCount: Int,
    val commentCount: Int
)

//@Composable
//fun PreviewCommunityScreen() {
//    // Create a mock or test instance of the ViewModel
//    val mockViewModel = CommunityWriteScreenViewModel().apply {
//        // Initialize with some test data
//        val posts = listOf(
//            Post(post_id = 1, user_id = 1, userName = "User1", title = "Test Post 1", main_text = "This is a test post", createdAt = "2024-09-03T10:00:00Z", like_count = 10, comment_count = 5),
//            Post(post_id = 2, user_id = 2, userName = "User2", title = "Test Post 2", main_text = "Another test post", createdAt = "2024-09-03T11:00:00Z", like_count = 5, comment_count = 2)
//        )
//        // Assuming you have a method to set posts in the ViewModel
//        // You might need to create a method to add mock posts
//        _posts.value = posts // Update the private _posts directly
//    }
//
//    CommunityScreen(
//        navController = rememberNavController(),
//        viewModel = mockViewModel
//    )
//}
