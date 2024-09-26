package com.bestdriver.aaa_klaxon.util

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bestdriver.aaa_klaxon.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    navController: NavController,
    pageTitle: String,
    titleColor: Color = Color.Black, // Default title color
    onNavigationIconClick: () -> Unit = { navController.navigateUp() },
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .fillMaxWidth(), // Ensure the app bar fills the width of its parent
            navigationIcon = {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            title = {
                Text(
                    text = pageTitle,
                    fontFamily = FontFamily(Font(R.font.pretendard_extrabold)),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    color = titleColor // Apply the color to the title text
                )
            },
            actions = {
                // Add any actions if needed
            }
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp)// Add horizontal padding
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomTopAppBar() {
    // Mock NavController for preview
    val navController = rememberNavController()

    CustomTopAppBar(
        navController = navController,
        pageTitle = "프로필 편집",
        titleColor = Color.Blue // Example of custom title color
    )
}

