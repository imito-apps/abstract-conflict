package imito.ac.activities.dialogs

import android.app.AlertDialog
import android.content.*
import androidx.appcompat.app.*
import imito.core.*
import imito.ac.*
import imito.ac.notifiers.*
import imito.core.views.dialogs.*

class YesNoDialog {
    companion object {
        fun show(
            activity: AppCompatActivity,
            messageId: Int,
            onYes: () -> Unit = Const.EmptyAction,
            onNo: () -> Unit = Const.EmptyAction,
        ) {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        onYes()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        onNo()
                    }
                }
            }
            val builder = AlertDialog.Builder(activity)
            val dialog = builder.setMessage(messageId)
                .setPositiveButton(R.string.dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.dialog_no, dialogClickListener)
                .show()

            val textSize = OkDialog.getTextSize(activity)
            val textView = CoreDialog.findMessageTextView(dialog)
            CoreDialog.setTextSize(textView, textSize)

            val positiveButton = CoreDialog.findPositiveButton(dialog)
            CoreDialog.setTextSize(positiveButton, textSize)
            val negativeButton = CoreDialog.findNegativeButton(dialog)
            CoreDialog.setTextSize(negativeButton, textSize)
        }
    }
}
