package com.bestdriver.aaa_klaxon.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.network.auth.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authApiService = RetrofitClient.getAuthApiService(context)
    val viewModel = remember { SignUpViewModel(authApiService) }

    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val nickname by viewModel.nickname.observeAsState("")
    val car_number by viewModel.car_number.observeAsState("")
    val showDialog by viewModel.showDialog.observeAsState(false)
    val dialogMessage by viewModel.dialogMessage.observeAsState("")

    // 에러 메시지 상태 변수
    var emailErrorMessage by remember { mutableStateOf<String?>(null) }
    var passwordErrorMessage by remember { mutableStateOf<String?>(null) }

    val isFormValid = email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty() && car_number.isNotEmpty()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                pageTitle = "회원가입",
                onNavigationIconClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                // Email Field
                Text(
                    text = "아이디 (이메일)*",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        viewModel.updateEmail(it)
                        emailErrorMessage = if (!isEmailValid(it)) {
                            "올바른 이메일 형식을 입력해 주세요."
                        } else {
                            null
                        }
                    },
                    singleLine = true,
                    placeholder = { Text("cyber@duksung.ac.kr", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                // 이메일 형식 에러 메시지 표시
                emailErrorMessage?.let { errorMessage ->
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                // Password Field
                Text(
                    text = "비밀번호*",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        viewModel.updatePassword(it)
//                        passwordErrorMessage = if (!isPasswordValid(it)) {
//                            "비밀번호가 유효하지 않습니다. 규칙을 확인해주세요."
//                        } else {
//                            null
//                        }
                    },
                    singleLine = true,
                    placeholder = { Text("비밀번호를 입력해주세요", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
//                passwordErrorMessage?.let { errorMessage ->
//                    Text(
//                        text = errorMessage,
//                        color = Color.Red,
//                        fontSize = 12.sp,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//                Text(
//                    text = "(영대문자, 영소문자, 숫자 및 특수문자 중 3종류 이상으로 구성, 최소 9자 이상)",
//                    color = Color.Gray,
//                    fontSize = 12.sp,
//                    modifier = Modifier.padding(top = 4.dp)
//                )
            }

            item {
                // Nickname Field
                Text(
                    text = "닉네임*",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { viewModel.updateNickname(it) },
                    singleLine = true,
                    placeholder = { Text("닉네임을 입력해주세요", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            item {
                // Car Number Field
                Text(
                    text = "차번호*",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                OutlinedTextField(
                    value = car_number,
                    onValueChange = { viewModel.updateCarNumber(it) },
                    singleLine = true,
                    placeholder = { Text("차번호를 입력해주세요", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isFormValid) {
                            viewModel.onSignUpClick(navController)
                        } else {
                            viewModel.updateDialogMessage("모든 필드를 올바르게 입력해 주세요.")
                            viewModel.showErrorDialog()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyPurple
                    )
                ) {
                    Text(
                        "회원가입",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }

            // AlertDialog 표시
            item {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { viewModel.hideErrorDialog() },
                        title = { Text("폼 검증 오류") },
                        text = { Text(dialogMessage) },
                        confirmButton = {
                            Button(onClick = { viewModel.hideErrorDialog() }) {
                                Text("확인")
                            }
                        }
                    )
                }
            }
        }
    }
}

// Email validation function
fun isEmailValid(email: String): Boolean {
    val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    return emailPattern.matches(email)
}

//// Password validation function
//fun isPasswordValid(password: String): Boolean {
//    val minLength = 9
//    val hasUpperCase = password.any { it.isUpperCase() }
//    val hasLowerCase = password.any { it.isLowerCase() }
//    val hasDigit = password.any { it.isDigit() }
//    val hasSpecialChar = password.any { "!@#\$%^&*()_-+=[]{}|\\;:'\"<>,.?/~".contains(it) }
//
//    val validComposition = listOf(hasUpperCase, hasLowerCase, hasDigit, hasSpecialChar).count { it } >= 3
//    if (password.length < minLength || !validComposition) {
//        return false
//    }
//
//    val easyPatterns = listOf("12345678", "abcdef", "qwerty", "password", "love", "happy")
//    if (easyPatterns.any { password.contains(it, ignoreCase = true) }) {
//        return false
//    }
//
//    return true
//}



@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    AAA_klaxonTheme {
        SignUpScreen(
            navController = rememberNavController()
        )
    }
}
