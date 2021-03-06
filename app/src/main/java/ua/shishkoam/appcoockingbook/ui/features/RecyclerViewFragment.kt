package ua.shishkoam.appcoockingbook.ui.features

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import jp.wasabeef.recyclerview.animators.LandingAnimator
import ua.shishkoam.appcoockingbook.R
import ua.shishkoam.appcoockingbook.ui.features.dummy.DummyContent
import ua.shishkoam.appcoockingbook.ui.features.snap.FixedSnapHelper


/**
 * A fragment representing a list of Items.
 */
class RecyclerViewFragment : Fragment() {

    private var itemList = mutableListOf<DummyContent.DummyItem>()
//    private var itemClickListener: AdapterView.OnItemClickListener<>? = null

//    constructor()

//    constructor(listener: AdapterView.OnItemClickListener<>) {
//        itemClickListener = listener
//    }

    val handler = Handler()

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
        val defaultItemAnimator: ItemAnimator = LandingAnimator()


//        RecyclerView.ItemDecoration

        viewAdapter = RecyclerViewAdapter(DummyContent.ITEMS)

        recyclerView = view.findViewById(R.id.list) as RecyclerView

        val swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
            itemAnimator = defaultItemAnimator

            /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeDismissTouchCallback(ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                DemoItem demoItem = (DemoItem) viewHolder.itemView.getTag(R.id.demo_item_key);
                demoAdapter.removeItem(demoItem);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);*/


        }
        val snapHelper: SnapHelper = FixedSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

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

        swipeRefreshLayout.setOnRefreshListener {
            // Initialize a new Runnable
            val runnable = Runnable {
                // Update the text view text with a random number
                (viewAdapter as RecyclerViewAdapter).update(DummyContent.ITEMS_UPDATE)
                // Hide swipe to refresh icon animation
                swipeRefreshLayout.isRefreshing = false
            }

            // Execute the task after specified time
            handler.postDelayed(
                runnable, 3000.toLong()
            )
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