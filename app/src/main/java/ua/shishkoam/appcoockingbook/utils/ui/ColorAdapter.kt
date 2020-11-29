package ua.shishkoam.appcoockingbook.utils.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ua.shishkoam.appcoockingbook.utils.util.UIUtils
import ua.shishkoam.appcoockingbook.R

class ColorAdapter(context: Context, resource: Int, objects: Array<EntityColor>) :
    ArrayAdapter<EntityColor>(context, resource, objects) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent, -1, R.layout.color_dropdown_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent, -1, R.layout.color_item)
    }

    fun getCustomView(
        position: Int, convertView: View?, parent: ViewGroup?, textSize: Int, id: Int
    ): View {
        val convertView = convertView ?: LayoutInflater.from(context).inflate(id, parent, false)
        val text = convertView.findViewById<View>(R.id.text) as TextView
        val color = convertView.findViewById<View>(R.id.color) as ImageView
        val colorNames = context.resources.getStringArray(R.array.Colors)
        text.text = colorNames[position]
        if (textSize > 0) text.textSize = textSize.toFloat()
        color.setBackgroundColor(
            UIUtils.getColorResourceByName(
                EntityColor.values()[position].name,
                context
            )
        )
        return convertView
    }
}