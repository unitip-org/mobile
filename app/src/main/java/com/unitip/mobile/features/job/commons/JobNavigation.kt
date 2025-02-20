package com.unitip.mobile.features.job.commons

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.unitip.mobile.features.job.presentation.screens.ApplyJobScreen
import com.unitip.mobile.features.job.presentation.screens.CreateJobScreen
import com.unitip.mobile.features.job.presentation.screens.DetailJobScreen
import com.unitip.mobile.features.job.presentation.screens.DetailOrderJobCustomerScreen
import com.unitip.mobile.features.job.presentation.screens.DetailOrderJobDriverScreen

fun NavGraphBuilder.jobNavigation() {
    composable<JobRoutes.Create> {
        CreateJobScreen()
    }

    composable<JobRoutes.Detail> {
        val data = it.toRoute<JobRoutes.Detail>()
        DetailJobScreen(jobId = data.jobId)
    }

    composable<JobRoutes.DetailOrderDriver> {
        val data = it.toRoute<JobRoutes.DetailOrderDriver>()
        DetailOrderJobDriverScreen(
            orderId = data.orderId
        )
    }

    composable<JobRoutes.DetailOrderCustomer> {
        DetailOrderJobCustomerScreen()
    }

    composable<JobRoutes.Apply> {
        ApplyJobScreen()
    }
}