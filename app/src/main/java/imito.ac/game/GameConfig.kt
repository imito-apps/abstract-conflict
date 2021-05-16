package imito.ac.game

import androidx.appcompat.app.*
import imito.core.*
import java.util.*

class GameConfig(activity: AppCompatActivity) {
    private val appConfig = AppConfig(activity)

    fun getUserId(): UUID = appConfig.getUserId()

    fun getUserName(): String = appConfig.getUserName()

    fun save() = appConfig.save()

    fun saveUserName(name: String): String? {
        if (name.isBlank()) return null

        val minAllowedChar = ' '
        val filteredName = name.filter { it >= minAllowedChar }

        val maxLength = 30
        val truncatedName = if (filteredName.length <= maxLength) filteredName
        else filteredName.substring(0, maxLength)

        appConfig.setUserName(truncatedName)
        return truncatedName
    }

    fun getValueOrFalseAndSetTrue(key: String): Boolean {
        return appConfig.getValueOrFalseAndSetTrue(key)
    }

    class Keys {
        companion object {
            const val GuideMainMenu = "GuideMainMenu"
            const val GuideGameMain = "GuideGameMain"
        }
    }
}
