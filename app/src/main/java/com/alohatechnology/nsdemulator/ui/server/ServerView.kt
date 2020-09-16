package com.alohatechnology.nsdemulator.ui.server

import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplate

interface ServerView {

    fun getResponseTemplates(): ArrayList<ResponseTemplate>

    fun showToast(message: String)

    fun refreshClientsSpinner()

    fun showMessageTemplatesList()

    fun removeClient(client: Client)

    fun addClient(client: Client)

    fun showUnregisterConfirmation()

}