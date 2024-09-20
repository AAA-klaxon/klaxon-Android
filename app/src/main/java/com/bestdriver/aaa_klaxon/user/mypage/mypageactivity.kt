package com.bestdriver.aaa_klaxon.mypage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.community.ThinHorizontalLine
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.network.TokenManager
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import kotlinx.coroutines.launch

class MyPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                MyPageScreen(navController = rememberNavController())
            }
        }
    }
}

@Composable
fun MyPageScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("User Name") }
    var carNumber by remember { mutableStateOf("Car Number") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // 사용자 정보 불러오기 로직 추가 (코루틴 사용)
    LaunchedEffect(Unit) {
        val mypageApiService = RetrofitClient.getMypageApiService(context)

        try {
            val response = mypageApiService.getUserInfo()

            if (response.isSuccessful) {
                response.body()?.let { userInfoResponse ->
                    userName = userInfoResponse.result.nickname
                    carNumber = userInfoResponse.result.car_number
                    Log.d("MyPage", "User Info: ${userInfoResponse.result.nickname}, ${userInfoResponse.result.car_number}")
                }
            } else {
                Log.e("MyPage", "Error: ${response.code()}, ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("MyPage", "Exception: ${e.message}")
        }
    }



    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "마이페이지",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 20.dp)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Profile", color = Color.White)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = userName,
                        fontSize = 23.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = carNumber,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("profileEdit")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyPurple.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "프로필 편집",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            ThinHorizontalLine()

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column {
                Text(
                    text = "내 정보",
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(15.dp))

                MenuItem(
                    text = "오분류 신고내역",
                    onClick = { navController.navigate("reportHistory") }
                )

                MenuItem(
                    text = "캐시내역",
                    onClick = { navController.navigate("cash") }
                )

                Spacer(modifier = Modifier.height(15.dp))
                ThinHorizontalLine()
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "계정",
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))

                MenuItem(
                    text = "로그아웃",
                    onClick = { showLogoutDialog = true }
                )

                MenuItem(
                    text = "탈퇴하기",
                    onClick = { navController.navigate("deleteAccount") }
                )

                Spacer(modifier = Modifier.height(15.dp))
                ThinHorizontalLine()
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "공지사항",
                    fontSize = 23.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                MenuItem(
                    text = "공지사항",
                    onClick = { navController.navigate("notice") }
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("로그아웃 확인") },
            text = { Text("로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    navController.navigate("login")
                    showLogoutDialog = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.pretendard_medium))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageScreen() {
    AAA_klaxonTheme {
        MyPageScreen(navController = rememberNavController())
    }
}
