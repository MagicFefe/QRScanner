package com.swaptech.qrscanner

import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import java.lang.Exception

class QRDrawer(private val manager: WindowManager) {
    fun draw(data: String, view: ImageView) {
        try {
            val display = manager.defaultDisplay

            val point = Point()

            display.getSize(point)
            val width = point.x
            val height = point.y

            var dimen: Int = if (width < height) width else height
            dimen = dimen * 3 / 4
            val qrGenerator = QRGEncoder(data, null, QRGContents.Type.TEXT, dimen)
            val bitmap = qrGenerator.encodeAsBitmap()
            view.setImageBitmap(bitmap)

        } catch (e: Exception) {

        }
    }
}