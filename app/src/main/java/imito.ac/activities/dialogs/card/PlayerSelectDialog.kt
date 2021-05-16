package imito.ac.activities.dialogs.card

import android.view.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import imito.core.*
import imito.core.views.*
import imito.ac.*
import imito.ac.activities.*
import imito.ac.activities.adapters.user.*
import imito.ac.game.*
import imito.ac.models.*
import imito.ac.models.cards.*
import java.util.*

class PlayerSelectDialog(
    activity: AppCompatActivity? = null,
    card: CardModel? = null,
    onCancel: () -> Unit = Const.EmptyAction,
    private val game: Game? = null,
    private val players: PlayerModels? = null,
    private val onSelect: (UUID) -> Unit = {},
) : CardDialog(activity, card, onCancel, R.layout.dialog_select_player) {
    private lateinit var cardActivator: CardActivator

    private val adapter = PlayerAdapter {
        if (it.canBeTargeted) {
            dismiss()
            onSelect(it.id)
        } else
            SimpleToast.show(activity!!, R.string.info_player_defended)
    }

    init {
        if (players != null) adapter.replaceAll(players)
    }

    override fun changeSelf() {
        super.changeSelf()

        val recyclerView = findRecyclerView(R.id.recycler_player)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val groupLayout = findLinearLayout(R.id.layout_group)
        if (!game!!.isPlayerTheCurrentOne) groupLayout.visibility = View.INVISIBLE

        cardActivator = CardActivator(card!!, game)
        cardActivator.watchState(activity!!) {
            groupLayout.visibility = View.VISIBLE
        }
        val doNoEffectButton = findButton(R.id.button_do_no_effect)
        if (players!!.canAnyBeTargeted) {
            doNoEffectButton.visibility = View.GONE
            return
        }
        doNoEffectButton.visibility = View.VISIBLE
        doNoEffectButton.setOnClickListener {
            dismiss()
            onSelect(Const.Guid.Nil)
        }
        findTextView(R.id.button_select).visibility = View.GONE
    }

    override fun onDestroy() {
        cardActivator.onDestroy()
        super.onDestroy()
    }
}
