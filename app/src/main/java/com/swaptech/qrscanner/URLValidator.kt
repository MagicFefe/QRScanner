package com.swaptech.qrscanner

object URLValidator {
    fun validate(url: String): Boolean {
        val protocolFamily = listOf("http://", "https://")

        return url.contains(protocolFamily[0]) || url.contains(protocolFamily[1])

    }
}