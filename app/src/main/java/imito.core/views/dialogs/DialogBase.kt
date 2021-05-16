package imito.core.views.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView

abstract class DialogBase(
    private val activity: AppCompatActivity? = null,
    private val layoutId: Int = 0,
    private val onCancel: (() -> Unit)? = null,
) : DialogFragment() {
    private var dialog: AlertDialog? = null
    private val alertDialog: AlertDialog get() = dialog!!

    init {
        if (activity != null) {
            show(activity.supportFragmentManager, "")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) return getEmptyDialog()

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(layoutId, null))

        dialog = builder.show()
        if (onCancel == null) isCancelable = false

        changeSelf()

        return alertDialog
    }

    override fun onCancel(dialog: DialogInterface) {
        onCancel?.invoke()
        super.onCancel(dialog)
    }

    protected abstract fun changeSelf()

    protected fun findButton(id: Int) = alertDialog.findViewById<Button>(id)!!

    protected fun findImageView(id: Int) = alertDialog.findViewById<ImageView>(id)!!

    protected fun findLinearLayout(id: Int) = alertDialog.findViewById<LinearLayout>(id)!!

    protected fun findProgressBar(id: Int) = alertDialog.findViewById<ProgressBar>(id)!!

    protected fun findRecyclerView(id: Int) = alertDialog.findViewById<RecyclerView>(id)!!

    protected fun findTextView(id: Int) = alertDialog.findViewById<TextView>(id)!!

    private fun getEmptyDialog(): Dialog {
        dismiss()
        return AlertDialog.Builder(context!!)
            .show()
    }
}
