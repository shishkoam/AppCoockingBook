package ua.shishkoam.appcoockingbook.ui.features

import androidx.recyclerview.widget.DiffUtil
import ua.shishkoam.appcoockingbook.ui.features.dummy.DummyContent

class RecyclerDiffUtil ( private val oldItems: List<DummyContent.DummyItem>,
                         private val newItems: List<DummyContent.DummyItem>,
                         private val itemDiff: ItemDiff<DummyContent.DummyItem>
): DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        itemDiff.isSame(oldItems, newItems, oldItemPosition, newItemPosition)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        itemDiff.isSameContent(oldItems, newItems, oldItemPosition, newItemPosition)
}

interface ItemDiff<T> {
    fun isSame(
        oldItems: List<T>,
        newItems: List<T>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean

    fun isSameContent(
        oldItems: List<T>,
        newItems: List<T>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean
}