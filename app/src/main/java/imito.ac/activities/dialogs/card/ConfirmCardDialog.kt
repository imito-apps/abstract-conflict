package imito.ac.activities.dialogs.card

import android.view.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.activities.*
import imito.ac.game.*
import imito.ac.models.cards.*
import imito.core.*

class ConfirmCardDialog(
    activity: AppCompatActivity? = null,
    card: CardModel? = null,
    onCancel: () -> Unit = Const.EmptyAction,
    private val game: Game? = null,
    private val onConfirm: () -> Unit = Const.EmptyAction,
    private val hideConfirmButton: Boolean = false,
) : CardDialog(activity, card, onCancel, R.layout.dialog_card_confirm) {
    private var cardActivator: CardActivator? = null

    override fun changeSelf() {
        super.changeSelf()

        cardActivator = CardActivator(card!!, game!!)
        val confirmButton = findButton(R.id.button_confirm)
        if (hideConfirmButton) {
            confirmButton.visibility = View.GONE
            return
        }
        confirmButton.setOnClickListener {
            dismiss()
            onConfirm()
        }
        confirmButton.isEnabled = game.isPlayerTheCurrentOne
        cardActivator!!.watchState(activity!!) {
            confirmButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        cardActivator?.onDestroy()
        super.onDestroy()
    }
}
