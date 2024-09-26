package com.bestdriver.aaa_klaxon.user.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.community.CircleCanvas
import com.bestdriver.aaa_klaxon.community.ThinHorizontalLine
import com.bestdriver.aaa_klaxon.util.CustomTopAppBar
import com.bestdriver.aaa_klaxon.viewmodel.NoticeViewModel

data class Notice(
    val id: String,
    val title: String,
    val date: String,
    val body: String
)


@Composable
fun NoticeHomeScreen(navController: NavController, viewModel: NoticeViewModel) {
    val notices by viewModel.notices.observeAsState(emptyList())

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                pageTitle = "공지사항",
                onNavigationIconClick = { navController.navigate("mypage") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LazyColumn {
                items(notices) { notice ->
                    NoticeItem(
                        title = notice.title,
                        date = notice.date,
                        onClick = {
                            // 선택한 공지사항으로 이동
                            navController.navigate("noticeLetter/${notice.id}")
                        }
                    )
                    ThinHorizontalLine()
                }
            }
        }
    }
}


@Composable
fun NoticeItem(title: String, date: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)
            .clickable { onClick() }, // 아이템 클릭 시 onClick 실행
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            Text(
                text = date,
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                color = Color.Black.copy(0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Arrow Forward",
            modifier = Modifier
                .size(24.dp),
            tint = Color.Black
        )
    }
}


@Composable
fun HorizontalLine() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 2.dp,
        color = Color.Black
    )
}

@Composable
fun ThinHorizontalLine() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = 1.dp,
        color = Color.Black.copy(alpha = 0.2f)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNoticeHomeScreen() {
    val navController = rememberNavController()
    val viewModel = NoticeViewModel()
    NoticeHomeScreen(navController = navController, viewModel = viewModel)
}