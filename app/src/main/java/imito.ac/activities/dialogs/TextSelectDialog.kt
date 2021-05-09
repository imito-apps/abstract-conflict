package imito.ac.activities.dialogs

import android.content.res.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.activities.adapters.*

class TextSelectDialog(
    activity: AppCompatActivity,
    resources: Resources,
    descriptionId: Int,
    texts: Iterable<TextAdapter.KeyValue>,
    onCancel: (() -> Unit)?,
    onSelect: (Int) -> Unit,
) : DialogBase(activity, R.layout.dialog_select_text, onCancel) {
    private val description = resources.getText(descriptionId)

    private val adapter = TextAdapter {
        dismiss()
        onSelect(it.key)
    }

    init {
        adapter.replaceAll(texts)
    }

    override fun changeSelf() {
        val recyclerView = findRecyclerView(R.id.recycler_card)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        findTextView(R.id.button_description).text = description
    }
}
