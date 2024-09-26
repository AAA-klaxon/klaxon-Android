package com.bestdriver.aaa_klaxon.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R
import com.bestdriver.aaa_klaxon.community.ThinHorizontalLine
import com.bestdriver.aaa_klaxon.ui.theme.MyPurple


@Composable
fun TitleCard(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 15.dp)
    ) {
        // 상단 아이콘과 텍스트
        Row(
            modifier = Modifier.padding(bottom = 3.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "내 캐시 아이콘",
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 5.dp)
            )
            Spacer(modifier = Modifier.width(4.dp)) // 아이콘과 텍스트 사이의 여백
            Text(
                text = "내캐시",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                modifier = Modifier
                    .padding(top = 5.dp),
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            )
        }

        // 하단 텍스트와 상자
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 캐시 금액
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "10,300",
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    style = TextStyle(
                        fontSize = 30.sp
                    ),
                    modifier = Modifier.alignByBaseline()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "원",
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    style = TextStyle(
                        fontSize = 15.sp
                    ),
                    modifier = Modifier.alignByBaseline()
                )
            }

            // 적립내역보기 상자
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .height(37.dp)
                    .size(130.dp)
                    .background(
                        color = MyPurple.copy(alpha = 0.3f), // 배경색 설정
                        shape = RoundedCornerShape(5.dp) // 모서리 둥글게
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp) // 내부 여백
                    .clickable {
                        navController.navigate("cash") // CashView로 이동
                    }
            ) {
                Text(
                    text = "적립내역 보기",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Black
                    ),
                    modifier = Modifier.align(Alignment.Center) // 텍스트 가운데 정렬
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        ThinHorizontalLine()
        Spacer(modifier = Modifier.height(10.dp))
    }
}




@Composable
fun MapCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "실시간 정보",
            style = TextStyle(
                fontSize = 25.sp, // 텍스트 크기 설정
                fontFamily = FontFamily(Font(R.font.pretendard_semibold)), // 텍스트 굵기 설정
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 왼쪽에 주소 텍스트
            Text(
                text = "서울특별시 도봉구 삼양로144길 33",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    color = Color.Gray
                )
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "내 위치 아이콘",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 5.dp)
                )
                Spacer(modifier = Modifier.width(4.dp)) // 아이콘과 텍스트 사이의 여백
                Text(
                    text = "내위치",
                    modifier = Modifier
                        .padding(top = 5.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Gray
                    )
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "지도",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)// 가로 폭에 맞게 조정
                .padding(top = 8.dp) // 상단 여백 추가
        )
    }
}

@Composable
fun ListCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .shadow(7.dp, RoundedCornerShape(9.dp))
            .background(
                color = Color.White, shape = RoundedCornerShape(12.dp)
            )
            .height(310.dp)



    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "오분류가 많은 지역",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_semibold)),
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row( modifier = Modifier
            )
            {
                Text(
                    text = "많음",
                    modifier = Modifier
                        .padding(top = 3.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color.Red
                    )
                )

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "오분류 아이콘",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 5.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "보통",
                    modifier = Modifier
                        .padding(top = 3.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color(0xFFFFA500)
                    )
                )

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "오분류 아이콘",
                    tint = Color(0xFFFFA500),
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 5.dp)

                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "적음",
                    modifier = Modifier
                        .padding(top = 3.dp),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = Color(0xFFE0C200)
                    )
                )

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "오분류 아이콘",
                    tint = Color(0xFFE0C200),
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 5.dp)
                )

            }

        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "오분류 아이콘",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 5.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                text = "서울특별시 강북구 4.19로",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(
                                text = "우회전 표지판 인식 결과 80% 오분류",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 13.sp
                            )
                        }

                    }

                }
                Divider(
                    color = Color.Gray,
                    thickness = 0.5.dp
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Row{
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "오분류 아이콘",
                            tint = Color(0xFFFFA500),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 5.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                text = "서울특별시 도봉구 우이천로38길 18-6",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Text(
                                text = "서행표지판 인식 결과 50% 오분류",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))

                    }

                }
                Divider(
                    color = Color.Gray,
                    thickness = 0.5.dp
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "오분류 아이콘",
                            tint = Color(0xFFE0C200),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 5.dp)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(
                                text = "서울특별시 강북구 수유동 270-63",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Text(
                                text = "진입금지 표지판 인식 결과 15% 오분류",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 13.sp
                                )
                        }
                        Spacer(modifier = Modifier.width(15.dp))

                    }

                }
            }
        }
    }
}


@Composable
fun MyScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleCard(navController)
        LazyColumn() {
            item {
                MapCard()
            }
            item {
                ListCard()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewMapCard() {
    MyScreen(navController = rememberNavController())
}
