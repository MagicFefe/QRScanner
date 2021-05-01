package com.swaptech.qrscanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera

import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.*
import com.swaptech.qrscanner.databinding.ActivityMainBinding
import java.nio.channels.AsynchronousFileChannel.open
import java.util.*


class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var qrCode = ""
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    val hasFlashlight by lazy {
        baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }
    var mIntent: Intent? = null

    private val anim by lazy {
        AnimationUtils.loadAnimation(this, R.anim.scale_anim)
    }
    private val interpolator = CustomBounceInterpolator(0.2, 10.toDouble())



    private val qrCodeAnalyzer by lazy {
        QRCodeImageAnalyzer(object : QRCodeImageAnalyzer.OnQRCodeAnalyzerResult {
            override fun onSuccess(result: String) {
                qrCode = result

                try {
                    mIntent.apply {
                        this?.putExtra(EXTRA_DATA_KEY, qrCode)
                    }
                    startActivity(mIntent)
                } catch (e: NullPointerException) {

                }

                mIntent = null
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        supportActionBar?.hide()

        anim.interpolator = interpolator

        if (allPermissionsGranted()) {
            startCamera()

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    PERMISSION_REQUEST_CODE
            )
        }

        mIntent = Intent(this, ResultActivity::class.java)

        binding.pickFromGalleryBtn.setOnClickListener { btn ->

            btn.startAnimation(anim)

            val intent = Intent(Intent.ACTION_PICK).apply { type = MIMETYPE_IMAGES }
            startActivityForResult(intent, PICK_IMAGE_CODE)
        }



    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.scanQrBtn.animate().alpha(0f).setDuration(1650).start()

    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { preview ->
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


            val imageAnalyzer: ImageAnalysis = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this), qrCodeAnalyzer)
            }

            try {
                cameraProvider.unbindAll()

                val cameraX = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

                var counter = 0
                if(cameraX.cameraInfo.hasFlashUnit()) {
                    binding.flashlightBtn.setOnClickListener { btn ->
                        counter++
                        btn.startAnimation(anim)
                        try {
                            if(counter % 2 == 0) {
                                (btn as ImageButton).setImageResource(R.drawable.ic_baseline_flashlight_on_24)
                                cameraX.cameraControl.enableTorch(false)
                            } else {
                                (btn as ImageButton).setImageResource(R.drawable.ic_baseline_flashlight_off_24)
                                cameraX.cameraControl.enableTorch(true)
                            }
                        } catch(e: Exception) {

                        }
                    }

                }

            } catch (e: Exception) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            data?.data?.let { uri ->
                pickImage(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun allPermissionsGranted(): Boolean {
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        mIntent = null
    }

    override fun onRestart() {
        super.onRestart()
        mIntent = Intent(this@MainActivity, ResultActivity::class.java).apply {
            putExtra(EXTRA_DATA_KEY, qrCode)
        }
    }
    private fun pickImage(imageUri: Uri) {

        val img = InputImage.fromFilePath(this, imageUri)
        val scanner = BarcodeScanning.getClient()
        scanner.process(img).addOnSuccessListener { barcodes ->

            val barcode = barcodes.get(0)
            val value = barcode.displayValue

            mIntent = Intent(this, ResultActivity::class.java).apply {
                putExtra(EXTRA_DATA_KEY, value)
            }
            startActivity(mIntent)

        }.addOnFailureListener {
            Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show()
        }


    }


    private companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val REQUIRED_PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE =
            Manifest.permission.READ_EXTERNAL_STORAGE
        val permissions =
            arrayOf(REQUIRED_PERMISSION_CAMERA, REQUIRED_PERMISSION_READ_EXTERNAL_STORAGE)
        const val MIMETYPE_IMAGES = "image/*"
        const val PICK_IMAGE_CODE = 1
    }
}