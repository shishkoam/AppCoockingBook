package ua.shishkoam.appcoockingbook.ui.features

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mCurrentPosition : Int = -1

    protected abstract fun clear();

    fun onBind(position : Int) {
        mCurrentPosition = position;
//        clear()
    }

    fun getCurrentPosition() : Int {
        return mCurrentPosition;
    }

}