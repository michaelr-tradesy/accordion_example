package com.example.accordionview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
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
    private var callback: (AccordionViewModel) -> Unit = { _ -> }
    private var totalColumns: Int = 1

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_accordion_view, this)

        loadViewAttributes(context, attrs, defStyle)
        bindView()
        onCreate()
    }

    /**
     * @name onCreate
     * @author Coach Roebuck
     * @since 2.18
     * This method is to be called in correlation with the View::onCreate().
     */
    private fun onCreate() {
        setLayoutManager()
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isNestedScrollingEnabled = false

        accordionViewAdapter = AccordionViewAdapter(list, ::onModelSelected)
        recyclerView.adapter = accordionViewAdapter
    }

    private fun setLayoutManager() {
        val layoutManager = if (totalColumns > 1) {
            GridLayoutManager(this.context, totalColumns)
        } else {
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }

        recyclerView.layoutManager = layoutManager
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
        selAccordionViewModel = accordionViewModel
        accordionViewModel.isExpanded = !accordionViewModel.isExpanded
        accordionViewAdapter.isAnimationEnabled = accordionViewModel.isExpanded

        val index = accordionViewAdapter.indexOf(accordionViewModel)
        val totalChildren = accordionViewModel.children.size

        callback.invoke(accordionViewModel)

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
        recyclerView.adapter?.notifyDataSetChanged()
//        recyclerView.adapter?.notifyItemRangeChanged(0, list.size - 1)
    }

    /**
     * @name onListChanged
     * @author Coach Roebuck
     * @since 2.18
     * This serves as the setter for the callback property.
     * @param callback The callback block to invoke
     */
    fun setCallback(callback: (AccordionViewModel) -> Unit) {
        this.callback = callback
    }

    /**
     * @name onListChanged
     * @author Coach Roebuck
     * @since 2.18
     * Sets the total number of columns in each row.
     * @param value the total number of columns.
     * Catch 22: The layout manager of the recycler view MUST be GridLayoutManager.
     */
    fun setTotalColumns(value: Int) {
        this.totalColumns = value
        setLayoutManager()
    }

    /**
     * @name setIsAlternatingRowBackgroundColorsEnabled
     * @author Coach Roebuck
     * @since 2.18
     * Sets the ability to use alternating row background colors.
     * @param value true or false
     */
    fun setIsAlternatingRowBackgroundColorsEnabled(value: Boolean) {
        accordionViewAdapter.setIsAlternatingRowBackgroundColorsEnabled(value)
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
        val padding = typedArray.getDimensionPixelSize(
            R.styleable.AccordionView_padding,
            0
        )
        totalColumns = typedArray.getInteger(
            R.styleable.AccordionView_columns,
            1
        )

        typedArray.recycle()
    }

    private fun bindView() {
        recyclerView = findViewById(R.id.recyclerView)
    }

    fun refresh() {
        accordionViewAdapter.notifyDataSetChanged()
    }
}