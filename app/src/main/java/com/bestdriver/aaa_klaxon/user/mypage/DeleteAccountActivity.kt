package com.bestdriver.aaa_klaxon.mypage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.data.data_source.CustomTopBar
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(navController: NavController, modifier: Modifier = Modifier) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        CustomTopBar(
            title = "탈퇴하기",
            onBackClick = { navController.navigateUp() }
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp)
                .padding(bottom = 16.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "클락션을 탈퇴하면,",
            fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
            fontSize = 25.sp,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Text(
            text = "내 프로필, 주행내역, 게시글, 댓글, 적립캐시\n" +
                    "그 외 사용자가 설정한 모든 정보가 사라지고 복구가 불가능합니다.",
            fontSize = 17.sp,
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
                        // TODO: 탈퇴 처리 로직 추가
                    }
                },
                shape = RoundedCornerShape(6.dp), // 모서리 둥글기를 조절
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
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    color = Color.White
                )
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