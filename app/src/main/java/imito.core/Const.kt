package imito.core

import java.util.*

class Const {
    companion object {
        val EmptyAction: () -> Unit = {}
    }

    class Guid {
        companion object {
            val Nil = UUID(0, 0)
            const val Length = 36
        }
    }
}
