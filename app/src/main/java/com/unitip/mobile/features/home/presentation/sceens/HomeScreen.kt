package com.unitip.mobile.features.home.presentation.sceens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.unitip.mobile.features.home.commons.HomeRoutes
import com.unitip.mobile.features.home.commons.nestedHomeNavigation
import com.unitip.mobile.features.home.presentation.components.CustomNavbar
import com.unitip.mobile.features.home.presentation.viewmodels.HomeViewModel
import com.unitip.mobile.shared.commons.compositional.LocalHomeNavController

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeNavController = rememberNavController()

    val session = viewModel.session

    CompositionLocalProvider(LocalHomeNavController provides homeNavController) {
        Scaffold(
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(
                sides = WindowInsetsSides.Bottom
            )
        ) {
            Column(modifier = Modifier.padding(it)) {
                NavHost(
                    navController = homeNavController,
                    startDestination = HomeRoutes.Dashboard,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    nestedHomeNavigation(session = session)
                }
                CustomNavbar(session = session)
            }
        }
    }
}

