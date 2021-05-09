package imito.ac.activities.dialogs.card

import android.text.method.*
import androidx.appcompat.app.*
import imito.core.*
import imito.core.views.dialogs.*
import imito.ac.*
import imito.ac.models.cards.*

open class CardDialog(
    activity: AppCompatActivity,
    val card: CardModel,
    onCancel: () -> Unit = Const.EmptyAction,
    layoutId: Int = R.layout.dialog_card,
) : DialogBase(activity, layoutId, onCancel) {
    override fun changeSelf() {
        val imageView = findImageView(R.id.image_view)
        imageView.setImageResource(card.imageId)

        val titleText = findTextView(R.id.text_view_title)
        titleText.text = card.name

        val labelPoints = resources.getString(R.string.label_card_points)
        val valueText = findTextView(R.id.text_view_points)
        valueText.text = String.format(labelPoints, card.points)

        val labelCount = resources.getString(R.string.label_card_count)
        val countText = findTextView(R.id.text_view_count)
        countText.text = String.format(labelCount, card.count)

        val descriptionText = findTextView(R.id.text_view_description)
        descriptionText.text = card.description
        descriptionText.movementMethod = ScrollingMovementMethod()
    }
}
