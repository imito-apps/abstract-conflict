package imito.ac.activities.waiting.room

import android.os.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.servers.*

class WaitingRoomClientActivity : WaitingRoomActivity(R.layout.activity_waiting_room_client) {

    override fun createSelf(savedInstanceState: Bundle?) {
        super.createSelf(savedInstanceState)

        game.abort = ::leaveWithoutNotify
        game.joinHost(::changeAll, ::startGame) {
            val okId = -1
            val messageId = when (it) {
                GameParticipant.Response.Join.TooManyPlayers ->
                    R.string.response_too_many_players
                GameParticipant.Response.Join.VersionTooNew ->
                    R.string.response_version_too_new
                GameParticipant.Response.Join.VersionTooOld ->
                    R.string.response_version_too_old
                else -> okId
            }
            if (messageId == okId) return@joinHost

            runOnUiThread {
                OkDialog.show(this, messageId) {
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        if (leaveOnDestroy) game.leaveHost()

        super.onDestroy()
    }
}
