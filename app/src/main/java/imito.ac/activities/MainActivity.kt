package imito.ac.activities

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.*
import imito.core.*
import imito.core.errors.*
import imito.core.views.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.activities.adapters.user.*
import imito.ac.activities.dialogs.*
import imito.ac.activities.waiting.room.*
import imito.ac.game.*
import java.util.*

class MainActivity : PortraitActivity(R.layout.activity_main) {
    private lateinit var game: Game
    private lateinit var nameEditText: EditText
    private lateinit var nameEditButton: ImageButton
    private lateinit var nameSaveButton: ImageButton
    private lateinit var nameShowButton: Button

    private val playerAdapter = NetPlayerAdapter {
        joinHost(it.id)
    }

    override fun createSelf(savedInstanceState: Bundle?) {
        game = Game(this,
            {
                runOnUiThread { playerAdapter.add(it) }
            }, {
                runOnUiThread { playerAdapter.remove(it) }
            })
        GameProvider.Instance = game
        showStartInfo()
        setNameWidgets()
        setGameRules()
        setHosts()
        setStartButtons()
    }

    override fun onStart() {
        super.onStart()
        game.onAppStart()
        game.end()
    }

    override fun onStop() {
        game.onAppStop()
        playerAdapter.removeAll()
        super.onStop()
    }

    override fun onDestroy() {
        game.onAppDestroy()
        val exceptions = ExceptionCatcher.actualExceptions
        if (exceptions.isNotEmpty()) {
            throw Exception(exceptions.toString())
        }
        super.onDestroy()
    }

    private fun showStartInfo() {
        if (game.getValueOrFalseAndSetTrue(GameConfig.Keys.GuideMainMenu)) return

        val format = resources.getString(R.string.guide_main_menu)
        val appName = resources.getString(R.string.label_app_name)
        val message = String.format(format, appName)
        OkDialog.show(this, message)
    }

    private fun tryJoinTheOnlyHost() {
        val hosts = game.discoveredHosts
        if (hosts.size != 1) return

        val host = hosts.values.first()
        joinHost(host.id)
    }

    private fun joinHost(hostId: UUID) {
        if (!saveName()) return

        val intent = Intent(this, WaitingRoomClientActivity::class.java)
        game.setDiscoveredHost(hostId)
        startActivity(intent)
    }

    private fun openGameActivity() {
        game.startSummoning { }
        game.startSelf()
        val intent = Intent(this, GameMainActivity::class.java)
        startActivity(intent)
    }

    private fun setGameRules() {
        findViewById<Button>(R.id.button_game_rules)
            .setOnClickListener {
                GameRulesDialog(this)
            }
    }

    private fun setHosts() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_hosts)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = playerAdapter
    }

    private fun setStartButtons() {
        findViewById<TextView>(R.id.text_join_game)
            .setOnClickListener {
                tryJoinTheOnlyHost()
            }

        findViewById<Button>(R.id.button_start_game)
            .setOnClickListener {
                startGame()
            }
    }

    private fun startGame() {
        if (!saveName()) return

        val intent = Intent(this, WaitingRoomHostActivity::class.java)
        startActivity(intent)
    }

    private fun saveName(): Boolean {
        val name = nameEditText.text
            .toString()
            .trim()
        if (game.saveName(name)) {
            setNameText()
            disableNameEdit()
            return true
        }
        val view = findViewById<LinearLayout>(R.id.layout_main)
        Snackbar.make(view, R.string.info_enter_name, 5000)
            .show()
        enableNameEdit()
        return false
    }

    private fun setNameText() {
        val name = game.playerName
        nameEditText.setText(name)
        nameShowButton.text = name
    }

    private fun disableNameEdit() {
        Keyboard.hide(this)
        setNameEdit(View.VISIBLE, View.GONE)
    }

    private fun enableNameEdit() {
        setNameEdit(View.GONE, View.VISIBLE)
        nameEditText.requestFocus()
        nameEditText.setSelection(nameEditText.text.length)
        Keyboard.show(this)
    }

    private fun setNameEdit(
        editVisibility: Int,
        saveVisibility: Int,
    ) {
        nameShowButton.visibility = editVisibility
        nameEditButton.visibility = editVisibility
        nameEditText.visibility = saveVisibility
        nameSaveButton.visibility = saveVisibility
    }

    private fun setNameWidgets() {
        nameSaveButton = findViewById(R.id.button_save_name)
        nameSaveButton.setOnClickListener {
            //if (BuildConfig.DEBUG) openGameActivity()

            saveName()
        }
        nameEditButton = findViewById(R.id.button_edit_name)
        nameEditButton.setOnClickListener { enableNameEdit() }

        nameShowButton = findViewById(R.id.button_show_name)
        nameEditText = findViewById(R.id.edit_text_name)
        setNameText()
        if (game.playerName.isBlank()) enableNameEdit()
        else disableNameEdit()
    }
}
