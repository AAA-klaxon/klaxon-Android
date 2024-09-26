package com.bestdriver.aaa_klaxon.mypage

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.network.mypage.MypageApiService
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class NicknameUpdateRequest(val nickname: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(navController: NavController, modifier: Modifier = Modifier) {
    var nickname by remember { mutableStateOf("") }
    var serverNickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("email") }
    var carNumber by remember { mutableStateOf("CarNumber") }
    var updateMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val mypageApiService = RetrofitClient.getMypageApiService(context)

    LaunchedEffect(Unit) {
        try {
            val response = mypageApiService.getUserInfo()
            if (response.isSuccessful) {
                response.body()?.let { userInfoResponse ->
                    serverNickname = userInfoResponse.result.nickname
                    carNumber = userInfoResponse.result.car_number
                    email = userInfoResponse.result.email
                }
            } else {
                Log.e("MyPage", "Error: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("MyPage", "Exception: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                pageTitle = "프로필 편집",
                onNavigationIconClick = { navController.navigate("mypage") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // 항목 간의 간격
        ) {
            item {
                CircleWithCross(modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    context.startActivity(intent)
                })
            }
            item {
                Text(
                    text = serverNickname,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            item {
                Text(
                    text = "아이디(이메일)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 5.dp)
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = email,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
            item {
                Text(
                    text = "차번호",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 5.dp)
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = carNumber,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
            item {
                Text(
                    text = "닉네임*",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 5.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )
            }
            item {
                // 409 에러 메시지 표시
                if (updateMessage == "이미 존재하는 사용자입니다.") {
                    Text(
                        text = updateMessage,
                        color = MyPurple,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth() // 왼쪽 정렬
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        if (nickname.isNotEmpty()) {
                            val nicknameUpdateRequest = NicknameUpdateRequest(nickname = nickname)
                            updateNickname(mypageApiService, nicknameUpdateRequest, navController, { message ->
                                updateMessage = message
                            }, {
                                nickname = ""
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(bottom = 50.dp),
                    enabled = nickname.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (nickname.isNotEmpty()) Color(0xFF321D87) else Color.Gray
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("수정 완료", fontSize = 20.sp, color = Color.White)
                }
            }
            item {
                // 업데이트 메시지 표시
                if (updateMessage.isNotEmpty() && updateMessage != "이미 존재하는 사용자입니다.") {
                    Text(
                        text = updateMessage,
                        color = MyPurple,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth() // 왼쪽 정렬
                    )
                }
            }
        }
    }
}


private fun updateNickname(
    mypageApiService: MypageApiService,
    request: NicknameUpdateRequest,
    navController: NavController,
    onUpdateMessage: (String) -> Unit,
    onNicknameCleared: () -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.d("MyPage", "Request: $request")
            val response = mypageApiService.updateNickname(request)
            Log.d("MyPage", "Response Code: ${response.code()}, Response Message: ${response.message()}")

            if (response.isSuccessful) {
                // 성공 시 화면 전환만 수행
                withContext(Dispatchers.Main) {
                    navController.navigate("mypage")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("MyPage", "닉네임 변경 오류: ${response.code()}, ${response.message()}, Error Body: $errorBody")

                // 409 에러 처리
                if (response.code() == 409) {
                    withContext(Dispatchers.Main) {
                        // 닉네임 필드 비우기
                        onNicknameCleared()
                        onUpdateMessage("이미 존재하는 사용자입니다.")
                    }

                    kotlinx.coroutines.delay(600)

                    // 화면을 초기화
                    withContext(Dispatchers.Main) {
                        onUpdateMessage("") // 메시지 지우기
                    }
                    return@launch // 더 이상 처리하지 않음
                } else {
                    val errorMessage = "닉네임 변경 오류: ${response.message()}"
                    withContext(Dispatchers.Main) {
                        onUpdateMessage(errorMessage)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("MyPage", "닉네임 변경 중 예외 발생: ${e.message}")
            withContext(Dispatchers.Main) {
                onUpdateMessage("닉네임 변경 중 오류가 발생했습니다.")
            }
        }
    }
}




@Composable
fun CircleWithCross(
    modifier: Modifier = Modifier,
    circleColor: Color = Color.Gray.copy(alpha = 0.4f),
    crossColor: Color = Color.White,
    crossStrokeWidth: Dp = 7.dp
) {
    Canvas(
        modifier = modifier.size(100.dp)
    ) {
        val canvasSize = size
        val radius = canvasSize.minDimension / 2
        val center = Offset(canvasSize.width / 2, canvasSize.height / 2)

        drawCircle(
            color = circleColor,
            radius = radius,
            center = center
        )

        val strokeWidthPx = crossStrokeWidth.toPx()
        val crossLength = radius / 2.3f

        drawLine(
            color = crossColor,
            start = Offset(center.x - crossLength, center.y),
            end = Offset(center.x + crossLength, center.y),
            strokeWidth = strokeWidthPx
        )
        drawLine(
            color = crossColor,
            start = Offset(center.x, center.y - crossLength),
            end = Offset(center.x, center.y + crossLength),
            strokeWidth = strokeWidthPx
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileEditScreen() {
    AAA_klaxonTheme {
        ProfileEditScreen(navController = rememberNavController())
    }
}
