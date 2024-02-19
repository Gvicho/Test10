package com.example.test10.presenter.screen.home.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.test10.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: ModalBottomSheetBinding? = null
    private val binding get() = _binding!!
    interface BottomSheetListener {
        fun onOptionSelected(option: String)
    }
    private var listener: BottomSheetListener? = null
    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ModalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraBtn.setOnClickListener {
            listener?.onOptionSelected("TAKE_PICTURE")
            dismiss()
        }
        binding.galeryBtn.setOnClickListener {
            listener?.onOptionSelected("CHOOSE_GALLERY")
            dismiss()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}