package com.bestdriver.aaa_klaxon.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.network.auth.LoginViewModel
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.R // 로고 파일이 포함된 리소스 패키지 추가
import com.bestdriver.aaa_klaxon.home.MyScreen
import com.bestdriver.aaa_klaxon.network.TokenManager
import com.bestdriver.aaa_klaxon.user.mypage.OnboardingScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("main")
                            },
                            navController = navController
                        )
                    }
                    composable("signup") {
                        SignUpScreen(navController = navController)
                    }
                }
            }
        }
    }
}


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // LocalContext를 통해 AuthApiService 인스턴스 생성
    val context = LocalContext.current
    val authApiService = RetrofitClient.getAuthApiService(context)
    val viewModel = remember { LoginViewModel(authApiService, context) }

    // UI에서 사용하는 상태 값들
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val loginError by viewModel.loginError.observeAsState("")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp)
        ) {
            // 제목 및 로고
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // 로고 이미지
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(140.dp)
                        .height(100.dp)
                )
                Text(
                    text = "K l a x o n",
                    fontSize = 20.sp,
                    color = MyPurple,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 이메일 입력 필드
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
                singleLine = true,
                label = { Text("아이디", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // 비밀번호 입력 필드
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it) },
                singleLine = true,
                label = { Text("비밀번호", fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            // 로그인 오류 메시지 표시
            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 로그인 버튼
            Button(
                onClick = {
                    viewModel.onLoginClick {
                        accessToken ->
                        // 액세스 토큰이 저장되었는지 확인
                        val savedToken = TokenManager(context).getAccessToken()
                        if (savedToken != null) {
                            Log.d("LoginScreen", "Saved Token: $savedToken")
                        } else {
                            Log.d("LoginScreen", "No Token Found")
                        }
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 23.dp)
                    .height(53.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MyPurple
                )
            ) {
                Text("로그인",
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            // 회원가입 및 아이디/비밀번호 찾기 링크
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "회원가입",
                    modifier = Modifier
                        .clickable { navController.navigate("signup") }
                        .padding(end = 16.dp),
                    color = MyPurple,
                    fontSize = 16.sp
                )
                Text(
                    text = "아이디/비밀번호 찾기",
                    modifier = Modifier.clickable { /* 아이디/비밀번호 찾기 로직 추가 */ },
                    color = MyPurple,
                    fontSize = 16.sp
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    // LoginScreen 미리보기를 위한 Dummy onLoginSuccess 함수
    LoginScreen(
        onLoginSuccess = { /* 로그인 성공 후 동작 */ },
        navController = rememberNavController() // 네비게이션을 위한 NavController 생성
    )
}


