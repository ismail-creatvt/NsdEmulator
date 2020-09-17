package com.alohatechnology.nsdemulator.ui.templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.alohatechnology.nsdemulator.R
import com.alohatechnology.nsdemulator.databinding.ActivityResponseTemplateBinding
import com.alohatechnology.nsdemulator.util.responseTemplates
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ResponseTemplateBottomSheetFragment : BottomSheetDialogFragment(), ResponseTemplateAdapter.ResponseTemplateClickListener {

    private var binding: ActivityResponseTemplateBinding? = null

    companion object {
        val CLASS_NAME = ResponseTemplateBottomSheetFragment::class.java.simpleName
    }

    var listener: ResponseTemplateAdapter.ResponseTemplateClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_response_template, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.lifecycleOwner = this
        binding?.adapter = ResponseTemplateAdapter(requireContext().responseTemplates, this)
    }

    override fun onResponseTemplateClick(template: ResponseTemplate) {
        dismiss()
        listener?.onResponseTemplateClick(template)
    }
}