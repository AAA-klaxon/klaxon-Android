package com.bestdriver.aaa_klaxon.user.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.LiveData
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar
import com.bestdriver.aaa_klaxon.viewmodel.NoticeViewModel
import kotlin.reflect.KProperty



@Composable
fun NoticeLetterScreen(navController: NavController, noticeId: String, viewModel: NoticeViewModel) {
    // ViewModel에서 공지사항 리스트를 가져옵니다.
    val notices by viewModel.notices.observeAsState(emptyList()) // LiveData를 observeAsState로 변경

    // noticeId에 해당하는 공지사항을 찾습니다.
    val notice = notices.find { it.id == noticeId }

    // 공지사항이 존재할 때 UI를 구성합니다.
    notice?.let {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    navController = navController,
                    pageTitle = "공지사항",
                    onNavigationIconClick = { navController.navigateUp() }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues) // topBar에 의해 생기는 padding 처리
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // 공지사항의 제목을 표시합니다.
                Text(
                    text = it.title,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                // 공지사항의 날짜를 표시합니다.
                Text(
                    text = it.date,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp)
                )

                // 공지사항과 본문 사이의 선을 표시합니다.
                ThinHorizontalLine() // 수정된 이름을 사용하세요. (여기서는 가정한 것임)

                // 공지사항의 본문을 표시합니다.
                Text(
                    text = it.body,
                    fontSize = 17.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNoticeLetterScreen() {
    val navController = rememberNavController()
    val viewModel = NoticeViewModel() // 여기에 적절한 ViewModel 인스턴스를 제공하세요.
    NoticeLetterScreen(
        navController = navController,
        noticeId = "1", // 여기에 적절한 noticeId를 제공하세요.
        viewModel = viewModel
    )
}