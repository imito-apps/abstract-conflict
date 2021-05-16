package imito.ac.activities.dialogs

import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.activities.adapters.*

class TextSelectDialog(
    activity: AppCompatActivity? = null,
    private val descriptionId: Int = 0,
    texts: Iterable<TextAdapter.KeyValue>? = null,
    onCancel: (() -> Unit)? = null,
    onSelect: ((Int) -> Unit)? = null,
) : DialogBase(activity, R.layout.dialog_select_text, onCancel) {

    private val adapter = TextAdapter {
        dismiss()
        onSelect!!(it.key)
    }

    init {
        if (texts != null) adapter.replaceAll(texts)
    }

    override fun changeSelf() {
        val recyclerView = findRecyclerView(R.id.recycler_card)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val description = resources.getText(descriptionId)
        findTextView(R.id.button_description).text = description
    }
}
