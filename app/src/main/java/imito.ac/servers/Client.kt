package imito.ac.servers

import java.net.*

class Client(
    address: InetAddress,
    port: Int,
) {
    private val client = Socket(address, port)

    fun send(
        message: String,
    ) {
        val outputStream = client.outputStream
        val byteArray = message.toByteArray()
        outputStream.write(byteArray)
        client.close()
    }
}
