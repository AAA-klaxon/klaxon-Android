import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.network.community.CommunityWriteScreenViewModel
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.UUID



@Composable
fun CommunityWriteScreen(
    navController: NavController,
    viewModel: CommunityWriteScreenViewModel,
    userName: String,
    onSubmitClick: (String, String, String) -> Unit // 추가된 매개변수
) {
    val titleState = remember { mutableStateOf("") }
    val textState = remember { mutableStateOf("") }
    val maxLength = 500
    val titleMaxLength = 100

    val isSubmitEnabled = remember(titleState.value, textState.value) {
        titleState.value.isNotBlank() && textState.value.isNotBlank() && textState.value.length <= maxLength
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable {
                keyboardController?.hide()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "취소",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            navController.navigate("communityHome") {
                                popUpTo("communityWrite") { inclusive = true }
                            }
                        }
                )

                Text(
                    text = "글쓰기",
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = "등록",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if (isSubmitEnabled) {
                                val title = titleState.value
                                val body = textState.value
                                val nickname = userName

                                // onSubmitClick 안에서 ViewModel의 addPost를 호출
                                coroutineScope.launch {
                                    val result = viewModel.addPost(title, body, nickname)
                                    if (result != null) {
                                        // 성공적으로 게시물이 등록되었으면 CommunityHome으로 이동
                                        navController.navigate("communityHome?newPostId=$result") {
                                            popUpTo("communityWrite") { inclusive = true }
                                        }
                                    } else {
                                        // 실패 시 에러 메시지 출력
                                        Log.e("CommunityWriteScreen", "Failed to add post")
                                    }
                                }
                            }
                        }
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = titleState.value,
                onValueChange = { newTitle ->
                    if (newTitle.length <= titleMaxLength) {
                        titleState.value = newTitle
                        Log.d("CommunityWriteScreen", "Title updated: $newTitle")
                    }
                },
                placeholder = {
                    Text(
                        text = "제목",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color.Transparent)
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular))
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, // 아래 선 색상
                    unfocusedIndicatorColor = Color.Transparent // 아래 선 색상
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                TextField(
                    value = textState.value,
                    onValueChange = { newText ->
                        if (newText.length <= maxLength) {
                            textState.value = newText
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular))
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = Int.MAX_VALUE,
                    singleLine = false,
                    placeholder = {
                        Text(
                            text = "내용을 입력하세요...",
                            fontSize = 18.sp,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${textState.value.length}/$maxLength",
                        fontSize = 18.sp,
                        color = Color.Black.copy(alpha = 0.5f),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular))
                    )
                }
            }
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewCommunityWriteScreen() {
//    val navController = rememberNavController()
//    val posts = remember { mutableStateListOf<Post>() }
//
//    val viewModel = CommunityWriteScreenViewModel()
//
//    val handleSubmitClick: (String, String, String) -> Unit = { title, body, timestamp ->
//        viewModel.addPost(title, body, "testUser", timestamp)
//        navController.navigate("community_screen") {
//            popUpTo("community_write_screen") { inclusive = true }
//        }
//    }
//
//    CommunityWriteScreen(
//        navController = navController,
//        viewModel = viewModel,
//        userName = "testUser", // 예제 사용자 이름
//        onSubmitClick = handleSubmitClick
//    )
//}