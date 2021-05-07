package imito.core.views

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class PortraitActivity(
    private val layoutId: Int,
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) return

        setContentView(layoutId)
        createSelf(savedInstanceState)
    }

    abstract fun createSelf(savedInstanceState: Bundle?)
}
