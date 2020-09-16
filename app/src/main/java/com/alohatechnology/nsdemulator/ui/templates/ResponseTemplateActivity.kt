package com.alohatechnology.nsdemulator.ui.templates

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alohatechnology.nsdemulator.R
import com.alohatechnology.nsdemulator.databinding.ActivityResponseTemplateBinding
import com.alohatechnology.nsdemulator.util.responseTemplates

class ResponseTemplateActivity : AppCompatActivity(), ResponseTemplateAdapter.ResponseTemplateClickListener {

    companion object {
        const val RESPONSE_STRING: String = "RESPONSE_STRING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityResponseTemplateBinding>(
                this, R.layout.activity_response_template
        )
        binding.lifecycleOwner = this

        binding.adapter = ResponseTemplateAdapter(responseTemplates, this)
    }

    override fun onResponseTemplateClick(template: ResponseTemplate) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(RESPONSE_STRING, template.response)
        })
        finish()
    }
}