package com.alohatechnology.nsdemulator.tcp

import java.util.*

/**
 * Model info to store client data
 *
 * Only has a single property clientId
 * Instead of storing a list of clientId as long
 * it is stored in this wrapper
 * as this wrapper implements a toString() method
 * which will be used to display the Client name wherever required
 * e.g. in client drop down list
 */
data class Client(val clientId: Long) {

    companion object {
        fun ofString(str: String): Client? {
            val clientRegex = "Client (.*)".toRegex()
            if (!clientRegex.matches(str)) return null
            val result = clientRegex.find(str)
            val clientIdStr = result?.groups?.get(1)?.value ?: return null
            return try {
                Client(clientIdStr.toLong())
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun toString(): String {
        return "Client $clientId"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        return when (other) {
            is String -> other == toString()
            is Client -> other.clientId == clientId
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(toString())
    }
}