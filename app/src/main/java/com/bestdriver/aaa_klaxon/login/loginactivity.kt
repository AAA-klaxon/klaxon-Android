package com.bestdriver.aaa_klaxon.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.bestdriver.aaa_klaxon.mypage.MyPageActivity

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                LoginScreen(
                    onLoginSuccess = {
                        // 로그인 성공 시 MyPageActivity로 이동
                        val intent = Intent(this, MyPageActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var navigateToSignUp by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf("") }  // 로그인 오류 메시지 상태

    if (navigateToSignUp) {
        // 회원가입 화면으로 전환
        SignUpScreen(
            modifier = modifier,
            onSignUpComplete = {
                navigateToSignUp = false
                // Handle post sign-up actions, if any
            }
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "클락션",
                    fontSize = 50.sp,
                    color = Color(0xFF321D87),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp) // 줄 간격 조정
                )
                Text(
                    text = "Klaxon",
                    fontSize = 30.sp,
                    color = Color(0xFF321D87),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("아이디", fontSize = 20.sp) },
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
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation() // 비밀번호 입력 필드 마스킹
            )

            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        // 아이디와 비밀번호가 모두 입력된 경우
                        onLoginSuccess()
                    } else {
                        // 아이디 또는 비밀번호가 비어있는 경우
                        loginError = "아이디와 비밀번호를 입력해 주세요."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF321D87)
                )
            ) {
                Text("로그인", fontSize = 20.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ClickableText(
                    text = AnnotatedString("회원가입"),
                    onClick = {
                        navigateToSignUp = true
                    },
                    style = TextStyle(
                        color = Color(0xFF321D87),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(end = 16.dp)
                )
                ClickableText(
                    text = AnnotatedString("아이디/비밀번호 찾기"),
                    onClick = {
                        // 아이디/비밀번호 찾기 로직 추가
                    },
                    style = TextStyle(
                        color = Color(0xFF321D87),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
