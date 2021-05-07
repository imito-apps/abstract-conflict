package imito.core.entities

import imito.core.id.*
import java.util.*

open class UserBase(
    override val id: UUID,
    val name: String,
) : IGuidIdentifiable
