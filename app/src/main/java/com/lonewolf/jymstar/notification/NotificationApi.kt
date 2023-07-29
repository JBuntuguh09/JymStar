package com.lonewolf.jymstar.notification


import com.lonewolf.jymstar.resources.Constant.Companion.CONTENT_TYPE
import com.lonewolf.jymstar.resources.Constant.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotificattion(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}

