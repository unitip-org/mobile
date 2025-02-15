package com.unitip.mobile.features.home.presentation.sceens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.Bike
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.RefreshCw
import com.unitip.mobile.features.home.presentation.states.DashboardCustomerState
import com.unitip.mobile.features.home.presentation.viewmodels.DashboardCustomerViewModel
import com.unitip.mobile.features.job.commons.JobRoutes
import com.unitip.mobile.features.offer.commons.OfferRoutes
import com.unitip.mobile.shared.commons.compositional.LocalNavController
import com.unitip.mobile.shared.presentation.components.CustomIconButton

@Composable
fun DashboardCustomerScreen(
    viewModel: DashboardCustomerViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(JobRoutes.Create)
                }
            ) {
                Icon(Lucide.Plus, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.surfaceContainerLowest,
                        )
                    )
                )
                .padding(it)
        ) {
            // app bar
            Row(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Halo, Rizal Dwi Anggoro!",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Berikut kami sajikan beberapa ringkasan untuk Anda",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                CustomIconButton(
                    onClick = {
                        viewModel.getAllOrders()
                    },
                    icon = Lucide.RefreshCw
                )
            }

            if (false)
                HorizontalDivider()

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // active driver counter
                item {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = uiState.onlineDriverIds.size.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 16.dp)
                            ) {
                                Text(
                                    text = "Driver Aktif",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Segera pesan jasa driver atau ikuti penawaran yang mereka tawarkan!",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // my services
                item {
                    Text(
                        text = "Layanan Kami",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    )
                }

                item {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                            .fillMaxWidth()
                            .height((screenWidth - (2 * 16.dp)) / 4 * 2),
                        columns = GridCells.Fixed(count = 4),
                        userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(6) {
                            Box(
                                modifier = Modifier
                                    .size((screenWidth - (2 * 16.dp) - (3 * 8.dp)) / 4)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Lucide.Bike,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Anjem",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // active order
                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
                        Text(
                            text = "Pesanan Anda",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "Berikut beberapa pesanan Anda yang sedang aktif",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                with(uiState.detail) {
                    when (this) {
                        is DashboardCustomerState.Detail.Loading -> {
                            item {
                                CircularProgressIndicator()
                            }
                        }

                        is DashboardCustomerState.Detail.Success -> {
                            items(orders) { order ->
                                ListItem(
                                    modifier = Modifier.clickable {
                                        when (order.type) {
                                            "job" -> navController.navigate(
                                                JobRoutes.DetailOrderCustomer(jobId = order.id)
                                            )
                                            "offer" -> navController.navigate(
                                                OfferRoutes.DetailOfferCustomer(offerId = order.id)
                                            )
                                        }
                                    },
                                    overlineContent = {
                                        Text(
                                            text = when(order.type) {
                                                "job" -> "Pesanan Jasa"
                                                "offer" -> "Tawaran Driver"
                                                else -> "Pesanan"
                                            }
                                        )
                                    },
                                    headlineContent = { Text(text = order.title) },
                                    supportingContent = {
                                        Column {
                                            Text(text = order.note)
                                            order.status?.let {
                                                Text(
                                                    text = when(it) {
                                                        "pending" -> "Menunggu Konfirmasi"
                                                        "accepted" -> "Diterima"
                                                        "on_the_way" -> "Dalam Perjalanan"
                                                        "rejected" -> "Ditolak"
                                                        else -> it
                                                    },
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        else ->
                            Unit

                    }
                }

                // spacer
                item {
                    Spacer(modifier = Modifier.height((56 + 32).dp))
                }
            }
        }
    }
}
