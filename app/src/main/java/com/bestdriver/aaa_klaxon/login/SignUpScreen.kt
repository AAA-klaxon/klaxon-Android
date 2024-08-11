package com.bestdriver.aaa_klaxon.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SignUpScreen(modifier: Modifier = Modifier, onSignUpComplete: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var carNumber by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // 모든 입력 필드가 채워졌는지 체크
    val isFormValid = email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty() && carNumber.isNotEmpty() && phone.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onSignUpComplete() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "회원가입",
                fontSize = 40.sp,
                color = Color(0xFF321D87),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("아이디 (이메일)", fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호", fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = { Text("닉네임", fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = carNumber,
            onValueChange = { carNumber = it },
            label = { Text("차번호", fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("휴대폰", fontSize = 20.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF321D87) // 배경색 설정
            ),
            enabled = isFormValid // 모든 필드가 채워졌을 때만 활성화
        ) {
            Text("완료", fontSize = 20.sp, color = Color.White) // 텍스트 색상
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("회원가입 완료") },
                text = { Text("이제 클락션에서 실시간 오분류 정보를 확인할 수 있어요!") },
                confirmButton = {
                    TextButton(onClick = {
                        onSignUpComplete()
                        showDialog = false
                    }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    AAA_klaxonTheme {
        SignUpScreen(
            onSignUpComplete = {}
        )
    }
}
