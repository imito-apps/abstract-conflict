package imito.core.nsd

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import imito.core.*
import imito.core.entities.*
import imito.core.errors.*
import java.net.ConnectException
import java.net.Socket
import java.util.*

class NsdDiscoverer(
    activity: AppCompatActivity,
    val gameName: String,
    val getServiceName: () -> String,
    val onFound: (DiscoveredService) -> Unit,
    val onLost: (UUID) -> Unit,
) : NsdBase(activity, gameName) {
    companion object {
        private const val TAG = "NsdDiscoverer"
    }

    fun onAppStart() {
        discoverServices()
    }

    fun onAppStop() {
        // service discovery is an expensive operation
        unregisterDiscoveryListener()
    }

    private fun discoverServices() {
        tryRegister({
            nsdManager.discoverServices(
                typeName,
                NsdManager.PROTOCOL_DNS_SD,
                discoveryListener
            )
        }, ::unregisterDiscoveryListener)
    }

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(regType: String) {
        }

        override fun onServiceFound(service: NsdServiceInfo) {
            val serviceName = service.serviceName
            when {
                service.serviceType != typeName -> return
                serviceName == getServiceName() -> return
                serviceName.startsWith(this@NsdDiscoverer.gameName) -> {
                    ExceptionCatcher.tryRun<Exception>({
                        nsdManager.resolveService(service, resolveListener)
                    })
                }
            }
        }

        override fun onServiceLost(service: NsdServiceInfo?) {
            if (service == null) return

            val id = userSerializer.deserializeId(service.serviceName)
            onLost(id)
        }

        override fun onDiscoveryStopped(serviceType: String) {
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            unregisterDiscoveryListener()
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            unregisterDiscoveryListener()
        }
    }

    private val resolveListener = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            if (serviceInfo.serviceName == getServiceName()) return

            ExceptionCatcher.tryRun<ConnectException>({
                checkIfServiceExists(serviceInfo)
                val player = userSerializer.deserialize(serviceInfo.serviceName, serviceInfo.host) ?: return

                val discoveredService = DiscoveredService(
                    player.id,
                    player.address,
                    player.name,
                    serviceInfo.port
                )
                onFound(discoveredService)
            }, CommonErrorMessages.ForConnect)
        }
    }

    private fun checkIfServiceExists(serviceInfo: NsdServiceInfo) {
        val client = Socket(serviceInfo.host, serviceInfo.port)
        client.close()
    }

    private fun unregisterDiscoveryListener() {
        ExceptionCatcher.tryRun<IllegalArgumentException>({
            nsdManager.stopServiceDiscovery(discoveryListener)
        }, allowedMessages, "unregisterDiscoveryListener", false)
    }

    private val allowedMessages = arrayOf(
        "service discovery not active on listener",
    )
}
