package com.swaptech.qrscanner

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.lang.Exception
import java.nio.ByteBuffer


class QRCodeImageAnalyzer(private var callback: OnQRCodeAnalyzerResult): ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        if(image.format == ImageFormat.YUV_420_888
            || image.format == ImageFormat.YUV_422_888
            || image.format == ImageFormat.YUV_444_888) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                data, image.width, image.height, 0,0, image.width, image.height, false)

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            try {
                val result = QRCodeMultiReader().decode(binaryBitmap)
                //val imageSource = image.image
                callback.onSuccess(result.toString())
            } catch (e: Exception) {

            }

        }

        image.close()
    }

    interface OnQRCodeAnalyzerResult {
        fun onSuccess(result: String)
    }
}