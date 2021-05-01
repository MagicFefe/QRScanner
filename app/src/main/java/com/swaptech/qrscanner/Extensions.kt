package com.swaptech.qrscanner

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.util.Pair
import android.view.View
import androidx.core.view.ViewCompat
import java.nio.ByteBuffer

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}


fun Activity.startActivityWithTransition(startActivity: Activity, endActivity: Class<*>, sharedElement: View) {
    val options = ActivityOptions.makeSceneTransitionAnimation(startActivity, Pair( sharedElement, ViewCompat.getTransitionName(sharedElement)))
    startActivity(Intent(startActivity.baseContext, endActivity), options.toBundle())
}
