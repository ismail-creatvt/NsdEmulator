package com.alohatechnology.nsdemulator.ui.server

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alohatechnology.nsdemulator.tcp.Client
import java.util.*

class ServerViewModel : ViewModel() {
    val clients = ArrayList<Client>()
    val message = MutableLiveData<String>()
    val consolePrompt = ">"
    val consoleText = MutableLiveData(consolePrompt)

    val isStarted = MutableLiveData<Boolean>()
    val isProgressVisible = MutableLiveData<Boolean>()

    val selection = MutableLiveData(-1)
    val selectionText = MutableLiveData("")

    private val _isSendEnabled = MutableLiveData<Boolean>()
    val isSendEnabled: LiveData<Boolean>
        get() = _isSendEnabled

    init {
        message.observeForever {
            refreshSendEnabled(selection.value, it)
        }

        selection.observeForever {
            refreshSendEnabled(it, message.value)
        }
    }

    private fun refreshSendEnabled(selection: Int?, message: String?) {
        _isSendEnabled.postValue(selection != -1 && !TextUtils.isEmpty(message))
    }
}