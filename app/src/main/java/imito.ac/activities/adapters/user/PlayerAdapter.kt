package imito.ac.activities.adapters.user

import android.graphics.*
import android.view.*
import android.widget.*
import imito.core.views.*
import imito.core.views.lists.*
import imito.ac.*
import imito.ac.models.*

class PlayerAdapter(
    onSelect: (PlayerModel) -> Unit,
) : UserBaseAdapter<PlayerModel, LinearLayout, PlayerAdapter.ViewHolder>(onSelect) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = getInflatedView(viewGroup, R.layout.item_player)

        return ViewHolder(view) { safeItemSelect(it) }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.index = position
        val player = dataSet[position]
        viewHolder.textViewPoints.text = "${player.points}"
        setNameTextView(viewHolder, player)
        viewHolder.textViewEliminated.text = player.cards.first!!.name
        viewHolder.textViewEliminated.visibility = getVisibleIf(player.isEliminated)
        viewHolder.imageViewDefence.visibility = getVisibleIf(player.hadDefence)

        setTopPaddingIfNotFirst(viewHolder, position)
    }

    private fun setNameTextView(
        viewHolder: ViewHolder,
        player: PlayerModel,
    ) {
        val textView = viewHolder.textView
        textView.text = player.name
        if (player.isCurrent)
            textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        else
            textView.paintFlags = textView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
    }

    class ViewHolder(
        view: View,
        onSelect: (index: Int) -> Unit,
    ) : RecyclerViewHolder<LinearLayout>(view, onSelect, R.id.layout_players, R.id.text_view_name) {
        val textViewPoints: TextView = view.findViewById(R.id.text_view_points)
        val textViewEliminated: TextView = view.findViewById(R.id.text_view_eliminated)
        val imageViewDefence: ImageView = view.findViewById(R.id.image_view_defence)

        init {
            imageViewDefence.setOnClickListener {
                SimpleToast.show(view, R.string.info_player_defended)
            }
        }
    }
}
