package com.bestdriver.aaa_klaxon.mypage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar

class ReportHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAA_klaxonTheme {
                ReportHistoryScreen(onBackPressed = { finish() })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportHistoryScreen(onBackPressed: () -> Unit) {
    // 예시 신고 내역
    val reportHistories = listOf(
        ReportHistory(
            date = "2024.07.24",
            location = "서울특별시 강북구 4.19로",
            sign = "우회전 표지판"
        ),
        ReportHistory(
            date = "2024.07.14",
            location = "서울특별시 강북구 수유동 270-63",
            sign = "진입금지 표지판"
        )
    )

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = rememberNavController(), // 필요시 navController를 전달
                pageTitle = "오분류 신고내역",
                onNavigationIconClick = onBackPressed
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // topBar에 의해 생기는 padding 처리
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "2024년 7월",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            if (reportHistories.isEmpty()) {
                Text("신고 내역이 없습니다.")
            } else {
                LazyColumn {
                    items(reportHistories) { report ->
                        ReportHistoryItem(report)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun ReportHistoryItem(report: ReportHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "날짜: ${report.date}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "오분류 발생 위치: ${report.location}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "표지판: ${report.sign}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}

data class ReportHistory(
    val date: String,
    val location: String,
    val sign: String
)
