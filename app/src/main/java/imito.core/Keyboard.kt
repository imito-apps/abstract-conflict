package imito.core

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

class Keyboard {
    companion object {
        fun hide(activity: Activity) {
            val inputMethodManager = getMethodManager(activity)
            val view = activity.currentFocus ?: View(activity)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun show(activity: Activity) {
            val inputMethodManager = getMethodManager(activity)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }

        private fun getMethodManager(activity: Activity): InputMethodManager {
            return activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        }
    }
}
