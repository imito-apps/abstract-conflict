package imito.ac.activities.adapters.user

import android.view.*
import android.widget.*
import imito.core.entities.*
import imito.core.views.lists.*
import imito.ac.*

class NetPlayerAdapter(
    onSelect: (UserBase) -> Unit,
) : UserBaseAdapter<UserBase, LinearLayout, NetPlayerAdapter.ViewHolder>(onSelect) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = getInflatedView(viewGroup, R.layout.item_net_player)

        return ViewHolder(view) { safeItemSelect(it) }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position].name
        viewHolder.index = position
    }

    class ViewHolder(
        view: View,
        onSelect: (index: Int) -> Unit,
    ) : RecyclerViewHolder<LinearLayout>(view, onSelect, R.id.layout_net_players, R.id.text_view)
}
