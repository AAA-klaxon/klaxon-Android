package com.bestdriver.aaa_klaxon.mypage

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme

class DeleteAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                DeleteAccountScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "탈퇴하기",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // 뒤로가기 버튼 클릭 시 마이페이지로 이동
                        val intent = Intent(context, MyPageActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                thickness = 1.dp,
                color = Color.Gray
            )

            Text(
                text = "클락션을 탈퇴하면,",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "내 프로필, 주행내역, 게시글, 댓글, 적립캐시\n" +
                        "그 외 사용자가 설정한 모든 정보가 사라지고 복구가 불가능합니다.",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text("안내 사항을 확인하였으며, 이에 동의합니다.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){Button(
                onClick = {
                    if (isChecked) {
                        // TODO: 탈퇴 처리 로직 추가
                    }
                },
                enabled = isChecked,

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF321D87)
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // 내부 패딩 조절
                modifier = Modifier.wrapContentWidth()
            ) {
                    Text(
                        text = "탈퇴하기",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteAccountScreen() {
    AAA_klaxonTheme {
        DeleteAccountScreen()
    }
}
