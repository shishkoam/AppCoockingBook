package ua.shishkoam.appcoockingbook.oldutils.ui

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.*
import android.widget.*
import ua.shishkoam.appcoockingbook.R

/**
 * Created by Shevron on 06.05.2015.
 */
class MenuGroup : LinearLayout {
    private var content: LinearLayout? = null
    private var iconLeft: ImageView? = null
    private var icon0: ImageView? = null
    private var icon1: ImageView? = null
    private var icon2: ImageView? = null
    var label: TextView? = null
    var info: TextView? = null
    private var icon0Src = 0
    private var expandable = false
    private var expanded = false
    private var menu: PopupMenu? = null
    private var menuOpenButton: ImageButton? = null
    private var customClickListener: OnClickListener? = null
    private var defaultTintColor = 0
    private var hoveredTintColor = 0

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialize(context, attrs)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (content != null && child.id != R.id.menugroup_header && child.id != R.id.menugroup_content) content?.addView(
            child,
            index,
            params
        ) else super.addView(child, index, params)
    }

    val contentView: View?
        get() = content

    fun setExpanded(expanded: Boolean) {
        if (!expandable) return
        icon0Src =
            if (expanded) R.drawable.ic_menu_arrow_up_light else R.drawable.ic_menu_arrow_down_light
        icon0?.setImageResource(icon0Src)
        content?.visibility = if (expanded) VISIBLE else GONE
        this.expanded = expanded
    }

    private fun initialize(context: Context, attributeSet: AttributeSet?) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.menu_group, this, true)
        label = view.findViewById<View>(R.id.label) as TextView
        info = view.findViewById<View>(R.id.info) as TextView
        iconLeft = view.findViewById<View>(R.id.iconLeft) as ImageView
        icon0 = view.findViewById<View>(R.id.icon0) as ImageView
        icon1 = view.findViewById<View>(R.id.icon1) as ImageView
        icon2 = view.findViewById<View>(R.id.icon2) as ImageView
        menuOpenButton = view.findViewById<View>(R.id.menu_open_button) as ImageButton
        //menu     = (PopupMenu) view.findViewById(R.id.menu_open_button);
        val header = view.findViewById<View>(R.id.menugroup_header) as LinearLayout
        content = view.findViewById<View>(R.id.menugroup_content) as LinearLayout
        if (attributeSet != null) {
            val attrs = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.MenuGroup,
                0, 0
            )
            defaultTintColor = context.resources.getColor(android.R.color.white)
            hoveredTintColor = context.resources.getColor(android.R.color.holo_blue_dark)
            val labelText = attrs.getString(R.styleable.MenuGroup_labelText)
            val infoText = attrs.getString(R.styleable.MenuGroup_infoText)
            val backColor = attrs.getResourceId(R.styleable.MenuGroup_backgroundColor, R.color.Blue)
            val labelColor = attrs.getResourceId(R.styleable.MenuGroup_labelColor, R.color.White)
            val infoColor = attrs.getResourceId(R.styleable.MenuGroup_infoColor, labelColor)
            val labelSize = attrs.getDimension(R.styleable.MenuGroup_labelSize, -1f)
            expanded = attrs.getBoolean(R.styleable.MenuGroup_expanded, false)
            expandable = attrs.getBoolean(R.styleable.MenuGroup_expandable, expanded)
            expanded = expandable && expanded
            val isMenuMode = attrs.getBoolean(R.styleable.MenuGroup_showMenu, false)
            val iconLeftSrc = attrs.getResourceId(R.styleable.MenuGroup_iconLeft, -1)
            val icon0Src = attrs.getResourceId(R.styleable.MenuGroup_iconRight0, -1)
            val icon1Src = attrs.getResourceId(R.styleable.MenuGroup_iconRight1, -1)
            val icon2Src = attrs.getResourceId(R.styleable.MenuGroup_iconRight2, -1)
            val menuIcon = attrs.getResourceId(R.styleable.MenuGroup_menuIcon, -1)
            val menuPosition = attrs.getInteger(R.styleable.MenuGroup_menuPosition, -1)
            val menuRef = attrs.getResourceId(R.styleable.MenuGroup_menuRef, -1)
            header.setBackgroundColor(resources.getColor(backColor))
            label?.setTextColor(resources.getColor(labelColor))
            label?.text = labelText
            info?.setTextColor(resources.getColor(infoColor))
            setInfo(infoText)
            if (iconLeftSrc == -1) {
                iconLeft?.visibility = GONE
                iconLeft?.setImageBitmap(null)
            } else {
                iconLeft?.visibility = VISIBLE
                iconLeft?.setImageResource(iconLeftSrc)
            }
            if (icon0Src == -1 && !expandable) {
                icon0?.visibility = GONE
                icon0?.setImageBitmap(null)
            } else {
                icon0?.visibility = VISIBLE
                if (icon0Src != -1) {
                    icon0?.setImageResource(icon0Src)
                }
            }
            if (icon1Src == -1) {
                icon1?.visibility = GONE
                icon1?.setImageBitmap(null)
            } else {
                icon1?.visibility = VISIBLE
                icon1?.setImageResource(icon1Src)
            }
            if (icon2Src == -1) {
                icon2?.visibility = GONE
                icon2?.setImageBitmap(null)
            } else {
                icon2?.visibility = VISIBLE
                icon2?.setImageResource(icon2Src)
            }
            if (isMenuMode && menuPosition > -1 && menuRef > -1) setupMenu(
                menuIcon,
                menuPosition,
                menuRef
            )

            // Disable fucking margin
            if (iconLeft?.visibility == VISIBLE) {
                val params = TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
                params.gravity = Gravity.CENTER_VERTICAL
                params.leftMargin = 0
                label?.layoutParams = params
            }

            // This works starnge in editor
            if (labelSize > 0) label?.textSize = labelSize
            header.setOnClickListener { v ->
                if (expandable) setExpanded(!expanded)
                if (customClickListener != null) customClickListener?.onClick(v)
            }
            setExpanded(expanded)
        }
    }

    fun getExpanded(): Boolean {
        return expanded
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        customClickListener = listener
    }

    fun setOnLeftIconClickListener(listener: OnClickListener) {
        setListener(iconLeft, listener)
    }

    fun setOnRightIcon0ClickListener(listener: OnClickListener) {
        setListener(icon0, listener)
    }

    fun setOnRightIcon1ClickListener(listener: OnClickListener) {
        setListener(icon1, listener)
    }

    fun setOnRightIcon2ClickListener(listener: OnClickListener) {
        setListener(icon2, listener)
    }

    fun setOnMenuItemSelectedListener(listener: PopupMenu.OnMenuItemClickListener?) {
        if (menu != null) menu?.setOnMenuItemClickListener(listener)
    }

    private fun setListener(view: View?, listener: OnClickListener) {
        view?.setOnClickListener(listener)
        view?.setOnHoverListener { v, event ->
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                (v as ImageView).setColorFilter(hoveredTintColor, PorterDuff.Mode.SRC_IN)
            } else if (action == MotionEvent.ACTION_UP) {
                (v as ImageView).setColorFilter(defaultTintColor, PorterDuff.Mode.SRC_IN)
            }
            true
        }
    }

    fun setInfo(infoText: String?) {
        info?.text = infoText
        info?.visibility =
            if (infoText != null && !infoText.isEmpty()) VISIBLE else GONE
    }

    fun setLabel(labelText: String?) {
        if (labelText != null) {
            label?.text = labelText
        }
    }

    fun setIconLeft(resId: Int) {
        iconLeft?.setImageResource(resId)
        setLeftIconVisible(true)
    }

    fun setRightIcon0(resId: Int) {
        icon0?.setImageResource(resId)
        setRightIcon0Visible(true)
    }

    fun setRightIcon1(resId: Int) {
        icon1?.setImageResource(resId)
        setRightIcon1Visible(true)
    }

    fun setRightIcon2(resId: Int) {
        icon2?.setImageResource(resId)
        setRightIcon2Visible(true)
    }

    fun setLeftIconVisible(visible: Boolean) {
        iconLeft?.visibility = if (visible) VISIBLE else GONE
    }

    fun setRightIcon0Visible(visible: Boolean) {
        icon0?.visibility = if (visible) VISIBLE else GONE
    }

    fun setRightIcon1Visible(visible: Boolean) {
        icon1?.visibility = if (visible) VISIBLE else GONE
    }

    fun setRightIcon2Visible(visible: Boolean) {
        icon2?.visibility = if (visible) VISIBLE else GONE
    }

    fun collapse() {
        setExpanded(false)
    }

    fun expand() {
        setExpanded(true)
    }

    fun removeIconsAndListeners() {
        iconLeft?.setOnClickListener(null)
        icon0?.setOnClickListener(null)
        icon1?.setOnClickListener(null)
        icon2?.setOnClickListener(null)
        menuOpenButton?.setOnClickListener(null)
        iconLeft?.visibility = GONE
        icon0?.visibility = GONE
        icon1?.visibility = GONE
        icon2?.visibility = GONE
        menuOpenButton?.visibility = GONE
    }

    private fun setupMenu(src: Int, position: Int, menuRef: Int) {
        val viewToReplace = getViewToReplace(position)

        //putting view to appropriate position
        val view = menuOpenButton?.parent as ViewGroup
        val index = view.indexOfChild(viewToReplace)
        view.removeView(viewToReplace)
        view.removeView(menuOpenButton)
        view.addView(menuOpenButton, index)
        setupMenuAppearance(src, menuRef)
        menuOpenButton?.setOnClickListener {
            if (menu != null) {
                menu?.show()
            }
        }
    }

    private fun getViewToReplace(position: Int): View? {
        return if (position == 0) icon0 else if (position == 1) icon1 else icon2
    }

    private fun setupMenuAppearance(src: Int, menuRef: Int) {
        menuOpenButton?.visibility = VISIBLE
        if (src != -1) menuOpenButton?.setImageResource(src)
        menu = PopupMenu(context, menuOpenButton)
        menu?.menuInflater?.inflate(menuRef, menu?.menu)

        //for showing imgs in menu
        try {
            val fields = menu?.javaClass?.declaredFields
            fields?.let {
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[menu]
                        val classPopupHelper = Class.forName(
                            menuPopupHelper
                                .javaClass.name
                        )
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromWindow() {
        clearReferences()
        super.onDetachedFromWindow()
    }

    private fun clearReferences() {
        customClickListener = null
        iconLeft?.setOnClickListener(null)
        icon0?.setOnClickListener(null)
        icon1?.setOnClickListener(null)
        icon2?.setOnClickListener(null)
        menuOpenButton?.setOnClickListener(null)
    }
}