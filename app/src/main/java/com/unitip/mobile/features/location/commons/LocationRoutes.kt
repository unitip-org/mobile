package com.unitip.mobile.features.location.commons

import kotlinx.serialization.Serializable

@Serializable
sealed class LocationRoutes {
    @Serializable
    data class PickLocation(
        val resultKey: String,
        val initialLatitude: Double? = null,
        val initialLongitude: Double? = null
    )
}