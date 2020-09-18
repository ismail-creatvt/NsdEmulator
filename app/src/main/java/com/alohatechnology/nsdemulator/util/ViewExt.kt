package com.alohatechnology.nsdemulator.util

import android.widget.AutoCompleteTextView
import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.ui.server.ClientAdapter

fun AutoCompleteTextView.getSelection(): Int {
    if (adapter !is ClientAdapter) return -1
    val arrayAdapter = adapter as ClientAdapter
    return arrayAdapter.getPosition(Client.ofString(text.toString()))
}