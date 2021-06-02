package imito.core.views

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class SimpleToast {
    companion object {
        fun show(view: View, resourceId: Int, duration: Int = 5_000) {
            val snack = Snackbar.make(view, resourceId, duration)
            show(snack)
        }

        fun show(context: Context, view: View, text: String, duration: Int = 5_000) {
            val snack = Snackbar.make(context, view, text, duration)
            show(snack)
        }

        private fun show(snackbar: Snackbar) {
            snackbar.view
                .findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                .textAlignment = View.TEXT_ALIGNMENT_CENTER
            snackbar.show()
        }
    }
}
