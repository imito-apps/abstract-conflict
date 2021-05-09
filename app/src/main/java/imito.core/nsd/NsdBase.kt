package imito.core.nsd

import android.content.Context
import android.net.nsd.NsdManager
import androidx.appcompat.app.AppCompatActivity
import imito.core.*
import imito.core.errors.*

open class NsdBase(
    private val activity: AppCompatActivity,
    gameName: String,
) {
    protected val userSerializer = UserSerializer(gameName)
    protected val typeName = "_game._tcp."
    protected lateinit var nsdManager: NsdManager

    protected fun tryRegister(register: () -> Unit, unregister: () -> Unit) {
        nsdManager = activity.getSystemService(Context.NSD_SERVICE) as NsdManager
        ExceptionCatcher.tryRun<IllegalArgumentException>({
            ExceptionCatcher.tryRun<Exception>({
                unregister()
            }, innerAllowedMessages, "NsdBase.tryRegister - unregister", false)
            register()
        }, allowedMessages, "NsdBase.tryRegister - register")
    }

    private val allowedMessages = arrayOf(
        "listener already in use",
    )

    private val innerAllowedMessages = arrayOf(
        "listener not registered",
    )
}
