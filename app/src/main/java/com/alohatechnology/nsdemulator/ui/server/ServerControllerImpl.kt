package com.alohatechnology.nsdemulator.ui.server

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.alohatechnology.nsdemulator.nsd.NsdHelper
import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.tcp.TcpServer
import com.alohatechnology.nsdemulator.tcp.TcpServerImpl
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplate

class ServerControllerImpl(
        private val nsdManager: NsdHelper,
        private val view: ServerView,
        private val viewModel: ServerViewModel,
        lifecycleOwner: LifecycleOwner
) : ServerController, NsdManager.RegistrationListener, TcpServer.ClientListener, TcpServer.ResponseListener {

    private val server = TcpServerImpl(NsdHelper.NETWORK_SERVICE_PORT, lifecycleOwner).apply {
        setClientListener(this@ServerControllerImpl)
        setResponseListener(this@ServerControllerImpl)
    }

    init {
        if (viewModel.isStarted.value == true) {
            server.start()
        }

        viewModel.isStarted.observeForever {
            if (it) {
                server.start()
            } else {
                server.stop()
            }
        }
    }


    /// Controller interface implementation:-

    /**
     * Gets the message entered in the field
     * and sends it to the client selected in the spinner
     */
    override fun onSendClick() {
        val message = viewModel.message.value

        // if the message is empty then there is nothing to send, so don't do anything
        if (TextUtils.isEmpty(message)) return

        // if the client is null then there is no one to send to, so don't do anything
        val client = getSelectedClient() ?: return

        writeToConsole("Sent to $client : $message")

        // send the message to the selected client id
        server.sendMessage(client.clientId, message)

        // clear the message field
        viewModel.message.postValue("")
    }

    private fun writeToConsole(message: String) {
        val consoleText = viewModel.consoleText.value
        viewModel.consoleText.postValue(String.format("%s %s\n%s", consoleText, message, viewModel.consolePrompt))
    }

    /**
     * Disconnects the client selected in the spinner
     */
    override fun onDisconnectClick() {
        // if no client has been selected then there is nothing to do
        val client = getSelectedClient() ?: return
        server.disconnectClient(client.clientId)
    }


    override fun onMessageTemplateIconClick() {
        view.showMessageTemplatesList()
    }

    override fun onClearSelectionClick() {
        viewModel.selectionText.postValue("")
        viewModel.message.postValue("")
    }

    override fun onTemplateResult(responseText: String) {
        viewModel.message.postValue(responseText)
    }

    override fun onResponseTemplateClick(template: ResponseTemplate) {
        viewModel.message.postValue(template.response)
    }

    /**
     * Starts or stops the service depending on the current state
     */
    override fun onToggleClick() {
        if (viewModel.isStarted.value == true) {
            view.showUnregisterConfirmation()
        } else {
            viewModel.isProgressVisible.postValue(true)
            nsdManager.registerService(this)
        }
    }

    override fun onUnregisterConfirm() {
        viewModel.isProgressVisible.postValue(true)
        nsdManager.unregisterService(this)
    }

    /// REGISTRATION CALLBACKS:-
    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        view.showToast("Failed to start service")
        viewModel.isProgressVisible.postValue(false)
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        view.showToast("Unable to stop service! Try again")
        viewModel.isProgressVisible.postValue(false)
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
        viewModel.isStarted.postValue(false)
        viewModel.isProgressVisible.postValue(false)
    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
        viewModel.isStarted.postValue(true)
        viewModel.isProgressVisible.postValue(false)
    }

    /// Response callback:-
    override fun onResponse(response: TcpServer.Response?) {
        if (response == null) return
        writeToConsole("Client ${response.clientId} : ${response.message}")
        val responseTemplates = view.getResponseTemplates()
        val responseIndex = responseTemplates.indexOfFirst {
            it.request == response.message
        }
        if (responseIndex < 0) {
            server.sendMessage(response.clientId, response.message)
        } else {
            server.sendMessage(response.clientId, responseTemplates[responseIndex].response)
        }
    }

    /// Client Listener callbacks:-

    override fun onAdded(clientId: Long) {
        val client = Client(clientId)
        viewModel.selectionText.postValue("")
        view.addClient(client)
        writeToConsole("Connected to $client")
    }

    override fun onRemoved(clientId: Long) {
        val client = Client(clientId)
        viewModel.selectionText.postValue("")
        view.removeClient(client)
        writeToConsole("Disconnected from $client")
    }

    private fun getSelectedClient(): Client? {
        val selection = viewModel.selection.value ?: return null
        if (selection < 0) return null
        return viewModel.clients[selection]
    }
}