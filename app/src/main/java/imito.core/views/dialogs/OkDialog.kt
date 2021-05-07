package imito.core.views.dialogs

import android.app.*
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import imito.core.*

class OkDialog {
    companion object {
        fun show(
            activity: AppCompatActivity,
            messageId: Int,
            onClosed: () -> Unit = Const.EmptyAction,
        ) {
            val message = activity.resources.getString(messageId)
            show(activity, message, onClosed)
        }

        fun show(
            activity: AppCompatActivity,
            message: String,
            onClosed: () -> Unit = Const.EmptyAction,
        ) {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) onClosed()
            }
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(message)
                .setNeutralButton("OK", dialogClickListener)
                .setOnCancelListener { onClosed() }
                .setOnDismissListener { onClosed() }
                .show()
        }
    }
}
