package com.swaptech.qrscanner

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.swaptech.qrscanner.databinding.ActivityCreateQRBinding
import java.lang.NullPointerException

class CreateQRActivity : AppCompatActivity() {

    private var _binding: ActivityCreateQRBinding? = null
    private val binding get() = _binding!!
    private val photoSaver = PhotoSaver(this)
    private var mBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateQRBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        
        var input: String?  = null

        val anim = AnimationUtils.loadAnimation(this, R.anim.scale_anim)
        val interpolator = CustomBounceInterpolator(0.2, 10.toDouble())
        anim.interpolator = interpolator


        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val qrDrawer = QRDrawer(manager)


        binding.editTextForQR.doAfterTextChanged { text ->
            input = text?.toString()

            qrDrawer.draw(input ?: "", binding.qrImageView)
        }

        binding.uploadBtn.setOnClickListener { btn ->
            try {

                btn.startAnimation(anim)
                binding.qrImageView.invalidate()
                val bitmapDrawable = (binding.qrImageView.drawable) as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap

                if(allPermissionsGranted()) {
                    photoSaver.save(bitmap)
                } else {
                    mBitmap = bitmap
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE)
                }

            } catch (e: NullPointerException) {
                Toast.makeText(this, "enter data", Toast.LENGTH_SHORT).show()
            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSION_CODE) {
            mBitmap?.let {
                photoSaver.save(it)
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        permissions.forEach { permission ->
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    override fun onStart() {
        super.onStart()
        binding.createQrBtn.animate().alpha(0f).setDuration(1650).start()
    }

    private companion object {
        const val REQUEST_PERMISSION_CODE = 1
        const val WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissions = arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION)
    }
}