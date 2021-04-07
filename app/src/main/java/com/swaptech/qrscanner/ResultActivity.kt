package com.swaptech.qrscanner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import com.swaptech.qrscanner.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private var result: String? = null
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        result = intent.getStringExtra(EXTRA_DATA_KEY)

        result?.let { res ->
            if(URLValidator.validate(res)) {
                binding.resultTv.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.resultTv.text = underscoreString(res)
                binding.resultTv.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res))
                    if(intent.resolveActivity(this.packageManager) != null) {
                        startActivity(intent)
                    }
                }
            } else {
                binding.resultTv.text = res
            }
        }




    }

    private fun underscoreString(text: String): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
        return spannableString
    }
}