package com.lonewolf.jymstar.resources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.TextView
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import java.io.File

class QRCodeScanner(private val filePath:String) {

    fun scanQRCode(): String? {
        val bitmap = BitmapFactory.decodeFile(filePath) ?: run {
            Log.e("QRCodeScanner", "Failed to decode image file")
            return null
        }
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(binaryBitmap)
            println(result.text.toString()+"mmmmmmmm")
            return result.text.toString()
        } catch (e: Exception) {
            Log.e("QRCodeScanner", "Error scanning QR code: ${e.message}")
        }
        return null
    }

    private fun getFilePath(uri: Uri, context: Context): String? {
        val projection = arrayOf("_data")
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow("_data")
                return it.getString(columnIndex)
            }
        }
        return null
    }
}