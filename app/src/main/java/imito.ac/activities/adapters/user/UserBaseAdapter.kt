package imito.ac.activities.adapters.user

import android.view.*
import imito.core.entities.*
import imito.core.views.lists.*
import java.util.*

abstract class UserBaseAdapter<TItem : UserBase, TLayout : ViewGroup, THolder : RecyclerViewHolder<TLayout>>(
    onSelect: (host: TItem) -> Unit,
) : RecyclerAdapter<TItem, TLayout, THolder>(onSelect) {

    fun add(user: TItem) {
        if (dataSet.any { it.id == user.id }) return

        dataSet.add(user)
        notifyItemInserted(dataSet.size - 1)
    }

    fun add(users: List<TItem>) {
        users.forEach { add(it) }
    }

    fun remove(id: UUID) {
        val index = dataSet.indexOfFirst { it.id == id }
        if (index < 0) return

        dataSet.removeAt(index)
        notifyItemRemoved(index)
    }
}
