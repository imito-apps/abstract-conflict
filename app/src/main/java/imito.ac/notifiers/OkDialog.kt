package imito.ac.notifiers

import androidx.appcompat.app.*
import imito.ac.*
import imito.core.*
import imito.core.views.dialogs.*

class OkDialog {
    companion object {
        fun show(
            activity: AppCompatActivity,
            messageId: Int,
            onClosed: () -> Unit = Const.EmptyAction,
        ) {
            val message = activity.resources
                .getString(messageId)
            show(activity, message, onClosed)
        }

        fun show(
            activity: AppCompatActivity,
            message: String,
            onClosed: () -> Unit = Const.EmptyAction,
        ) {
            val textSize = getTextSize(activity)
            CoreOkDialog.show(activity, message, textSize, onClosed)
        }

        fun getTextSize(activity: AppCompatActivity): Float {
            return activity.resources
                .getDimension(R.dimen.info_dialog_text_size)
        }
    }
}
