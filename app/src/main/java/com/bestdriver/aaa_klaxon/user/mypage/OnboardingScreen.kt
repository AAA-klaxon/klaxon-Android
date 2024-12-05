package com.bestdriver.aaa_klaxon.user.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple

@Composable
fun OnboardingScreen(navController: NavController) {
    val images = listOf(
        R.drawable.onboarding1
    )
    var currentIndex by remember { mutableStateOf(0) }

    // 1초 후에 로그인 화면으로 이동
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000L) // 1초 지연
        navController.navigate("login") {
            popUpTo("onboarding") { inclusive = true } // 온보딩 화면 제거
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MyPurple),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = images[currentIndex]),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxSize(0.85f), // 이미지 크기 조정
            contentScale = ContentScale.Fit // 이미지가 잘리지 않도록 맞춤
        )
    }
}
