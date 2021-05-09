package imito.core.entities

import java.net.InetAddress
import java.util.*

class DiscoveredService(
    val id: UUID,
    val address: InetAddress?,
    val name: String,
    val port: Int,
) {
    fun toUser(): UserBase = UserBase(id, name)
}
