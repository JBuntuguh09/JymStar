package com.lonewolf.jymstar.utils

import android.app.Activity
import android.net.Uri
import android.provider.OpenableColumns
import androidx.constraintlayout.widget.ConstraintLayout
import com.lonewolf.jymstar.resources.Storage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class API {


    fun POST(path: String, body: String) {
        val url = URL(path)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")

        val requestBody = body
        println(body)

        connection.outputStream.write(requestBody.toByteArray())

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().readText()
            println(response)
        } else {
            println("Error: HTTP status code $responseCode")
        }

        connection.disconnect()
    }

     fun postAPI(URL : String, BODY: String, activity: Activity, linear: ConstraintLayout):String{
//        val layoutInflater = LayoutInflater.from(activity)
//        val alertDialog = AlertDialog.Builder(activity)
//
//        val view = layoutInflater.inflate(R.layout.loading, linear, false)
//
//        val dialog = alertDialog.create()
//
//        dialog.setView(view)
//
//        dialog.show()

        var res = "[]"
        try {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body =
                (BODY).toRequestBody(
                    mediaType
                )
            val request = Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("content-type", "application/json")
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
           // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
           // dialog.dismiss()
        }


        return res

    }
    fun uploadWithHeader(URL : String, fileUri: Uri, activity: Activity, linear: ConstraintLayout):String{
        val storage = Storage(activity)
        var res = "[]"
        try {
            val client = OkHttpClient()
            val mediaType = "image/*".toMediaTypeOrNull()
            val fileName = getFileName(activity, fileUri)
            val file = File(fileUri.path!!)
           // val requestBody = file.asRequestBody(mediaType)
            //val imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody)
//            val requestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", file.name,
//                    file.asRequestBody("image/*".toMediaTypeOrNull())
//                ).build()
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, requestBody)
                .build()

            val request = Request.Builder()
                .url(URL)
                .post(multipartBody)
                .addHeader("Authorization", storage.tokenId!!)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res

    }

    fun getFileName(activity: Activity, uri: Uri): String {
        var fileName = ""
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    fun getAPI(URL : String, activity: Activity, linear: ConstraintLayout):String{
        var res = "[]"
        val storage = Storage(activity)

        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(URL)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", storage.tokenId!!)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res
    }

    fun putAPIWithHeader(URL : String, BODY: String, activity: Activity, linear: ConstraintLayout):String{
        val storage = Storage(activity)
        var res = "[]"
        try {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body =
                (BODY).toRequestBody(
                    mediaType
                )
            val request = Request.Builder()
                .url(URL)
                .put(body)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", storage.tokenId!!)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res

    }

    fun postAPIWithHeader(URL : String, BODY: String, activity: Activity, linear: ConstraintLayout):String{
        val storage = Storage(activity)
        var res = "[]"
        try {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body =
                (BODY).toRequestBody(
                    mediaType
                )
            val request = Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", storage.tokenId!!)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res

    }

    fun deleteAPI(URL : String, activity: Activity, linear: ConstraintLayout):String{
        var res = "[]"
        val storage = Storage(activity)

        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(URL)
                .delete()
                .addHeader("content-type", "application/json")
                .addHeader("Authorization", storage.tokenId!!)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res
    }

    //Get muscle wiki exercises
    fun getMuscleExercisesAPI(URL: String, key:String, host:String):String{
        var res = "[]"

        try {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(URL)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", key)
                .addHeader("X-RapidAPI-Host", host)
                .build()


            val response = client.newCall(request).execute()

            res= (response.body?.string().toString())
            // dialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
            // dialog.dismiss()
        }


        return res
    }



}