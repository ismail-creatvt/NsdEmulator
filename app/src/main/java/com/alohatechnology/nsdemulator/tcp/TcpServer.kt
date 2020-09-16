package com.alohatechnology.nsdemulator.tcp

interface TcpServer {
    /**
     * @param responseListener callback listener for client responses
     */
    fun setResponseListener(responseListener: ResponseListener?)

    /**
     * callback for client connect and disconnect events
     */
    fun setClientListener(clientListener: ClientListener?)

    /**
     * Start the server and listen to the clients
     */
    fun start()

    /**
     * Sends the message to client
     * @param clientId the id of client to whom to send the message
     * @param message the message to be send
     */
    fun sendMessage(clientId: Long, message: String?)

    /**
     * Stops the server
     */
    fun stop()

    /**
     * @return whether or not the server is currently active
     */
    val isRunning: Boolean

    fun disconnectClient(clientId: Long)

    class Response(var clientId: Long, var message: String)

    interface ResponseListener {
        fun onResponse(response: Response?)
    }

    interface ClientListener {
        fun onAdded(clientId: Long)
        fun onRemoved(clientId: Long)
    }
}