package com.lonewolf.jymstar.notification

import com.lonewolf.jymstar.notification.NotificationData

data class PushNotification (
    val data: NotificationData,
    val to : String
        )

