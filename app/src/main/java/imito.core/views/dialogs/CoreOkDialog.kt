package imito.core.views.dialogs

import android.app.*
import android.content.*
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import imito.core.*

class CoreOkDialog {
    companion object {
        fun show(
            activity: AppCompatActivity,
            message: String,
            textSize: Float = 16F,
            onClosed: () -> Unit = Const.EmptyAction,
        ) {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) onClosed()
            }
            val builder = AlertDialog.Builder(activity)
            val dialog = builder.setMessage(message)
                .setPositiveButton("OK", dialogClickListener)
                .setOnCancelListener { onClosed() }
                .setOnDismissListener { onClosed() }
                .show()
            val textView = CoreDialog.findMessageTextView(dialog)
            CoreDialog.setTextSize(textView, textSize)

            val positiveButton = CoreDialog.findPositiveButton(dialog)
            CoreDialog.setTextSize(positiveButton, textSize)
            (positiveButton.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
        }
    }
}
