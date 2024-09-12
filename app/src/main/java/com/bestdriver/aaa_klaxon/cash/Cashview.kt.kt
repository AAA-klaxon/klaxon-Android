package com.bestdriver.aaa_klaxon.cash

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.data.data_source.CustomTopBar

@Composable
fun CashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 바
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CustomTopBar(
                title = "적립내역",
                onBackClick = { navController.navigateUp() }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        // 나머지 콘텐츠
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            CashDetails()
            Spacer(modifier = Modifier.height(16.dp))
            CashHistory()
        }
    }
}

@Composable
fun CashDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "내캐시",
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                )
                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.Bottom,) {
                    Text(
                        text = "10,300",
                        fontSize = 35.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    )
                    Text(
                        text = "원",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* Handle 새로고침 클릭 */ },
                    modifier = Modifier
                        .width(85.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF2E3E2)),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = true,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text(
                        text = "새로고침",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),

                        )
                }

                Button(
                    onClick = { /* Handle 사용하기 클릭 */ },
                    modifier = Modifier
                        .width(85.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFE8EEFB)),
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = true,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text(
                        text = "사용하기",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    )
                }
            }
        }

        // 구분선 추가
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}

@Composable
fun CashHistory() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // 날짜별 캐시 내역을 그룹화하기 위한 예시 데이터
        val cashHistoryData = mapOf(
            "2024년 7월" to listOf(
                Pair("07.24", "+3000 원"),
                Pair("07.19", "+530 원"),
                Pair("07.07", "-2000 원")
            ),
            "2024년 6월" to listOf(
                Pair("06.15", "+1000 원"),
                Pair("06.10", "-500 원")
            )
        )

        cashHistoryData.forEach { (date, transactions) ->
            item {
                Column {
                    Text(
                        text = date,
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
            items(transactions) { (day, amount) ->
                val isCredit = amount.startsWith("+")
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = day, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = amount,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                color = if (isCredit) Color.Red else Color.Blue
                            )
                        }

                        Text(
                            text = if (isCredit) "적립" else "사용",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 13.sp,
                            color = if (isCredit) Color.Red else Color.Blue
                        )
                    }
                    // 금액 항목 아래 구분선 추가
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        thickness = 0.3.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewCashScreen() {
    val navController = rememberNavController() // Preview용 NavController
    CashScreen(navController = navController)
}