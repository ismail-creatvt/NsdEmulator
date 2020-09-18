package com.alohatechnology.nsdemulator.nsd

import android.content.Context
import android.content.Context.NSD_SERVICE
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import androidx.lifecycle.LifecycleObserver
import com.alohatechnology.nsdemulator.util.appName
import com.alohatechnology.nsdemulator.util.versionName

class NsdHelper(private val context: Context) : LifecycleObserver {

    companion object {
        const val SERVICE_TYPE = "_rio._tcp."
        const val NETWORK_SERVICE_PORT = 9999
    }

    private val nsdManager: NsdManager = context.getSystemService(NSD_SERVICE) as NsdManager

    fun registerService(registrationListener: NsdManager.RegistrationListener) {
        try {
            nsdManager.registerService(getNsdServiceInfo(), NsdManager.PROTOCOL_DNS_SD, registrationListener)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    fun unregisterService(registrationListener: NsdManager.RegistrationListener) {
        try {
            nsdManager.unregisterService(registrationListener)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun getNsdServiceInfo() = NsdServiceInfo().apply {
        // Service information
        serviceName = "MCAService"
        port = NETWORK_SERVICE_PORT
        serviceType = SERVICE_TYPE

        //Txt Records attributes
        setAttribute("localName", "Ismail's MCA Emulator")
        setAttribute("App Name", context.appName)
        setAttribute("App Ver.", context.versionName)
        setAttribute("OS Name", "Android")
        setAttribute("OS Ver.", Build.VERSION.RELEASE)
    }

}