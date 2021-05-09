package imito.ac.activities.dialogs.card

import android.view.*
import androidx.appcompat.app.*
import imito.ac.*
import imito.ac.activities.*
import imito.ac.game.*
import imito.ac.models.cards.*

class ConfirmCardDialog(
    activity: AppCompatActivity,
    card: CardModel,
    onCancel: () -> Unit,
    private val game: Game,
    private val onConfirm: () -> Unit,
    private val hideConfirmButton: Boolean = false,
) : CardDialog(activity, card, onCancel, R.layout.dialog_card_confirm) {
    private val cardActivator = CardActivator(card, game)

    override fun changeSelf() {
        super.changeSelf()

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
        cardActivator.watchState(activity!!) {
            confirmButton.isEnabled = true
        }
    }

    override fun onDestroy() {
        cardActivator.onDestroy()
        super.onDestroy()
    }
}
