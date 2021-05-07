package imito.ac.servers

import android.util.*
import imito.core.errors.*
import java.io.*
import java.net.*
import java.util.*
import kotlin.concurrent.*

class Server : Closeable {
    companion object {
        private const val TAG = "ServerBase"
    }

    private val port: Int? get() = serverSocket?.localPort
    private var serverSocket: ServerSocket? = null

    fun start(
        onStarted: (address: InetAddress, port: Int) -> Unit,
        onReceive: (request: String, address: InetAddress) -> Unit,
    ) {
        // Initialize a server socket on the next available port.
        serverSocket = ServerSocket(0)
        thread {
            ExceptionCatcher.tryRun<SocketException>({
                while (true) {
                    val client = serverSocket!!.accept()
                    println("Client connected : ${client.inetAddress.hostAddress}")
                    val scanner = Scanner(client.inputStream)
                    val stringBuilder = StringBuilder()
                    while (scanner.hasNextLine()) {
                        stringBuilder.append(scanner.nextLine())
                    }
                    val text = stringBuilder.toString()
                    if (text.isNotEmpty()) onReceive(text, client.inetAddress)
                }
            }, allowedMessages, "Server.start")
        }
        onStarted(serverSocket!!.inetAddress, port!!)
    }

    private val allowedMessages = arrayOf(
        "Socket closed",
        "Socket is closed",
    )

    override fun close() {
        serverSocket?.close()
    }
}
