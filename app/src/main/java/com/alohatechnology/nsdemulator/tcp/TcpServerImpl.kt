package com.alohatechnology.nsdemulator.tcp

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.alohatechnology.nsdemulator.tcp.TcpServer.ClientListener
import com.alohatechnology.nsdemulator.tcp.TcpServer.ResponseListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class TcpServerImpl(private val port: Int = 5555, lifecycleOwner: LifecycleOwner) : TcpServer, LifecycleObserver {
    private var serverJob: Job? = null
    private var clientListener: ClientListener? = null
    private var socket: ServerSocket? = null

    override var isRunning = false

    private var CLASS_LOG = TcpServerImpl::class.java.simpleName

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private val clients = HashMap<Long, Socket>()

    private var responseListener: ResponseListener? = null

    override fun setResponseListener(responseListener: ResponseListener?) {
        this.responseListener = responseListener
    }

    override fun setClientListener(clientListener: ClientListener?) {
        this.clientListener = clientListener
    }

    override fun start() {
        serverJob = CoroutineScope(IO).launch(IO) {
            try {
                socket = ServerSocket(port)

                isRunning = true

                while (isRunning) {
                    Log.d(CLASS_LOG, "Accepting Connections...")
                    val client = socket?.accept() ?: break
                    val clientId = System.currentTimeMillis()
                    clients[clientId] = client
                    Log.d(CLASS_LOG, "Connected to Client ${clientId}")
                    clientListener?.onAdded(clientId)

                    serveClient(clientId)
                }
            } catch (e: IOException) {
                clients.clear()
                Log.d(CLASS_LOG, "Exception while starting server : ${e.message}")
            }
        }
    }

    private fun serveClient(clientId: Long) {
        CoroutineScope(IO).launch(IO) {
            val client = getClient(clientId) ?: return@launch
            try {
                getReader(client).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        responseListener?.onResponse(TcpServer.Response(clientId, line!!))
                    }
                    Log.d(CLASS_LOG, "Client disconnected bacause of null response")
                    removeClient(clientId)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                removeClient(clientId)
                Log.d(CLASS_LOG, "Client disconnected bacause of IO Exception : ${e.message}")
            }
        }
    }

    private fun removeClient(clientId: Long) {
        clients.remove(clientId)
        clientListener?.onRemoved(clientId)
    }

    override fun disconnectClient(clientId: Long) {
        val client = getClient(clientId) ?: return
        if (!client.isClosed) {
            try {
                client.close()
            } catch (e: IOException) {
                Log.d(CLASS_LOG, "Exception while disconnecting client : ${e.message}")
            }
        }
    }

    private fun getClient(clientId: Long): Socket? {
        return if (!clients.containsKey(clientId)) {
            null
        } else clients[clientId]
    }

    @Throws(IOException::class)
    private fun getReader(client: Socket): BufferedReader {
        return BufferedReader(InputStreamReader(client.getInputStream()))
    }

    @Throws(IOException::class)
    private fun getWriter(client: Socket): PrintWriter {
        return PrintWriter(OutputStreamWriter(client.getOutputStream()))
    }

    override fun sendMessage(clientId: Long, message: String?) {
        if (message == null) return
        CoroutineScope(IO).launch(IO) {
            val client = getClient(clientId) ?: return@launch
            val writer = getWriter(client)
            try {
                writer.write("$message${0x0D}${0x0A}")
                writer.flush()
            } catch (e: IOException) {
                writer.close()
                removeClient(clientId)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun stop() {
        isRunning = false

        for (clientId in clients.keys) {
            disconnectClient(clientId)
        }

        if (serverJob?.isActive == true) {
            serverJob?.cancel()
        }
        clients.clear()
        if (socket != null && socket?.isClosed != true) {
            try {
                socket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}