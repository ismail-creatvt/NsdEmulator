package com.alohatechnology.nsdemulator.ui.server

import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplateAdapter

interface ServerController : ResponseTemplateAdapter.ResponseTemplateClickListener {

    fun onSendClick()

    fun onDisconnectClick()

    fun onToggleClick()

    fun onMessageTemplateIconClick()

    fun onClearSelectionClick()

    fun onTemplateResult(responseText: String)

    fun onUnregisterConfirm()

}