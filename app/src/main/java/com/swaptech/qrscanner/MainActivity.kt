package com.swaptech.qrscanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.common.util.concurrent.ListenableFuture
import com.swaptech.qrscanner.databinding.ActivityMainBinding

import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var qrCode = ""
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    var mIntent: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        if(allPermissionsGranted()) {
           startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(REQUIRED_PERMISSION), PERMISSION_REQUEST_CAMERA)
        }
        mIntent = Intent(this, ResultActivity::class.java)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CAMERA) {
            if(allPermissionsGranted()) {
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

        cameraProviderFuture.addListener( {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { preview ->
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val qrCodeAnalyzer = QRCodeImageAnalyzer(object : OnQRCodeAnalyzerResult {
                override fun onSuccess(result: String) {
                    qrCode = result

                    try {
                        mIntent.apply {
                            this?.putExtra(EXTRA_DATA_KEY, qrCode)
                        }
                        startActivity(mIntent)
                        //SharedViewModel.qrResult = qrCode
                    } catch (e: NullPointerException) {

                    }

                    mIntent = null
                }
            })

            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this), qrCodeAnalyzer)
            }

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)


            } catch (e: Exception) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }


    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED
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


    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 0
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}