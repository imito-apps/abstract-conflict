package imito.ac.servers

import imito.core.*
import imito.core.entities.*
import java.net.*

open class GameParticipant(
    gameName: String,
) {
    companion object {
        const val PadLength = 3
    }

    protected class Message {
        companion object {
            const val Separator = '\u0002'

            const val Join = 'J'
            const val Decision = 'D'
            const val Leave = 'L'
            const val AbortByClient = 'C'

            const val Others = 'O'
            const val Start = 'S'
            const val GameState = 'G'
            const val Abort = 'A'
            const val ResponseToJoin = 'R'
        }
    }

    class Response {
        enum class Join(val value: Int) {
            Ok(1),
            TooManyPlayers(2),
            VersionTooNew(3),
            VersionTooOld(4),
        }

        companion object {
            val JoinValues = Join.values()
        }
    }

    protected val server = Server()
    protected val userSerializer = UserSerializer(gameName)

    protected fun serializePlayer(player: NetUser?): String {
        return userSerializer.serialize(player!!)
            .replace(Message.Separator, ' ')
    }

    protected fun deserializePlayer(text: String, address: InetAddress): NetUser? {
        return userSerializer.deserialize(text, address)
    }
}
