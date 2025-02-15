package com.unitip.mobile.features.home.presentation.states

import com.unitip.mobile.features.home.domain.models.GetDashboardDriverResult

data class DashboardDriverState(
    val detail: Detail = Detail.Loading
) {
    sealed interface Detail {
        data object Initial : Detail
        data object Loading : Detail
        data class Success(
            val data: GetDashboardDriverResult
        ) : Detail

        data class Failure(
            val message: String
        ) : Detail
    }
}
