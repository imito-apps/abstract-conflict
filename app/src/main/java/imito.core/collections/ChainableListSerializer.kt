package imito.core.collections

fun <T> ChainableListBase.Companion.deserialize(
    separator: String,
    text: String?,
    deserializeOne: (String) -> T,
): List<T> {
    if (text.isNullOrBlank()) return listOf()

    return text.split(separator)
        .map { deserializeOne(it) }
        .toList()
}
