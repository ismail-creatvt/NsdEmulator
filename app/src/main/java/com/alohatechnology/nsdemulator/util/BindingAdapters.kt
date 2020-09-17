package com.alohatechnology.nsdemulator.util

import android.content.res.ColorStateList
import android.os.Handler
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.ui.server.ClientAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("dataSet")
fun setDropdownData(textView: AutoCompleteTextView, arrayList: ArrayList<Client>?) {
    if (arrayList == null) return
    val adapter = ClientAdapter(textView.context, android.R.layout.simple_spinner_item, arrayList)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    textView.setAdapter(adapter)
}

@InverseBindingAdapter(attribute = "selection")
fun getSelection(textView: AutoCompleteTextView): Int {
    return textView.getSelection()
}

@BindingAdapter("selection")
fun setSelection(textView: AutoCompleteTextView, selection: Int) {
    textView.listSelection = selection
}

@BindingAdapter("selectionAttrChanged")
fun setSelectionChangeListener(textView: AutoCompleteTextView, inverseBindingAdapter: InverseBindingListener) {
    textView.setOnItemClickListener { parent, view, position, id ->
        inverseBindingAdapter.onChange()
    }
    textView.addTextChangedListener {
        inverseBindingAdapter.onChange()
    }
}

@BindingAdapter("endIconClick")
fun setEndIconClickListener(textInputLayout: TextInputLayout, endIconClickListener: View.OnClickListener) {
    textInputLayout.setEndIconOnClickListener(endIconClickListener)
}

@BindingAdapter("autoBottomScrollView")
fun autoScrollBottom(textView: TextView, scrollView: ScrollView) {
    textView.addTextChangedListener {
        Handler().postDelayed({
            scrollView.fullScroll(View.FOCUS_DOWN)
        }, 100)
    }
}

@BindingAdapter("backgroundTintList")
fun setBackgroundTint(button: MaterialButton, backgroundTint: ColorStateList) {
    button.backgroundTintList = backgroundTint
}