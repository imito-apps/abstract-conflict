package imito.core.collections

abstract class ChainableListBase<T, TList>(
    protected val value: List<T>,
) : Iterable<T> {
    val isEmpty = value.isEmpty()
    val size = value.size

    override fun iterator(): Iterator<T> = value.iterator()

    fun addAsFirst(item: T): TList = addAt(0, item)

    fun addAsLast(item: T): TList = addAt(value.size, item)

    fun addAt(index: Int, item: T): TList {
        val newValue = value.toMutableList()
        newValue.add(index, item)
        return createSelf(newValue)
    }

    fun addAsLast(items: Iterable<T>): TList {
        val newValue = value.toMutableList()
        for (item in items) newValue.add(item)

        return createSelf(newValue)
    }

    fun clearAndAdd(item: T): TList {
        val newValue = listOf(item)
        return createSelf(newValue)
    }

    fun filter(
        predicate: (T) -> Boolean,
    ): TList {
        val newValue = value.filter(predicate)
        return createSelf(newValue)
    }

    protected abstract fun createSelf(value: List<T>): TList

    companion object
}
