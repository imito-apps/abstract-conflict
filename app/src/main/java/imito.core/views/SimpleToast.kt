package imito.core.views

import android.content.Context
import android.widget.Toast

class SimpleToast {
    companion object {
        fun show(context: Context, resourceId: Int) {
            Toast.makeText(context, resourceId, Toast.LENGTH_LONG)
                .show()
        }

        fun show(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG)
                .show()
        }
    }
}
