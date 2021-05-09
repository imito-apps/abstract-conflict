package imito.core.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import imito.core.entities.*
import imito.core.errors.*

class NsdRegisterer(
    activity: AppCompatActivity,
    gameName: String,
) : NsdBase(activity, gameName) {
    companion object {
        private const val TAG = "NsdRegisterer"
    }

    private var serviceName = ""

    fun getServiceName(): String = serviceName

    fun register(port: Int, user: NetUser) {
        val serviceInfo = NsdServiceInfo().apply {
            // The name can change if conflicts with other services advertised on the same network.
            serviceName = userSerializer.serialize(user)
            serviceType = typeName
            setPort(port)
        }
        tryRegister({
            nsdManager.registerService(
                serviceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                registrationListener
            )
        }, ::unregister)
    }

    private val registrationListener = object : NsdManager.RegistrationListener {
        override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to resolve a conflict.
            serviceName = nsdServiceInfo.serviceName
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            throw Exception("RegistrationListener Registration failed! errorCode $errorCode")
        }

        override fun onServiceUnregistered(arg0: NsdServiceInfo) {
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        }
    }

    fun unregister() {
        ExceptionCatcher.tryRun<IllegalArgumentException>({
            nsdManager.unregisterService(registrationListener)
        }, allowedMessages, "unregister", false)
    }

    private val allowedMessages = arrayOf(
        "listener not registered",
    )
}
