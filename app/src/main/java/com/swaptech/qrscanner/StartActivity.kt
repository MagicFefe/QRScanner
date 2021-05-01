package com.swaptech.qrscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.swaptech.qrscanner.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        supportActionBar?.hide()

        val anim = AnimationUtils.loadAnimation(this, R.anim.scale_anim)
        val interpolator = CustomBounceInterpolator(0.2, 10.toDouble())
        anim.interpolator = interpolator

        binding.scanQrBtn.setOnClickListener { btn ->
            btn.startAnimation(anim)
            startActivityWithTransition(this, MainActivity::class.java, binding.scanQrBtn)
        }


        binding.createQrBtn.setOnClickListener { btn ->
            btn.startAnimation(anim)
            startActivityWithTransition(this, CreateQRActivity::class.java, binding.createQrBtn)
        }
    }

    override fun onRestart() {
        super.onRestart()
        binding.activityStart.transitionToState(R.id.end)
    }

}