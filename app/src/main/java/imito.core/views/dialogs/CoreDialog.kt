package imito.core.views.dialogs

import android.app.AlertDialog
import android.util.TypedValue
import android.widget.*

class CoreDialog {
    companion object {
        fun findNegativeButton(dialog: AlertDialog): Button {
            return dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        }

        fun findPositiveButton(dialog: AlertDialog): Button {
            return dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        }

        fun findMessageTextView(dialog: AlertDialog): TextView {
            return dialog.findViewById(android.R.id.message)
        }

        fun setTextSize(textView: TextView, textSize: Float) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }
    }
}
