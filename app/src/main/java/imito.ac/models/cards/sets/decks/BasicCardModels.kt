package imito.ac.models.cards.sets.decks

import android.content.res.*
import imito.ac.entities.cards.sets.*

class BasicCardModels(resources: Resources) :
    CardModelsCollection(BasicCards.instance, resources) {
    companion object {
        private var instance: BasicCardModels? = null

        fun getInstance(resources: Resources): BasicCardModels {
            if (instance == null) {
                instance = BasicCardModels(resources)
            }
            return instance!!
        }
    }
}
