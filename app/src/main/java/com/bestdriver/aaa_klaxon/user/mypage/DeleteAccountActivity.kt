package com.bestdriver.aaa_klaxon.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.network.RetrofitClient
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                pageTitle = "탈퇴하기",
                onNavigationIconClick = { navController.popBackStack() }
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
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "클락션을 탈퇴하면,",
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                fontSize = 23.sp,
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Text(
                text = "내 프로필, 주행내역, 게시글, 댓글, 적립캐시 그 외 사용자가 설정한 모든 정보가 사라지고 복구가 불가능합니다.\n\n",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "안내 사항을 확인하였으며, 이에 동의합니다.",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (isChecked) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val apiService = RetrofitClient.getMypageApiService(context)
                                try {
                                    val response = apiService.deleteUser()
                                    if (response.isSuccessful) {
                                        // 탈퇴 성공 시 로그인 화면으로 이동
                                        withContext(Dispatchers.Main) {
                                            navController.navigate("login") {
                                                popUpTo("myPage") { inclusive = true }
                                            }
                                        }
                                    } else {
                                        // 실패 처리 로직
                                        Log.e("DeleteAccount", "Failed to delete user: ${response.code()} - ${response.message()}")
                                    }
                                } catch (e: Exception) {
                                    // 에러 처리 로직
                                    Log.e("DeleteAccount", "Exception: ${e.message}")
                                    // 사용자에게 에러 메시지 표시 (예: Toast)
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "탈퇴 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    enabled = isChecked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyPurple
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "탈퇴하기",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
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
        DeleteAccountScreen(navController = rememberNavController())
    }
}
