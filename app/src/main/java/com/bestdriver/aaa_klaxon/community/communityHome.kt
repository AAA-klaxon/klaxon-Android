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
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.viewmodel.CommunityWriteScreenViewModel


@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityWriteScreenViewModel,
    newPostId: String? = null // 새로 추가된 게시글 ID를 선택적으로 받을 수 있습니다
) {
    val posts by viewModel.posts.collectAsState() // StateFlow<List<Post>>를 List<Post>로 변환합니다
    val mostLikedPost by remember { derivedStateOf { viewModel.getMostLikedPost() } }

    // 새 게시글 ID가 변경될 때마다 네비게이션을 수행
    LaunchedEffect(newPostId) {
        newPostId?.let { postId ->
            val post = posts.find { it.id == postId }
            if (post != null) {
                // 게시글 상세 페이지로 이동
                navController.navigate("communityFeed/${post.id}/${post.title}/${post.body}/${post.timestamp}/${post.likeCount}/${post.userName}") {
                    // 이 코드로 인해 CommunityScreen이 제거되고 CommunityFeed가 보여질 것입니다.
                    popUpTo("communityHome") { inclusive = true }
                }
            }
        }
    }

    // 네비게이션 효과를 추가합니다
  //      LaunchedEffect(newPostId) {
    //           newPostId?.let { postId ->
    //          val post = posts.find { it.id == postId }
    //          post?.let {
    //              navController.navigate("communityFeed/${it.id}/${it.title}/${it.body}/${it.timestamp}/${it.likeCount}/${it.userName}") {
    //                  popUpTo("communityScreen") { inclusive = true }
    //              }
    //          }
    //      }
    //  }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 50.dp)
        ) {
            item {
                Text(
                    text = "커뮤니티",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding( bottom = 40.dp)
                )
            }

            item {
                Text(
                    text = "인기 글",
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            if (mostLikedPost != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                navController.navigate("communityFeed/${mostLikedPost!!.id}/${mostLikedPost!!.title}/${mostLikedPost!!.body}/${mostLikedPost!!.timestamp}/${mostLikedPost!!.likeCount}/${mostLikedPost!!.userName}")
                            }
                    ) {
                        PopularCard(
                            title = mostLikedPost!!.title,
                            content = mostLikedPost!!.body,
                            date = mostLikedPost!!.timestamp,
                            favoriteCount = mostLikedPost!!.likeCount,
                            commentCount = mostLikedPost!!.commentCount
                        )
                    }
                }
            } else {
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

            item {
                Spacer(modifier = Modifier.height(40.dp)) // PopularCard와 다음 항목 사이에 패딩 추가
            }

            items(posts) { post ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("communityFeed/${post.id}/${post.title}/${post.body}/${post.timestamp}/${post.likeCount}/${post.userName}")
                        }
                ) {
                    CommunityPost(
                        title = post.title,
                        content = post.body,
                        date = post.timestamp,
                        favoriteCount = post.likeCount,
                        commentCount = post.commentCount
                    )
                }
                ThinHorizontalLine()
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd) // 오른쪽 하단에 배치
                .padding(16.dp) // 여백 추가
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
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            fontSize = 23.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = content,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 5.dp),
                tint = MyPurple
            )

            Text(
                text = favoriteCount.toString(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                color = Color.Black,
                modifier = Modifier.padding(end = 5.dp)
            )

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Chat",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 5.dp),
                tint = MyPurple
            )

            Text(
                text = commentCount.toString(),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = Color.Black,
                modifier = Modifier.padding(end = 10.dp)
            )

            SmallVerticalLine()

            Text(
                text = date,
                fontSize = 16.sp,
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
                            .size(30.dp)
                            .padding(bottom = 4.dp),
                        tint = MyPurple
                    )

                    Text(
                        text = favoriteCount.toString(),
                        fontSize = 18.sp,
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
                            .size(30.dp)
                            .padding(bottom = 4.dp),
                        tint = MyPurple
                    )

                    Text(
                        text = commentCount.toString(),
                        fontSize = 18.sp,
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
            .height(15.dp) // 선의 높이를 설정 (작게 설정)
            .width(1.dp) // 선의 두께를 설정 (얇게 설정)
            .background(Color.Black.copy(alpha = 0.3f)) // 선의 색상 및 투명도 설정
            .padding(top = 3.dp)
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

@Composable
fun PreviewCommunityScreen() {
    // Create a mock or test instance of the ViewModel
    val mockViewModel = CommunityWriteScreenViewModel().apply {
        // Initialize with some test data
        val posts = listOf(
            Post("wow","Test Post 1", "This is a test post", "User1", "2024-09-03T10:00:00Z", 10, 5),
            Post("wow", "Test Post 2", "Another test post", "User2", "2024-09-03T11:00:00Z", 5, 2)
        )
    }

    CommunityScreen(
        navController = rememberNavController(),
        viewModel = mockViewModel
    )
}