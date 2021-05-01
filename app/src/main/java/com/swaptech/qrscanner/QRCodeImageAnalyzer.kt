package com.swaptech.qrscanner

import android.annotation.SuppressLint
import android.graphics.*
import android.media.Image
import android.media.ToneGenerator
import android.widget.ImageView
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.nio.ByteBuffer


class QRCodeImageAnalyzer(private var callback: OnQRCodeAnalyzerResult): ImageAnalysis.Analyzer {


    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        /*
        val mediaImage = imageProxy.image
        if(mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()
            scanner.process(image).addOnSuccessListener { barcodes ->
                if(barcodes.isNotEmpty())  {
                    val barcode = barcodes.get(0)
                    val ok = barcode.displayValue!!

                    callback.onSuccess(ok)

                }
            }.addOnFailureListener {

            }
        }

         */

        if (imageProxy.format == ImageFormat.YUV_420_888
                || imageProxy.format == ImageFormat.YUV_422_888
                || imageProxy.format == ImageFormat.YUV_444_888) {

            val buffer = imageProxy.planes[0].buffer
            val data = buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                    data, imageProxy.width, imageProxy.height, 0, 0, imageProxy.width, imageProxy.height, false)

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            try {
                val result = QRCodeMultiReader().decode(binaryBitmap)
                //val imageSource = image.image
                callback.onSuccess(result.toString())
            } catch (e: Exception) {

            }

        }

        imageProxy.close()
    }


    interface OnQRCodeAnalyzerResult {
        fun onSuccess(result: String)
    }
}