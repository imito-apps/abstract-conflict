package imito.core

import android.app.Activity
import android.content.*
import java.util.*

class AppConfig(activity: Activity) {
    private val preferences: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    fun getValueOrFalseAndSetTrue(key: String): Boolean {
        val value = preferences.getBoolean(key, false)
        putBoolean(key, true)
        return value
    }

    fun getUserId(): UUID {
        val userId = preferences.getString(Keys.UserId, "")
        if (userId != "") return UUID.fromString(userId)

        val newUserId = UUID.randomUUID()
        putString(Keys.UserId, newUserId.toString())
        return newUserId
    }

    fun getUserName(): String {
        return preferences.getString(Keys.UserName, "") ?: ""
    }

    fun setUserName(value: String) {
        putString(Keys.UserName, value)
    }

    fun save() {
        editor.apply()
    }

    protected fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        save()
    }

    protected fun putString(key: String, value: String) {
        editor.putString(key, value)
        save()
    }

    private class Keys {
        companion object {
            const val UserId = "UserId"
            const val UserName = "UserName"
        }
    }
}
