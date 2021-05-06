package com.swaptech.qrscanner

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("DEPRECATION")
class PhotoSaver(private val context: Context) {
    fun save(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"

        var fileOutputStream: OutputStream?

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            context.contentResolver.also {  resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fileOutputStream = uri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDirectory, filename)
            fileOutputStream = FileOutputStream(image)
        }
        
        fileOutputStream.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
        }
    }
}