package imito.core.errors

import android.util.Log
import java.lang.StringBuilder
import java.net.ConnectException

class ExceptionCatcher {
    companion object {
        val emptyAllowedMessages = arrayOf<String>()

        val actualExceptions = StringBuilder()

        inline fun <reified TExc : Exception> tryRun(
            func: () -> Unit,
            originalThread: String = "",
            isTopCatcher: Boolean = true,
        ) {
            tryRun<TExc>(func, emptyAllowedMessages, originalThread, isTopCatcher)
        }

        inline fun <reified TExc : Exception> tryRun(
            func: () -> Unit,
            allowedMessages: Array<String>,
            originalThread: String = "",
            isTopCatcher: Boolean = true,
        ) {
            try {
                func()
            } catch (exc: Exception) {
                if (!isTopCatcher) return

                if (exc is TExc && has(exc, allowedMessages)) return

                val message = "$originalThread\n${exc.stackTraceToString()}\n\n"
                if (exc is ConnectException && has(exc, CommonErrorMessages.ForConnect)) return

                actualExceptions.append(message)
            }
        }

        fun has(exc: Exception, allowedMessages: Array<String>): Boolean {
            return allowedMessages.any { exc.message?.contains(it) == true }
        }
    }
}
