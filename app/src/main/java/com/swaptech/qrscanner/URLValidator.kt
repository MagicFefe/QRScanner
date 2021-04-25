package com.swaptech.qrscanner

import androidx.lifecycle.ViewModel

object URLValidator {

    fun validate(url: String): Pair<Boolean, MutableList<String>?>{

        val protocolFamily = listOf("http://", "https://")

        val regexp = Regex("\\w*(\\w[.]([A-Za-z0-9-._~:/?#\\[\\]@!\$&'()*+,;=А-ЯЁё])+([A-Za-z0-9А-ЯЁё]))")

        val validOutput = regexp.findAll(url).toMutableList()

        protocolFamily.forEach { keyword ->

            if(url.contains(keyword)) {

                return Pair(true, mutableListOf(url))

            } else if(validOutput.isNotEmpty()) {

                val result = mutableListOf<String>()

                validOutput.forEach { url ->
                    result.add(url.value)
                }

                return Pair(true, result)
            }
        }

        return Pair(false, null)
    }

}