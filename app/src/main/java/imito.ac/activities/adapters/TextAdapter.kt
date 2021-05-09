package imito.ac.activities.adapters

import android.view.*
import android.widget.*
import imito.core.views.lists.*
import imito.ac.*

class TextAdapter(
    onSelect: (KeyValue) -> Unit,
) : RecyclerAdapter<TextAdapter.KeyValue, FrameLayout, TextAdapter.ViewHolder>(onSelect) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = getInflatedView(viewGroup, R.layout.item_text)

        return ViewHolder(view) { safeItemSelect(it) }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].value
        viewHolder.index = position
        setTopPaddingIfNotFirst(viewHolder, position)
    }

    class ViewHolder(
        view: View,
        onSelect: (index: Int) -> Unit,
    ) : RecyclerViewHolder<FrameLayout>(view, onSelect, R.id.layout_texts, R.id.text_view)

    class KeyValue(
        val key: Int,
        val value: String,
    )
}
