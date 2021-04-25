package com.swaptech.qrscanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.swaptech.qrscanner.databinding.FragmentActionsBottomSheetDialogBinding
import java.security.Provider

class ActionsBottomSheetDialog private  constructor(): RoundedBottomSheetDialogFragment() {
    private var _binding: FragmentActionsBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    companion object {
        var clipboardId = 0
        fun newInstance(clipboardText: String) = ActionsBottomSheetDialog().apply {
            this.clipboardText = clipboardText
        }
    }

    private lateinit var clipboardText: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentActionsBottomSheetDialogBinding.inflate(inflater)
        val anim = AnimationUtils.loadAnimation(this.context, R.anim.scale_anim)
        val interpolator = CustomBounceInterpolator(0.2, 10.toDouble())
        anim.interpolator = interpolator


        binding.copyBtn.setOnClickListener {
            it.startAnimation(anim)
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("$clipboardId", clipboardText)
            clipboard.setPrimaryClip(clipData)
            Toast.makeText(requireContext(), "Text copied!", Toast.LENGTH_SHORT).show()
        }

        binding.shareBtn.setOnClickListener {
            it.startAnimation(anim)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, clipboardText)
            }

            startActivity(Intent.createChooser(intent, "Share"))
        }

        binding.closeBottomSheetBtn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        return binding.root
    }

}