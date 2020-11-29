package ua.shishkoam.appcoockingbook.oldutils.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import ua.shishkoam.appcoockingbook.R
import java.io.File
import java.io.FilenameFilter
import java.util.*
import kotlin.math.roundToInt

/**
 * Created with IntelliJ IDEA.
 * User: Scogun
 * Date: 27.11.13
 * Time: 10:47
 */
class OpenFileDialog(context: Context) : AlertDialog.Builder(context) {
    private var currentPath = ""
    private val files: MutableList<File> = ArrayList()
    private val title: TextView
    private val listView: ListView
    private var filenameFilter: FilenameFilter? = null
    private var selectedIndex = -1
    private var listener: OpenDialogListener? = null
    private var onCancelListener: DialogInterface.OnCancelListener? = null
    private var folderIcon: Drawable? = null
    private var fileIcon: Drawable? = null
    private var accessDeniedMessage: String? = null

    interface OpenDialogListener {
        fun onSelectedFile(fileName: String?)
    }

    private inner class FileAdapter(context: Context, files: List<File>) : ArrayAdapter<File>(
        context, android.R.layout.simple_spinner_item, files) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: TextView = super.getView(position, convertView, parent) as TextView
            val file: File? = getItem(position)
            view.text = file?.name
            if (file?.isDirectory == true) {
                setDrawable(view, folderIcon)
            } else {
                setDrawable(view, fileIcon)
                if (selectedIndex == position) {
                    view.setBackgroundColor(
                        context.resources.getColor(
                            android.R.color.holo_blue_dark
                        )
                    )
                } else view.setBackgroundColor(
                    context.resources.getColor(android.R.color.transparent)
                )
            }
            return view
        }

        private fun setDrawable(view: TextView?, drawable: Drawable?) {
            if (view != null) {
                if (drawable != null) {
                    val size = dpToPx(30)
                    drawable.setBounds(0, 0, size, size)
                    view.compoundDrawablePadding = 10
                    view.setCompoundDrawables(drawable, null, null, null)
                } else {
                    view.setCompoundDrawables(null, null, null, null)
                }
            }
        }

        private fun dpToPx(dp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }
    }

    override fun show(): AlertDialog {
        files.addAll(getFiles(currentPath))
        listView.adapter = FileAdapter(context, files)
        return super.show()
    }

    fun show(path: String): AlertDialog {
        files.addAll(getFiles(path))
        listView.adapter = FileAdapter(context, files)
        return super.show()
    }

    fun setFilter(filter: String): OpenFileDialog {
        filenameFilter = FilenameFilter { file, fileName ->
            val tempFile = File(file.path, fileName)
            if (tempFile.isFile) tempFile.name.matches(filter.toRegex()) else true
        }
        return this
    }

    fun setFilterOnlyDirectory(): OpenFileDialog {
        filenameFilter = FilenameFilter { dir, filename -> File(dir, filename).isDirectory }
        return this
    }

    fun setAllowableExtensions(exts: Array<String?>): OpenFileDialog {
        if (exts.isNotEmpty()) {
            filenameFilter = object : FilenameFilter {
                override fun accept(file: File, fileName: String): Boolean {
                    val tempFile = File(file.path, fileName)
                    return if (tempFile.isFile) matches(tempFile.name) else tempFile.isDirectory
                }

                private fun matches(nameOfFile: String): Boolean {
                    val fileExtension = getExtension(nameOfFile)
                    return fileExtension != null && fileExtension.isNotEmpty() && Arrays.asList(*exts)
                        .contains(fileExtension)
                }
            }
        }
        return this
    }

    fun setOpenDialogListener(listener: OpenDialogListener): OpenFileDialog {
        this.listener = listener
        return this
    }

    override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): OpenFileDialog {
        this.onCancelListener = onCancelListener
        super.setOnCancelListener(onCancelListener)
        return this
    }

    fun setFolderIcon(drawable: Drawable?): OpenFileDialog {
        folderIcon = drawable
        return this
    }

    fun setFileIcon(drawable: Drawable?): OpenFileDialog {
        fileIcon = drawable
        return this
    }

    fun setAccessDeniedMessage(message: String?): OpenFileDialog {
        accessDeniedMessage = message
        return this
    }

    fun setCurrentPath(context: Context, currentPath: String): OpenFileDialog {
        this.currentPath = currentPath
        changeTitle(context)
        return this
    }

    private fun createMainLayout(context: Context): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.minimumHeight = getLinearLayoutMinHeight(context)
        return linearLayout
    }

    private fun getItemHeight(context: Context): Int {
        val value = TypedValue()
        val metrics = DisplayMetrics()
        context.theme.resolveAttribute(R.attr.listPreferredItemHeightSmall, value, true)
        getDefaultDisplay(context).getMetrics(metrics)
        return TypedValue.complexToDimension(value.data, metrics).toInt()
    }

    private fun createTextView(context: Context, style: Int): TextView {
        val textView = TextView(context)
        textView.setTextAppearance(context, style)
        val itemHeight = getItemHeight(context)
        textView.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight)
        textView.minHeight = itemHeight
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setPadding(15, 0, 0, 0)
        return textView
    }

    private fun createTitle(context: Context): TextView {
        return createTextView(context, android.R.style.TextAppearance_DeviceDefault_Large)
    }

    private fun createBackItem(context: Context): TextView {
        val textView = createTextView(context, android.R.style.TextAppearance_DeviceDefault_Small)
        val drawable = getContext().resources.getDrawable(android.R.drawable.ic_menu_directions)
        drawable.setBounds(0, 0, 60, 60)
        textView.setCompoundDrawables(drawable, null, null, null)
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.setOnClickListener {
            val file = File(currentPath)
            val parentDirectory = file.parentFile
            if (parentDirectory != null) {
                currentPath = parentDirectory.path
                rebuildFiles((listView.adapter as FileAdapter))
            }
        }
        return textView
    }

    fun getTextWidth(text: String, paint: Paint): Int {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.left + bounds.width() + 80
    }

    private fun changeTitle(context: Context) {
        var titleText = currentPath
        val screenWidth = getScreenSize(context).x
        val maxWidth = (screenWidth * 0.99).toInt()
        if (getTextWidth(titleText, title.paint) > maxWidth) {
            while (getTextWidth("...$titleText", title.paint) > maxWidth) {
                val start = titleText.indexOf("/", 2)
                if (start > 0) titleText = titleText.substring(start) else titleText =
                    titleText.substring(2)
            }
            title.text = "...$titleText"
        } else {
            title.text = titleText
        }
    }

    private fun getFiles(directoryPath: String): List<File> {
        val directory = File(directoryPath)
        val listFiles = directory.listFiles(filenameFilter) ?: return ArrayList()
        val fileList = listOf(*listFiles)
        try {
            Collections.sort(
                fileList,
                Comparator { file, file2 ->
                    if (file.isDirectory && file2.isFile) -1 else if (file.isFile && file2.isDirectory) 1 else file.path.compareTo(
                        file2.path
                    )
                })
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return fileList
    }

    private fun rebuildFiles(adapter: ArrayAdapter<File>) {
        try {
            val fileList = getFiles(currentPath)
            files.clear()
            selectedIndex = -1
            files.addAll(fileList)
            adapter.notifyDataSetChanged()
            changeTitle(context)
        } catch (e: NullPointerException) {
            var message: String? = context.resources.getString(android.R.string.unknownName)
            if (accessDeniedMessage != null && accessDeniedMessage != "") message =
                accessDeniedMessage
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createListView(context: Context): ListView {
        val listView = ListView(context)
        listView.onItemClickListener =
            OnItemClickListener { adapterView, _, index, _ ->
                val adapter: ArrayAdapter<File> = adapterView.adapter as FileAdapter
                val file: File? = adapter.getItem(index)
                if (file?.isDirectory == true) {
                    currentPath = file.path
                    rebuildFiles(adapter)
                } else {
                    selectedIndex = if (index != selectedIndex) index else -1
                    adapter.notifyDataSetChanged()
                }
            }
        return listView
    }

    companion object {
        private fun getDefaultDisplay(context: Context): Display {
            return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }

        private fun getScreenSize(context: Context): Point {
            val screeSize = Point()
            getDefaultDisplay(context).getSize(screeSize)
            return screeSize
        }

        private fun getLinearLayoutMinHeight(context: Context): Int {
            return getScreenSize(context).y
        }

        private val NOT_FOUND = -1

        /**
         * The extension separator character.
         * @since 1.4
         */
        val EXTENSION_SEPARATOR = '.'

        /**
         * The Unix separator character.
         */
        private val UNIX_SEPARATOR = '/'

        /**
         * The Windows separator character.
         */
        private val WINDOWS_SEPARATOR = '\\'

        /**
         * The system separator character.
         */
        private val SYSTEM_SEPARATOR = File.separatorChar

        /**
         * Gets the extension of a filename.
         *
         *
         * This method returns the textual part of the filename after the last dot.
         * There must be no directory separator after the dot.
         * <pre>
         * foo.txt      --&gt; "txt"
         * a/b/c.jpg    --&gt; "jpg"
         * a/b.txt/c    --&gt; ""
         * a/b/c        --&gt; ""
        </pre> *
         *
         *
         * The output will be the same irrespective of the machine that the code is running on.
         *
         * @param filename the filename to retrieve the extension of.
         * @return the extension of the file or an empty string if none exists or `null`
         * if the filename is `null`.
         */
        fun getExtension(filename: String?): String? {
            if (filename == null) {
                return null
            }
            val index = indexOfExtension(filename)
            return if (index == NOT_FOUND) {
                ""
            } else {
                filename.substring(index + 1)
            }
        }

        /**
         * Returns the index of the last extension separator character, which is a dot.
         *
         *
         * This method also checks that there is no directory separator after the last dot. To do this it uses
         * [.indexOfLastSeparator] which will handle a file in either Unix or Windows format.
         *
         *
         *
         * The output will be the same irrespective of the machine that the code is running on.
         *
         *
         * @param filename
         * the filename to find the last extension separator in, null returns -1
         * @return the index of the last extension separator character, or -1 if there is no such character
         */
        fun indexOfExtension(filename: String?): Int {
            if (filename == null) {
                return NOT_FOUND
            }
            val extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR)
            val lastSeparator = indexOfLastSeparator(filename)
            return if (lastSeparator > extensionPos) NOT_FOUND else extensionPos
        }

        /**
         * Returns the index of the last directory separator character.
         *
         *
         * This method will handle a file in either Unix or Windows format.
         * The position of the last forward or backslash is returned.
         *
         *
         * The output will be the same irrespective of the machine that the code is running on.
         *
         * @param filename  the filename to find the last path separator in, null returns -1
         * @return the index of the last separator character, or -1 if there
         * is no such character
         */
        fun indexOfLastSeparator(filename: String?): Int {
            if (filename == null) {
                return NOT_FOUND
            }
            val lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR)
            val lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR)
            return Math.max(lastUnixPos, lastWindowsPos)
        }
    }

    init {
        title = createTitle(context)
        changeTitle(context)
        val linearLayout = createMainLayout(context)
        linearLayout.addView(createBackItem(context))
        listView = createListView(context)
        linearLayout.addView(listView)
        setCustomTitle(title).setView(linearLayout)
            .setPositiveButton(R.string.button_ok
            ) { _, _ ->
                if (listener != null) {
                    if (selectedIndex > -1) {
                        listener?.onSelectedFile(
                            listView.getItemAtPosition(selectedIndex).toString()
                        )
                    } else {
                        listener?.onSelectedFile(currentPath)
                    }
                }
            }.setNegativeButton(R.string.cancel
            ) { dialogInterface, _ ->
                if (onCancelListener != null) onCancelListener?.onCancel(
                    dialogInterface
                )
            }
    }

    fun setDefaultDrawablesForFoldersAndFiles(context: Context) {
        fileIcon = context.getResources().getDrawable(R.drawable.ic_tactics_item_dark)
        folderIcon = context.getResources().getDrawable(R.drawable.ic_tactics_folder_dark)
    }
}