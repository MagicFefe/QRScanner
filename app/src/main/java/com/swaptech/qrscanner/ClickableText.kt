package com.swaptech.qrscanner

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class ClickableText(private val clickListener: OnTextClick): ClickableSpan() {

    override fun onClick(widget: View) {
        clickListener.onTextClicked()
    }
    interface OnTextClick {
        fun onTextClicked()
    }
}