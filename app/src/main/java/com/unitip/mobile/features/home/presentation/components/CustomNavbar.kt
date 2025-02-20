package com.unitip.mobile.features.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.composables.icons.lucide.BadgeHelp
import com.composables.icons.lucide.BriefcaseBusiness
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.LayoutDashboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MessagesSquare
import com.composables.icons.lucide.User
import com.unitip.mobile.features.home.commons.HomeRoutes
import com.unitip.mobile.shared.commons.compositional.LocalHomeNavController
import com.unitip.mobile.shared.commons.extensions.isCustomer
import com.unitip.mobile.shared.commons.extensions.isDriver
import com.unitip.mobile.shared.domain.models.Session

private data class NavigationItem<T : Any>(
    val title: String,
    val icon: ImageVector,
    val route: T
)

@Composable
fun CustomNavbar(
    session: Session
) {
    val navigationItems = listOfNotNull(
        NavigationItem(
            "Dashboard",
            icon = Lucide.LayoutDashboard,
            route = HomeRoutes.Dashboard
        ),
        when (session.isDriver()) {
            true -> NavigationItem(
                "Jobs",
                icon = Lucide.BriefcaseBusiness,
                route = HomeRoutes.Jobs
            )

            else -> null
        },
        when (session.isCustomer()) {
            true -> NavigationItem(
                "Offers",
                icon = Lucide.BadgeHelp,
                route = HomeRoutes.Offers
            )

            else -> null
        },
        NavigationItem(
            "Social",
            icon = Lucide.Heart,
            route = HomeRoutes.Social
        ),
        NavigationItem(
            "Chats",
            icon = Lucide.MessagesSquare,
            route = HomeRoutes.Chats
        ),
        NavigationItem(
            "Profile",
            icon = Lucide.User,
            route = HomeRoutes.Profile
        ),
    )

    val homeNavController = LocalHomeNavController.current
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column {
        HorizontalDivider()
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = navigationItems.size),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            items(navigationItems) { item ->
                val isSelected =
                    currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

                Column(
                    modifier = Modifier
                        .height(56.dp)
                        .clickable {
                            homeNavController.navigate(item.route) {
                                popUpTo(homeNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = when (isSelected) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.outline.copy(alpha = .64f)
                        },
                    )
                }
            }
        }
    }
}
