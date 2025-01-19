package com.unitip.mobile.features.chat.data.repositories

import android.util.Log
import com.google.gson.Gson
import com.unitip.mobile.features.chat.domain.callbacks.RealtimeChat
import com.unitip.mobile.features.chat.domain.models.Message
import com.unitip.mobile.shared.commons.configs.MqttTopics
import com.unitip.mobile.shared.data.providers.MqttProvider
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealtimeChatRepository @Inject constructor(
    mqttProvider: MqttProvider
) {
    companion object {
        private const val TAG = "RealtimeChatRepository"
    }

    private val client: MqttAndroidClient = mqttProvider.client
    private val gson = Gson()
    private var messageListener: RealtimeChat.MessageListener? = null
    private var typingStatusListener: RealtimeChat.TypingStatusListener? = null

    // topics
    private lateinit var publishSubscribeMessageTopic: String
    private lateinit var publishTypingStatusTopic: String
    private lateinit var subscribeTypingStatusTopic: String
//    private val topicPrefix = "com.unitip/${BuildConfig.MQTT_SECRET}/chat"
//    private lateinit var messagePubTopic: String
//    private lateinit var messageSubTopic: String
//    private lateinit var typingStatusPubTopic: String
//    private lateinit var typingStatusSubTopic: String

    fun openConnection(
        roomId: String,
        currentUserId: String,
        otherUserId: String
    ) {
        publishSubscribeMessageTopic = MqttTopics.Chats.publishSubscribeMessage(
            roomId = roomId
        )
        publishTypingStatusTopic = MqttTopics.Chats.publishTypingStatus(
            currentUserId = currentUserId
        )
        subscribeTypingStatusTopic = MqttTopics.Chats.subscribeTypingStatus(
            otherUserId = otherUserId
        )
//        messagePubTopic = "$topicPrefix/message/$otherUserId-$currentUserId"
//        messageSubTopic = "$topicPrefix/message/$currentUserId-$otherUserId"
//        typingStatusPubTopic = "$topicPrefix/typing-status/$otherUserId-$currentUserId"
//        typingStatusSubTopic = "$topicPrefix/typing-status/$currentUserId-$otherUserId"

        subscribeToTopics(
            roomId = roomId,
            currentUserId = currentUserId
        )

        client.setCallback(object : MqttCallbackExtended {
            override fun messageArrived(topic: String?, message: MqttMessage?) = Unit
            override fun deliveryComplete(token: IMqttDeliveryToken?) = Unit
            override fun connectionLost(cause: Throwable?) =
                unsubscribeFromTopics()

            override fun connectComplete(reconnect: Boolean, serverURI: String?) =
                subscribeToTopics(
                    roomId = roomId,
                    currentUserId = currentUserId
                )
        })
    }

    fun unsubscribeFromTopics() {
        if (client.isConnected) {
            client.unsubscribe(publishSubscribeMessageTopic)
            client.unsubscribe(subscribeTypingStatusTopic)
        }
    }

    private fun subscribeToTopics(
        roomId: String,
        currentUserId: String
    ) {
        /**
         * subscribe ke beberapa topic berikut:
         * - messages
         * - typing status
         */
        if (client.isConnected) {
            client.subscribe(publishSubscribeMessageTopic, 2) { _, message ->
                val payload = message.toString()
                Log.d(TAG, "[message subscribe] $payload")
                if (payload.isNotBlank() && messageListener != null) {
                    val receivedMessage = gson.fromJson(payload, Message::class.java)
                    if (receivedMessage.userId != currentUserId)
                        messageListener!!.onMessageReceived(
                            message = receivedMessage
                        )
                }
            }

            client.subscribe(subscribeTypingStatusTopic, 2) { _, message ->
                val payload = message.toString()
                Log.d(TAG, "[typing status subscribe] $payload")
                if (payload.isNotBlank() && typingStatusListener != null)
                    typingStatusListener!!.onTypingStatusReceived(
                        isTyping = payload == roomId
                    )
            }
        }
    }

    fun listenMessages(listener: RealtimeChat.MessageListener) {
        this.messageListener = listener
    }

    fun listenTypingStatus(listener: RealtimeChat.TypingStatusListener) {
        this.typingStatusListener = listener
    }

    fun notifyMessage(message: Message) {
        if (client.isConnected) {
            client.publish(
                topic = publishSubscribeMessageTopic,
                payload = gson.toJson(message).toByteArray(),
                qos = 2,
                retained = false
            )
        }
    }

    fun notifyTypingStatus(roomId: String) {
        /**
         * update ke depan:
         * typing status perlu dihapus melalui onWillTopic untuk mengantisipasi ketika
         * user keluar dari aplikasi atau kehilangan sinyal
         */
        if (client.isConnected) {
            client.publish(
                topic = publishTypingStatusTopic,
                payload = roomId.toByteArray(),
                qos = 2,
                retained = true
            )
        }
    }
}