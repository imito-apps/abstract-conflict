package imito.ac.activities.adapters

import android.view.*
import android.widget.*
import imito.core.views.lists.*
import imito.ac.*
import imito.ac.models.cards.*

class CardAdapter(
    onSelect: (card: CardModel) -> Unit,
) : RecyclerAdapter<CardModel, FrameLayout, CardAdapter.ViewHolder>(onSelect) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = getInflatedView(viewGroup, R.layout.item_card)
        return ViewHolder(view) { safeItemSelect(it) }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].name
        viewHolder.index = position
        setTopPaddingIfNotFirst(viewHolder, position)
    }

    class ViewHolder(
        view: View,
        onSelect: (index: Int) -> Unit,
    ) : RecyclerViewHolder<FrameLayout>(view, onSelect, R.id.layout_cards, R.id.text_view)
}
