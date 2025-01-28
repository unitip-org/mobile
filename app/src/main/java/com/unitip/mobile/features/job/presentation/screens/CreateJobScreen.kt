package com.unitip.mobile.features.job.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.Bike
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Lucide
import com.unitip.mobile.features.job.commons.JobConstants
import com.unitip.mobile.features.job.presentation.states.CreateJobState
import com.unitip.mobile.features.job.presentation.viewmodels.CreateJobViewModel
import com.unitip.mobile.features.location.commons.LocationRoutes
import com.unitip.mobile.shared.commons.compositional.LocalNavController
import com.unitip.mobile.shared.commons.extensions.GetPopResult
import com.unitip.mobile.shared.presentation.components.CustomCard
import com.unitip.mobile.shared.presentation.components.CustomIconButton
import com.unitip.mobile.shared.presentation.components.CustomTextField
import com.unitip.mobile.shared.presentation.components.StaticMapPreview
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateJobScreen(
    viewModel: CreateJobViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val isImeVisible = WindowInsets.isImeVisible
    val listState = rememberLazyListState()
    val firstVisibleListItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    var title by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var pickupLocation by rememberSaveable { mutableStateOf("") }
    var pickupLocationGeoPoint by rememberSaveable { mutableStateOf<GeoPoint?>(null) }
    var destinationLocation by rememberSaveable { mutableStateOf("") }
    var destinationLocationGeoPoint by rememberSaveable { mutableStateOf<GeoPoint?>(null) }
    var isSelectServiceVisible by rememberSaveable { mutableStateOf(false) }
    var selectedService by rememberSaveable { mutableStateOf("") }
    var isJoinAllowed by rememberSaveable { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    navController.GetPopResult<GeoPoint>(key = "pickupLocationGeoPoint") {
        if (it != null)
            pickupLocationGeoPoint = it
    }
    navController.GetPopResult<GeoPoint>(key = "destinationLocationGeoPoint") {
        if (it != null)
            destinationLocationGeoPoint = it
    }

    LaunchedEffect(uiState.detail) {
        with(uiState.detail) {
            when (this) {
                is CreateJobState.Detail.Failure -> {
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = "Oke",
                        duration = SnackbarDuration.Indefinite
                    )
                    viewModel.resetState()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                CustomIconButton(
                    icon = Lucide.ChevronLeft,
                    onClick = { navController.popBackStack() }
                )
                AnimatedVisibility(
                    visible = listState.canScrollBackward && firstVisibleListItemIndex.value > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(text = "Pekerjaan Baru", style = MaterialTheme.typography.titleLarge)
                }
            }

            AnimatedVisibility(
                visible = listState.canScrollBackward &&
                        uiState.detail !is CreateJobState.Detail.Loading
            ) {
                HorizontalDivider()
            }

            // loading bar
            AnimatedVisibility(visible = uiState.detail is CreateJobState.Detail.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp
                        )
                        .fillMaxWidth(),
                    strokeCap = StrokeCap.Round,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .imePadding(),
                state = listState
            ) {
                item {
                    Text(
                        text = "Pekerjaan Baru",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = when (uiState.detail is CreateJobState.Detail.Loading) {
                                true -> 16.dp
                                else -> 0.dp
                            }
                        )
                    )
                }
                item {
                    Text(
                        text = "Masukkan beberapa informasi berikut untuk membuat pekerjaan baru",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                }

                item {
                    CustomCard(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = uiState.detail !is CreateJobState.Detail.Loading
                                    ) {
                                        isSelectServiceVisible = !isSelectServiceVisible
                                    }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    Icon(
                                        Lucide.Bike,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.Center),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Jenis Layanan",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = when (selectedService.isBlank()) {
                                            true -> "Pilih jenis layanan"
                                            else -> JobConstants.services.find { service ->
                                                service.value == selectedService
                                            }!!.title
                                        },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Icon(
                                    when (isSelectServiceVisible) {
                                        true -> Lucide.ChevronUp
                                        else -> Lucide.ChevronDown
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            AnimatedVisibility(visible = isSelectServiceVisible) {
                                Column {
                                    HorizontalDivider()

                                    JobConstants.services.map { item ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    isSelectServiceVisible = false
                                                    selectedService = item.value
                                                }
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Icon(
                                                when (item.value == selectedService) {
                                                    true -> Lucide.CircleCheck
                                                    else -> Lucide.Circle
                                                },
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                /**
                 * form untuk membuat pekerjaan baru
                 */
                item {
                    Column(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        CustomTextField(
                            label = "Judul pekerjaan",
                            value = title,
                            onValueChange = { title = it },
                            placeholder = "Cth: Anjem ke Gacoan",
                            enabled = uiState.detail !is CreateJobState.Detail.Loading
                        )

                        CustomTextField(
                            label = "Lokasi penjemputan",
                            value = pickupLocation,
                            onValueChange = { pickupLocation = it },
                            placeholder = "Cth: Kos Unitip no.2",
                            modifier = Modifier.padding(top = 16.dp),
                            enabled = uiState.detail !is CreateJobState.Detail.Loading
                        )

                        when (pickupLocationGeoPoint) {
                            null -> Button(onClick = {
                                navController.navigate(
                                    LocationRoutes.PickLocation(
                                        resultKey = "pickupLocationGeoPoint"
                                    )
                                )
                            }) {
                                Text(text = "Ganti lokasi")
                            }

                            else -> StaticMapPreview(
                                modifier = Modifier.padding(top = 8.dp),
                                geoPoint = pickupLocationGeoPoint!!,
                                onClick = {
                                    navController.navigate(
                                        LocationRoutes.PickLocation(
                                            resultKey = "pickupLocationGeoPoint",
                                            initialLatitude = pickupLocationGeoPoint!!.latitude,
                                            initialLongitude = pickupLocationGeoPoint!!.longitude
                                        )
                                    )
                                })
                        }

                        CustomTextField(
                            label = "Lokasi tujuan",
                            value = destinationLocation,
                            onValueChange = { destinationLocation = it },
                            placeholder = "Cth: Gacoan Jebres",
                            modifier = Modifier.padding(top = 16.dp),
                            enabled = uiState.detail !is CreateJobState.Detail.Loading
                        )

                        when (destinationLocationGeoPoint) {
                            null -> Button(onClick = {
                                navController.navigate(
                                    LocationRoutes.PickLocation(
                                        resultKey = "destinationLocationGeoPoint"
                                    )
                                )
                            }) {
                                Text(text = "Ganti lokasi")
                            }

                            else -> StaticMapPreview(
                                modifier = Modifier.padding(top = 8.dp),
                                geoPoint = destinationLocationGeoPoint!!,
                                onClick = {
                                    navController.navigate(
                                        LocationRoutes.PickLocation(
                                            resultKey = "destinationLocationGeoPoint",
                                            initialLatitude = destinationLocationGeoPoint!!.latitude,
                                            initialLongitude = destinationLocationGeoPoint!!.longitude
                                        )
                                    )
                                })
                        }

                        CustomTextField(
                            label = "Catatan tambahan",
                            value = note,
                            onValueChange = { note = it },
                            placeholder = "Cth: Tolong bawain helm",
                            minLines = 5,
                            modifier = Modifier.padding(top = 16.dp),
                            enabled = uiState.detail !is CreateJobState.Detail.Loading
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // button create
            if (!isImeVisible) {
                AnimatedVisibility(visible = uiState.detail !is CreateJobState.Detail.Loading) {
                    Button(
                        onClick = {
                            if (selectedService.isNotBlank())
                                if (!isJoinAllowed) {
                                    viewModel.createSingleJob(
                                        title = title,
                                        note = note,
                                        pickupLocation = pickupLocation,
                                        destination = destinationLocation,
                                        service = selectedService

                                    )
                                } else {
                                    viewModel.createMultiJob(
                                        title = title,
                                        note = note,
                                        pickupLocation = pickupLocation,
                                        service = selectedService

                                    )
                                }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text("Selesai")
                    }
                }
            }
        }
    }
}