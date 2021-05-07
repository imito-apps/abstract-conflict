package imito.ac.activities.waiting.room

import android.os.Bundle
import android.widget.Button
import imito.ac.*
import imito.ac.activities.dialogs.*

class WaitingRoomHostActivity : WaitingRoomActivity(R.layout.activity_waiting_room_host) {
    private lateinit var startButton: Button

    override fun createSelf(savedInstanceState: Bundle?) {
        super.createSelf(savedInstanceState)
        startButton = findViewById(R.id.button_start_game)
        startButton.isEnabled = false
        startButton.setOnClickListener {
            startGame()
            game.startSelf()
        }
        game.startSummoning { players ->
            runOnUiThread {
                changeAll(players)
                startButton.isEnabled = players.isNotEmpty()
            }
        }
        findViewById<Button>(R.id.button_abort_server)
            .setOnClickListener {
                if (playerAdapter.itemCount == 0) {
                    abortServer()
                    return@setOnClickListener
                }
                YesNoDialog.show(this, R.string.dialog_abort_server, ::abortServer)
            }
    }

    override fun onBackPressed() {}

    private fun abortServer() {
        game.abortSummoning()
        finish()
    }
}
