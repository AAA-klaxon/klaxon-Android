
package com.bestdriver.aaa_klaxon

import CommunityScreen
import CommunityWriteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bestdriver.aaa_klaxon.cash.CashScreen
import com.bestdriver.aaa_klaxon.community.CommunityFeedScreen
import com.bestdriver.aaa_klaxon.home.ListCard
import com.bestdriver.aaa_klaxon.home.MapCard
import com.bestdriver.aaa_klaxon.home.MyScreen
import com.bestdriver.aaa_klaxon.home.TitleCard
import com.bestdriver.aaa_klaxon.login.LoginScreen
import com.bestdriver.aaa_klaxon.login.SignUpScreen
import com.bestdriver.aaa_klaxon.mypage.DeleteAccountScreen
import com.bestdriver.aaa_klaxon.mypage.MyPageScreen
import com.bestdriver.aaa_klaxon.mypage.ProfileEditScreen
import com.bestdriver.aaa_klaxon.mypage.ReportHistoryScreen
import com.bestdriver.aaa_klaxon.network.community.CommunityWriteScreenViewModel
import com.bestdriver.aaa_klaxon.ui.theme.AAA_klaxonTheme
import com.bestdriver.aaa_klaxon.user.mypage.NoticeHomeScreen
import com.bestdriver.aaa_klaxon.user.mypage.NoticeLetterScreen
import com.bestdriver.aaa_klaxon.util.BottomNavigationItem
import com.bestdriver.aaa_klaxon.util.CustomBottomBar
import com.bestdriver.aaa_klaxon.viewmodel.NoticeViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AAA_klaxonTheme {
                val navController = rememberNavController()
                val communityViewModel: CommunityWriteScreenViewModel = viewModel()
                val noticeViewModel: NoticeViewModel = viewModel()

                val items = listOf(
                    BottomNavigationItem(
                        title = "커뮤니티",
                        selectedIcon = Icons.Filled.Email,
                        unselectedIcon = Icons.Outlined.Email,
                        hasNews = false,
                        route = "communityHome"
                    ),
                    BottomNavigationItem(
                        title = "홈",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = false,
                        route = "main"
                    ),
                    BottomNavigationItem(
                        title = "마이페이지",
                        selectedIcon = Icons.Filled.Person,
                        unselectedIcon = Icons.Outlined.Person,
                        hasNews = false,
                        route = "myPage"
                    )
                )

                var selectedItemIndex by rememberSaveable { mutableStateOf(1) }

                Scaffold(
                    bottomBar = {
                        // 현재 화면이 login, signup, communityWrite가 아닌지 확인
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        if (currentRoute in listOf("communityHome", "main", "myPage")) {
                            CustomBottomBar(
                                navController = navController,
                                items = items,
                                selectedItemIndex = selectedItemIndex,
                                onItemSelected = { index ->
                                    selectedItemIndex = index
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        communityViewModel = communityViewModel,
                        noticeViewModel = noticeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}




@Composable
fun AppNavGraph(
    navController: NavHostController,
    communityViewModel: CommunityWriteScreenViewModel,
    noticeViewModel: NoticeViewModel, // 추가된 부분
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        Modifier.then(modifier)
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("main")
            }, navController = navController)
        }
        composable("main") {
            MyScreen(navController)
        }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("cash") {
            CashScreen(navController)
        }
        composable("communityHome") { backStackEntry ->
            val newPostId = backStackEntry.arguments?.getString("newPostId")?.toIntOrNull()
            CommunityScreen(
                navController = navController,
                viewModel = communityViewModel,
                newPostId = newPostId
            )
        }

        composable("communityWrite") {
            val coroutineScope = rememberCoroutineScope()
            CommunityWriteScreen(
                navController = navController,
                viewModel = communityViewModel,
                userName = "임시 사용자",
                onSubmitClick = { title, body, nickname ->
                    coroutineScope.launch {
                        val newPostId = communityViewModel.addPost(title = title, body = body, nickname = nickname)
                        newPostId?.let {
                            navController.navigate("communityHome?newPostId=$it") {
                                popUpTo("communityWrite") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(
            route = "communityFeed/{postId}/{title}/{mainText}/{createdAt}/{likeCount}/{nickname}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("mainText") { type = NavType.StringType },
                navArgument("createdAt") { type = NavType.StringType },
                navArgument("likeCount") { type = NavType.StringType },
                navArgument("nickname") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")

            if (postId != null) {
                CommunityFeedScreen(
                    navController = navController,
                    viewModel = communityViewModel,
                    postId = postId
                )
            } else {
                // 오류 화면으로 이동
                navController.navigate("errorScreen")
                navController.popBackStack() // 이전 화면으로 돌아가게 함
            }
        }

        composable("myPage") {
            MyPageScreen(navController)
        }
        composable("deleteAccount") {
            DeleteAccountScreen(navController)
        }
        composable("profileEdit") {
            ProfileEditScreen(navController)
        }
        composable("notice") {
            NoticeHomeScreen(
                navController = navController,
                viewModel = noticeViewModel
            )
        }
        composable("noticeLetter/{noticeId}") { backStackEntry ->
            val noticeId = backStackEntry.arguments?.getString("noticeId") ?: return@composable
            NoticeLetterScreen(
                navController = navController,
                noticeId = noticeId,
                viewModel = noticeViewModel
            )
        }

        composable("reportHistory") {
            ReportHistoryScreen(onBackPressed = { navController.popBackStack() })
        }
    }
}