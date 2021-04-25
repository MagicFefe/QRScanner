package com.swaptech.qrscanner

import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.swaptech.qrscanner.databinding.ActivityCreateQRBinding

class CreateQRActivity : AppCompatActivity() {
    private var _binding: ActivityCreateQRBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateQRBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var input: String?  = null

        binding.editTextForQR.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                input = s?.toString()

                try {
                    val manager = getSystemService(WINDOW_SERVICE) as WindowManager

                    val display = manager.defaultDisplay

                    val point = Point()

                    display.getSize(point)
                    val width = point.x
                    val height = point.y

                    //val bitmap = createBitmap(widthQR, heightQR)


                    var dimen: Int = if (width < height) width else height
                    dimen = dimen * 3 / 4
                    val qrGenerator = QRGEncoder(input, null, QRGContents.Type.TEXT, dimen)
                    val bitmap = qrGenerator.encodeAsBitmap()
                    binding.qrImageView.setImageBitmap(bitmap)
                } catch (e: Exception) {

                }
            }
        })

        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        binding.createQrBtn.animate().alpha(0f).setDuration(1650).start()

    }


}