package imito.ac.activities.dialogs

import android.text.method.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import imito.core.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.activities.adapters.*
import imito.ac.activities.dialogs.card.*
import imito.ac.models.cards.*

class GameRulesDialog(
    activity: AppCompatActivity,
) : DialogBase(activity, R.layout.dialog_game_rules, Const.EmptyAction) {
    private val cardAdapter = CardAdapter {
        CardDialog(activity, it)
    }

    override fun changeSelf() {
        findTextView(R.id.text_view_game_rules)
            .movementMethod = ScrollingMovementMethod()

        val recyclerView = findRecyclerView(R.id.recycler_cards)
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.adapter = cardAdapter
        val cards = CardModelFactory.getCollection(true, resources).uniqueValues
        cardAdapter.replaceAll(cards)
    }
}
