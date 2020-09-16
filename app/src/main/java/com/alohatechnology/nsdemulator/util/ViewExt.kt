package com.alohatechnology.nsdemulator.util

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.alohatechnology.nsdemulator.tcp.Client

fun AutoCompleteTextView.getSelection(): Int {
    if (adapter !is ArrayAdapter<*>) return -1
    val arrayAdapter = adapter as ArrayAdapter<Client>
    return arrayAdapter.getPosition(Client.ofString(text.toString()))
}