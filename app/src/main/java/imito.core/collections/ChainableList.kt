package imito.core.collections

import imito.core.id.*

abstract class ChainableList<T : IIdentifiable<TId>, TId, TList>(
    value: List<T>,
) : ChainableListBase<T, TList>(value) {

    fun hasId(id: TId) = value.any { it.id == id }

    fun hasAnyOf(ids: Iterable<TId>) = value.any { ids.contains(it.id) }

    fun getAnyOf(ids: Iterable<TId>) = value.first { ids.contains(it.id) }

    fun removeOne(item: T): TList {
        val index = value.indexOfFirst { it.id == item.id }
        val newValue = value.filterIndexed { i, _ -> i != index }
            .toList()
        return createSelf(newValue)
    }
}
