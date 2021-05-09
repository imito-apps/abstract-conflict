package imito.core.views.lists

import android.view.*
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter<TItem, TLayout : ViewGroup, THolder : RecyclerViewHolder<TLayout>>(
    protected val onSelect: (item: TItem) -> Unit,
) : RecyclerView.Adapter<THolder>() {

    protected val dataSet = mutableListOf<TItem>()

    override fun getItemCount() = dataSet.size

    fun removeAll() {
        notifyItemRangeRemoved(0, dataSet.size)
        dataSet.clear()
    }

    fun replaceAll(iterable: Iterable<TItem>) {
        removeAll()

        dataSet.addAll(iterable)
        notifyItemRangeInserted(0, dataSet.size - 1)
    }

    protected fun getInflatedView(
        viewGroup: ViewGroup,
        layoutId: Int,
    ): View {
        return LayoutInflater.from(viewGroup.context)
            .inflate(layoutId, viewGroup, false)
    }

    protected fun getVisibleIf(value: Boolean) =
        if (value) View.VISIBLE
        else View.GONE

    protected fun setTopPaddingIfNotFirst(viewHolder: THolder, position: Int, padding: Int = 1) {
        val topPadding = if (position == 0) 0
        else padding

        viewHolder.layout.setPadding(0, topPadding, 0, 0)
    }

    protected fun safeItemSelect(index: Int) {
        if (index < dataSet.size) onSelect(dataSet[index])
    }
}
