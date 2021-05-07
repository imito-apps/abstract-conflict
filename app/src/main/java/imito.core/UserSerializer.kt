package imito.core

import imito.core.entities.*
import java.lang.Exception
import java.net.InetAddress
import java.util.*

class UserSerializer(
    private val gameName: String,
) {
    companion object {
        private const val Separator = '\u0001'
    }

    private val idEndIndex: Int get() = gameName.length + Const.Guid.Length

    fun serialize(user: NetUser): String {
        return "$gameName${user.id}${user.port}$Separator${user.name}"
    }

    fun deserialize(text: String, address: InetAddress): NetUser? {
        if (idEndIndex >= text.length) return null

        val id = deserializeId(text)
        val rest = text.subSequence(idEndIndex, text.length).split(Separator)
        if (rest.size != 2) return null

        return try {
            val port = rest[0].toInt()
            val name = rest[1]
            NetUser(id, address, name, port)
        } catch (exc: Exception) {
            null
        }
    }

    fun deserializeId(text: String): UUID {
        val id = text.subSequence(gameName.length, idEndIndex).toString()
        return UUID.fromString(id)
    }
}
