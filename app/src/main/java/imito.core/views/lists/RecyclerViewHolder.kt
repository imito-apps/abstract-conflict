package imito.core.views.lists

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewHolder<TLayout>(
    view: View,
    onSelect: (index: Int) -> Unit,
    layoutId: Int,
    textViewId: Int,
) : RecyclerView.ViewHolder(view) {
    val layout: TLayout = view.findViewById(layoutId)
    val textView: TextView = view.findViewById(textViewId)

    var index = 0

    init {
        view.setOnClickListener {
            onSelect(index)
        }
    }
}
