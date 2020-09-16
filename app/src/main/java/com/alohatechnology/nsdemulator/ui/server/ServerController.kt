package com.alohatechnology.nsdemulator.ui.server

interface ServerController {

    fun onSendClick()

    fun onDisconnectClick()

    fun onToggleClick()

    fun onMessageTemplateIconClick()

    fun onClearSelectionClick()

    fun onTemplateResult(responseText: String)

    fun onUnregisterConfirm()

}