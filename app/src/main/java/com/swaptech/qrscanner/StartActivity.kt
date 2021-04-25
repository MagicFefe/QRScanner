package com.swaptech.qrscanner

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import com.swaptech.qrscanner.databinding.ActivityStartBinding


class StartActivity : AppCompatActivity() {

    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
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
            val options = ActivityOptions.makeSceneTransitionAnimation(this@StartActivity, binding.scanQrBtn, ViewCompat.getTransitionName(binding.scanQrBtn))
            startActivity(Intent(this@StartActivity, MainActivity::class.java), options.toBundle())
        }


        binding.createQrBtn.setOnClickListener { btn ->
            btn.startAnimation(anim)
        }
    }

    override fun onRestart() {
        super.onRestart()

        binding.activityStart.transitionToState(R.id.end)
    }
}