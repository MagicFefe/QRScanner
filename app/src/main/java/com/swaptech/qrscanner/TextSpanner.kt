package com.swaptech.qrscanner

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View

class TextSpanner(private var fullstring: String, private val urls: MutableList<String>) {

    fun buildFormattedText(clickListener: (text: String) -> Unit = {}): SpannableString {
        val result = SpannableString(fullstring)
        if(urls.isNotEmpty()) {
            urls.forEach { clickable ->
                result.indexOf(clickable).takeIf { it >= 0 }?.let {

                        result.setSpan(object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    clickListener(clickable)
                                }
                            }, it, it + clickable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

            }

        }

        return result
    }

}
