package com.bestdriver.aaa_klaxon.mypage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import androidx.compose.ui.platform.LocalContext

class MyPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                MyPageScreen()
            }
        }
    }
}

@Composable
fun MyPageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val userName by remember { mutableStateOf("User Name") }
    val carNumber by remember { mutableStateOf("Car Number") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단 제목
        Text(
            text = "마이페이지",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF321D87),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        // 사용자 정보와 프로필 박스
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // 제목 아래 여백 추가
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 사용자 정보
            Column {
                Text(
                    text = "이름: $userName",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "차번호: $carNumber",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Profile 박스
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("Profile", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // 프로필 편집 화면으로 이동
                val intent = Intent(context, ProfileEditActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF321D87)
            )
        ) {
            Text("프로필 편집", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 회색 선
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // 내정보 및 계정 메뉴
        Column {
            Text(
                text = "내 정보",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            MenuItem(
                text = "오분류 신고내역",
                onClick = {
                    // 신고 내역 화면으로 이동
                    val intent = Intent(context, ReportHistoryActivity::class.java)
                    context.startActivity(intent)
                }
            )
            MenuItem(
                text = "캐시내역",
                onClick = { /* TODO: 캐시내역 로직 */ }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            Text(
                text = "계정",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            MenuItem(
                text = "로그아웃",
                onClick = {
                    showLogoutDialog = true
                }
            )
            MenuItem(
                text = "탈퇴하기",
                onClick = {
                    val intent = Intent(context, DeleteAccountActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }

    // 로그아웃 확인 대화상자
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("로그아웃 확인") },
            text = { Text("로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    // 로그아웃 처리 로직 추가
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
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color(0xFF321D87),
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageScreen() {
    AAA_klaxonTheme {
        MyPageScreen()
    }
}
