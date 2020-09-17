package com.alohatechnology.nsdemulator.util

import android.widget.AutoCompleteTextView
import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.ui.server.ClientsAdapter

fun AutoCompleteTextView.getSelection(): Int {
    if (adapter !is ClientsAdapter) return -1
    val arrayAdapter = adapter as ClientsAdapter
    return arrayAdapter.getPosition(Client.ofString(text.toString()))
}