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

        binding.scanQrBtn.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).setInterpolator(BounceInterpolator()).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(BounceInterpolator()).start()

                    val options = ActivityOptions.makeSceneTransitionAnimation(this@StartActivity, binding.scanQrBtn, ViewCompat.getTransitionName(binding.scanQrBtn))

                    startActivity(Intent(this@StartActivity, MainActivity::class.java), options.toBundle())
                }
            }
            true
        }

        binding.createQrBtn.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                v.animate().scaleX(0.8f).scaleY(0.8f).setInterpolator(BounceInterpolator()).start()
            }
            if(event.action == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1f).scaleY(1f).setInterpolator(BounceInterpolator()).start()

            }


            true
        }
    }

    override fun onStop() {
        super.onStop()

            binding.activityStart.transitionToState(R.id.end)
    }
}