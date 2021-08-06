package com.example.accordionview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @name AccordionView
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
class AccordionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ConstraintLayout(context, attrs, defStyle) {

    private var selAccordionViewModel: AccordionViewModel? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var accordionViewAdapter: AccordionViewAdapter
    private var list: MutableList<AccordionViewModel> = mutableListOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_accordion_view, this)

        loadViewAttributes(context, attrs, defStyle)
        bindView()
    }

    /**
     * @name onCreate
     * @author Coach Roebuck
     * @since 2.18
     * This method is to be called in correlation with the View::onCreate().
     */
    fun onCreate() {
        val linearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isNestedScrollingEnabled = false

        accordionViewAdapter = AccordionViewAdapter(list, ::onModelSelected)
        recyclerView.adapter = accordionViewAdapter
    }

    /**
     * @name onModelSelected
     * @author Coach Roebuck
     * @since 2.18
     * This method serves as the callback method to respond to the selected option in the list.
     * If the selected model is expandable, we will toggle between collapsing and expanding
     * the sublist of that category.
     * We will also save the most recent selection.
     */
    private fun onModelSelected(accordionViewModel: AccordionViewModel) {
        println("accordionViewModel=[$accordionViewModel]")
        selAccordionViewModel = accordionViewModel
        accordionViewModel.isExpanded = !accordionViewModel.isExpanded
        accordionViewAdapter.isAnimationEnabled = accordionViewModel.isExpanded

        val index = accordionViewAdapter.indexOf(accordionViewModel)
        val totalChildren = accordionViewModel.children.size

        if (accordionViewModel.canCollapse && accordionViewModel.isExpanded) {
            accordionViewAdapter.notifyItemRangeInserted(index + 1, totalChildren)
        } else if(accordionViewModel.canCollapse) {
            accordionViewAdapter.notifyItemRangeRemoved(index + 1, totalChildren)
        }
    }

    /**
     * @name onListChanged
     * @author Coach Roebuck
     * @since 2.18
     * This method replaces the existing list with a new one, then refreshes the view.
     * @param list A list of models
     */
    fun onListChanged(list: List<AccordionViewModel>) {
        this.list.clear()
        this.list.addAll(list)
        recyclerView.adapter?.notifyItemRangeChanged(0, list.size - 1)
    }

    /**
     * @name loadViewAttributes
     * @author Coach Roebuck
     * @since 2.18
     * This method retrieves the custom style attributes defined in order to set the recycler view.
     * @param context the context
     * @param attrs the set of attributes
     * @param defStyle the def style
     */
    private fun loadViewAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.AccordionView,
                0,
                defStyle
            )
        val accordionPadding = typedArray.getDimensionPixelSize(
            R.styleable.AccordionView_accordionPadding,
            0
        )
        println("accordionPadding=[$accordionPadding]")
        typedArray.recycle()
    }

    private fun bindView() {
        recyclerView = findViewById(R.id.recyclerView)
    }
}