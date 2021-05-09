package imito.ac.models.cards.sets.decks

import android.content.res.*
import imito.ac.entities.cards.sets.*

class ExtendedCardModels(resources: Resources) :
    CardModelsCollection(ExtendedCards.instance, resources) {
    companion object {
        private var instance: ExtendedCardModels? = null

        fun getInstance(resources: Resources): ExtendedCardModels {
            if (instance == null) {
                instance = ExtendedCardModels(resources)
            }
            return instance!!
        }
    }
}
