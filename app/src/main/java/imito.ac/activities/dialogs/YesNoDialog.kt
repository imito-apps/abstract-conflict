package imito.ac.activities.dialogs

import android.app.*
import android.content.*
import imito.core.*
import imito.ac.*

class YesNoDialog {
    companion object {
        fun show(
            context: Context,
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
            val builder = AlertDialog.Builder(context)
            builder.setMessage(messageId)
                .setPositiveButton(R.string.dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.dialog_no, dialogClickListener)
                .show()
        }
    }
}
