package com.alohatechnology.nsdemulator.ui.templates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alohatechnology.nsdemulator.R
import com.alohatechnology.nsdemulator.databinding.ResponseTemplateItemBinding

class ResponseTemplateAdapter(
        private val responseTemplates: ArrayList<ResponseTemplate>,
        private val listener: ResponseTemplateClickListener) : RecyclerView.Adapter<ResponseTemplateAdapter.ResponseTemplateViewHolder>() {

    class ResponseTemplateViewHolder(val itemBinding: ResponseTemplateItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ResponseTemplateViewHolder(
            DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.response_template_item,
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: ResponseTemplateViewHolder, position: Int) {
        holder.itemBinding.template = responseTemplates[position]
        holder.itemBinding.listener = listener
    }

    override fun getItemCount() = responseTemplates.size

    interface ResponseTemplateClickListener {
        fun onResponseTemplateClick(template: ResponseTemplate)
    }
}