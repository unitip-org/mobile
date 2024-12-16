package com.unitip.mobile.core.config

class MqttConfig {
    companion object {
        const val MQTT_SERVER_URI = "tcp://broker.hivemq.com:1883"
        val MQTT_CLIENT_ID = "com.unitip.android-" + System.currentTimeMillis()
    }
}