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
    // Get AuthApiService instance with context
    val context = LocalContext.current
    val authApiService = RetrofitClient.getAuthApiService(context)
    val viewModel = remember {
        SignUpViewModel(authApiService)
    }

    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val nickname by viewModel.nickname.observeAsState("")
    val car_number by viewModel.car_number.observeAsState("")
    val showDialog by viewModel.showDialog.observeAsState(false)
    val dialogMessage by viewModel.dialogMessage.observeAsState("")

    val isFormValid =
        email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty() && car_number.isNotEmpty()

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
                .padding(paddingValues) // LazyColumn에 paddingValues 적용
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
                    onValueChange = { viewModel.updateEmail(it) },
                    singleLine = true,
                    placeholder = { Text("cyber@duksung.ac.kr", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
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
                    onValueChange = { viewModel.updatePassword(it) },
                    singleLine = true,
                    placeholder = { Text("비밀번호를 입력해주세요", fontSize = 15.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
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

            // AlertDialog 표시를 위한 item
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

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    AAA_klaxonTheme {
        SignUpScreen(
            navController = rememberNavController()
        )
    }
}
