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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bestdriver.aaa_klaxon.data.data_source.CustomTopBar
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.viewmodel.ReportHistoryViewModel

// 신고 내역 데이터 클래스
data class ReportHistory(
    val date: String,
    val location: String,
    val sign: String
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportHistoryScreen(
    navController: NavController,
    reportHistoryViewModel: ReportHistoryViewModel = viewModel()
) {
    // ViewModel에서 상태를 직접 읽어옴
    val reportHistories by remember { mutableStateOf(reportHistoryViewModel.reportHistories) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTopBar(
            title = "오분류 신고내역",
            onBackClick = { navController.navigateUp() }
        )

        Text(
            text = "2024년 7월",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 20.dp)
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

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
