package ua.shishkoam.appcoockingbook.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import ua.shishkoam.appcoockingbook.R
import ua.shishkoam.appcoockingbook.ui.features.dummy.DummyContent


/**
 * A fragment representing a list of Items.
 */
class RecyclerViewFragment : Fragment() {

    private var itemList = mutableListOf<DummyContent.DummyItem>()
//    private var itemClickListener: AdapterView.OnItemClickListener<>? = null
    private var diffUtil: RecyclerDiffUtil? = null

//    constructor()

//    constructor(listener: AdapterView.OnItemClickListener<>) {
//        itemClickListener = listener
//    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var columnCount = 1



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        viewManager = if (columnCount == 1) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 3)
        }
        val defaultItemAnimator: ItemAnimator = DefaultItemAnimator()


//        RecyclerView.ItemDecoration

        viewAdapter = RecyclerViewAdapter(DummyContent.ITEMS)

        recyclerView = view.findViewById(R.id.list) as RecyclerView
        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
            itemAnimator = defaultItemAnimator

        }
        diffUtil = RecyclerDiffUtil(DummyContent.ITEMS, DummyContent.ITEMS_UPDATE, object :
            ItemDiff<DummyContent.DummyItem> {
                override fun isSame(
                    oldItems: List<DummyContent.DummyItem>,
                    newItems: List<DummyContent.DummyItem>,
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return true
                }

                override fun isSameContent(
                    oldItems: List<DummyContent.DummyItem>,
                    newItems: List<DummyContent.DummyItem>,
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return false
                }
            })
        val diffResult = DiffUtil.calculateDiff(diffUtil!!)

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                DummyContent.ITEMS.removeAt(position)
                recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_vew_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = RecyclerViewAdapter(DummyContent.ITEMS)
            }
        }

//        notifyDataSetChanged() or notifyItemInserted(…) or notifyItemRangeChanged(…)
        return view
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            RecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}