package ua.shishkoam.appcoockingbook.ui.features.dummy

import java.util.*
import kotlin.random.Random

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
object DummyContent {

    val ITEMS: MutableList<DummyItem> = ArrayList()
    val ITEMS_UPDATE: MutableList<DummyItem> = ArrayList()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(Random.nextInt(0, 100) ))
            addItemUpdate(createDummyItem(Random.nextInt(0, 100)))
        }

    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
    }

    private fun addItemUpdate(item: DummyItem) {
        ITEMS_UPDATE.add(item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem("Item $position", "http://www.google.es/images/srpr/logo11w.png")
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val name: String, val image: String) {
        override fun toString(): String = name
    }
}