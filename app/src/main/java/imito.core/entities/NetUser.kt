package imito.core.entities

import java.net.InetAddress
import java.util.*

open class NetUser(
    id: UUID,
    val address: InetAddress,
    name: String,
    val port: Int,
) : UserBase(id, name) {

    fun withName(value: String): NetUser = NetUser(id, address, value, port)
    fun withAddress(address: InetAddress, port: Int): NetUser = NetUser(id, address, name, port)
}
