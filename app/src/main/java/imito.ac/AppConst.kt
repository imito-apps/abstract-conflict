package imito.ac

class AppConst {
    companion object {
        const val GameName = "message"
        private val FirstCompatibleVerForVer = arrayOf(
            0,
            1,//1
        )
        const val VersionCode = BuildConfig.VERSION_CODE
        val FirstCompatibleVersion = FirstCompatibleVerForVer[VersionCode]
    }
}
