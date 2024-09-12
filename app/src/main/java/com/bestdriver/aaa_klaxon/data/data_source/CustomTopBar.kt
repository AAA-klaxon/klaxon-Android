package com.bestdriver.aaa_klaxon.data.data_source

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestdriver.aaa_klaxon.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun CustomTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), // 원하는 높이 설정
        contentAlignment = Alignment.Center // Box의 가운데에 모든 자식 컴포넌트를 정렬
    ) {
        // 가운데 정렬된 텍스트
        Text(
            text = title,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally) // 텍스트를 수평으로 가운데 정렬
        )

        Row(
            modifier = Modifier
                .fillMaxSize(), // 전체 Box 크기
            verticalAlignment = Alignment.CenterVertically, // 아이콘과 텍스트를 수직으로 가운데 정렬
            horizontalArrangement = Arrangement.SpaceBetween // 아이콘과 텍스트를 양쪽 사이드에 붙이기
        ) {
            // 뒤로가기 아이콘
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onBackClick() }, // 뒤로가기 클릭 시 이전 페이지로 이동
                tint = Color.Black
            )

        }
    }
}
