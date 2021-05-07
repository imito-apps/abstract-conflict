package imito.ac.activities.waiting.room

import android.content.*
import android.os.*
import androidx.recyclerview.widget.*
import imito.core.entities.*
import imito.core.views.*
import imito.ac.*
import imito.ac.activities.*
import imito.ac.activities.adapters.user.*
import imito.ac.game.*

open class WaitingRoomActivity(
    layoutId: Int,
) : PortraitActivity(layoutId) {
    protected val game = GameProvider.Instance
    protected val playerAdapter = NetPlayerAdapter { }
    protected var leaveOnDestroy = true

    override fun createSelf(savedInstanceState: Bundle?) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_waiting_player)
        recyclerView.adapter = playerAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    protected fun changeAll(players: List<NetUser>) {
        runOnUiThread {
            playerAdapter.removeAll()
            playerAdapter.add(players)
        }
    }

    protected fun startGame() {
        leaveWithoutNotify()
        val intent = Intent(game.activity, GameMainActivity::class.java)
        startActivity(intent)
    }

    protected fun leaveWithoutNotify() {
        leaveOnDestroy = false
        finish()
    }
}
