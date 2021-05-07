package imito.ac.notifiers

import androidx.appcompat.app.AppCompatActivity
import imito.core.views.dialogs.*

class AppNotifier {
    private val messages = mutableListOf<String>()
    private var isShowing = false

    val isEmpty get() = messages.isEmpty()

    fun addMessage(value: String) {
        synchronized(messages) {
            messages.add(value)
        }
    }

    fun showMessage(
        activity: AppCompatActivity,
        onClosed: () -> Unit,
    ) {
        synchronized(messages) {
            if (isShowing || messages.isEmpty()) return

            isShowing = true
            val message = messages.removeAt(0)
            OkDialog.show(activity, message) {
                isShowing = false
                onClosed()
            }
        }
    }
}
