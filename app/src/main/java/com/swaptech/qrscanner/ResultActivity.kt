package com.swaptech.qrscanner

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.swaptech.qrscanner.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {

    private var result: String? = null
    private var _binding: ActivityResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.resultTv.movementMethod = LinkMovementMethod.getInstance()

        supportActionBar?.hide()

        result = intent.getStringExtra(EXTRA_DATA_KEY)

        result?.let { res ->

            val validatorResult = URLValidator.validate(res)

            if(validatorResult.first) {

                val url = validatorResult.second

                val textSpanner = TextSpanner(res, url ?: mutableListOf())

                val clickListener  = { text: String ->
                    var clickableText = text
                    if(!(clickableText.contains("https://") || clickableText.contains("http://"))) {
                        clickableText = "http://$clickableText"
                    }

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(clickableText))

                    if(intent.resolveActivity(this@ResultActivity.packageManager) != null) {
                        startActivity(intent)
                    }
                }

                binding.resultCardTv.apply {
                    this.text = textSpanner.buildFormattedText(clickListener)
                    this.movementMethod = LinkMovementMethod.getInstance()
                }

            } else {
                binding.resultCardTv.text = res
            }

        }
        
        //TODO(REMOVE THIS)
        /*
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.backdrop_history))

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        findViewById<ConstraintLayout>(R.id.backdrop_header).setBackgroundResource(0)

                        findViewById<ConstraintLayout>(R.id.backdrop_header).setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.second_color_yellow))

                        binding.backdropHistory.imageButton.animate().rotation(180f).setDuration(110).start()

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                        binding.backdropHistory.imageButton.animate().rotation(0f).setDuration(110).start()

                    }
                    else -> {

                        findViewById<ConstraintLayout>(R.id.backdrop_header).setBackgroundResource(R.drawable.backdrop)

                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

         */

        /*
        binding.backdropHistory.imageButton.setOnClickListener {
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

        }

         */
    }
}