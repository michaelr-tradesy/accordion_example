package com.example.accordionview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

    // region Properties

    private lateinit var dynamicFastScroller: DynamicFastScroller
    private lateinit var accordionViewAdapter: AccordionViewAdapter
    private lateinit var alphabetViewAdapter: AlphabetViewAdapter
    private lateinit var currentLetterImageView: ImageView
    private lateinit var currentTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var alphabetRecyclerView: RecyclerView
    private var callback: (AccordionViewModel) -> Unit = { _ -> }
    private var isAlphabeticalScrollingEnabled = false
    private var isFastScrollingEnabled = false
    private var list: MutableList<AccordionViewModel> = mutableListOf()
    private var selAccordionViewModel: AccordionViewModel? = null
    private var totalColumns: Int = 1

    // endregion

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_accordion_view, this)
        bindView()
        instantiateDependencies()
        loadViewAttributes(context, attrs, defStyle)
        initAttributesForContentList()
        initAttributesForAlphabetList()
    }

    // region Public Methods

    /**
     * @name refresh
     * @author Coach Roebuck
     * @since 2.18
     * This method lives up to its name, and notifies the adapter that the data set has changed.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        accordionViewAdapter.notifyDataSetChanged()
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
        refresh()
    }

    /**
     * @name setCallback
     * @author Coach Roebuck
     * @since 2.18
     * This serves as the setter for the callback property.
     * @param callback The callback block to invoke
     */
    fun setCallback(callback: (AccordionViewModel) -> Unit) {
        this.callback = callback
    }

    /**
     * @name setTotalColumns
     * @author Coach Roebuck
     * @since 2.18
     * Sets the total number of columns in each row.
     * @param value the total number of columns.
     * Catch 22: The layout manager of the recycler view MUST be GridLayoutManager.
     */
    fun setTotalColumns(value: Int) {
        this.totalColumns = value
        setLayoutManagerForContentList()
    }

    /**
     * @name setAlphabeticalScrollingEnabled
     * @author Coach Roebuck
     * @since 2.18
     * If set, will enable alphabetical scrolling.
     * @param value If set, will enable alphabetical scrolling.
     */
    fun setAlphabeticalScrollingEnabled(value: Boolean) {
        this.isAlphabeticalScrollingEnabled = value
        this.alphabetRecyclerView.isVisible = value
        this.currentTextView.isVisible = false
        this.currentLetterImageView.isVisible = false
    }

    /**
     * @name setAlphabeticalScrollingEnabled
     * @author Coach Roebuck
     * @since 2.18
     * If set, will enable fast scrolling through the list.
     * @param value If set, will enable alphabetical scrolling.
     */
    @SuppressLint("VisibleForTests")
    fun setFastScrollingEnabled(value: Boolean) {
        this.isFastScrollingEnabled = value
        if (value) {
            dynamicFastScroller.attachToRecyclerView(recyclerView)
        } else {
            dynamicFastScroller.detachFromRecyclerView()
        }
    }

    /**
     * @name setIsAlternatingRowBackgroundColorsEnabled
     * @author Coach Roebuck
     * @since 2.18
     * Sets the ability to use alternating row background colors.
     * The adapter will be notified that its data set has changed.
     * @param value true or false
     */
    fun setIsAlternatingRowBackgroundColorsEnabled(value: Boolean) {
        accordionViewAdapter.setIsAlternatingRowBackgroundColorsEnabled(value)
        accordionViewAdapter.notifyItemRangeChanged(
            0,
            accordionViewAdapter.itemCount - 1
        )
    }

    // endregion

    // region Private Methods

    /**
     * @name bindView
     * @author Coach Roebuck
     * @since 2.18
     * Binds this component to the corresponding layout, that should have already been inflated.
     */
    private fun bindView() {
        recyclerView = findViewById(R.id.recyclerView)
        alphabetRecyclerView = findViewById(R.id.alphabetRecyclerView)
        currentLetterImageView = findViewById(R.id.currentLetterImageView)
        currentTextView = findViewById(R.id.currentTextView)
    }

    /**
     * @name instantiateDependencies
     * @author Coach Roebuck
     * @since 2.18
     * All dependencies of this component will be instantiated here.
     */
    private fun instantiateDependencies() {
        instantiateAlphabetAdapter()
        instantiateContentAdapter()
        initDynamicFastScrolling()
    }

    /**
     * @name instantiateListAdapter
     * @author Coach Roebuck
     * @since 2.18
     * Instantiates an instance of teh Alphabet View Adapter
     */
    private fun instantiateAlphabetAdapter() {
        alphabetViewAdapter =
            AlphabetViewAdapter(context.getString(R.string.alphabet), ::onLetterSelected)
        alphabetRecyclerView.adapter = alphabetViewAdapter
    }

    /**
     * @name instantiateContentAdapter
     * @author Coach Roebuck
     * @since 2.18
     * Instantiates an instance of teh "Content" View Adapter
     */
    private fun instantiateContentAdapter() {
        accordionViewAdapter = AccordionViewAdapter(list, ::onModelSelected)
        recyclerView.adapter = accordionViewAdapter
    }

    /**
     * @name instantiateContentAdapter
     * @author Coach Roebuck
     * @since 2.18
     * This method lives up to its name and instantiates and instance
     * of the fast scrolling component.
     * We are allowed to dynamically enable and disable this capability;
     * hence, the name "Dynamic Fast Scrolling."
     */
    @SuppressLint("VisibleForTests")
    private fun initDynamicFastScrolling() {
        val verticalThumbDrawable =
            ContextCompat.getDrawable(this.context, R.drawable.thumb_drawable) as StateListDrawable
        val verticalTrackDrawable =
            ContextCompat.getDrawable(this.context, R.drawable.line_drawable)
        val horizontalThumbDrawable =
            ContextCompat.getDrawable(this.context, R.drawable.thumb_drawable) as StateListDrawable
        val horizontalTrackDrawable =
            ContextCompat.getDrawable(this.context, R.drawable.thumb_drawable)
        val resources = context.resources

        verticalTrackDrawable?.let { vTrackDrawable ->
            horizontalTrackDrawable?.let { hTrackDrawable ->
                dynamicFastScroller = DynamicFastScroller(
                    recyclerView,
                    verticalThumbDrawable,
                    vTrackDrawable,
                    horizontalThumbDrawable,
                    hTrackDrawable,
                    resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness),
                    resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range),
                    resources.getDimensionPixelOffset(R.dimen.fastscroll_margin)
                )
            }
        }
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
        setTotalColumns(
            typedArray.getInteger(
                R.styleable.AccordionView_columns,
                1
            )
        )
        setAlphabeticalScrollingEnabled(
            typedArray.getBoolean(
                R.styleable.AccordionView_alphabeticalScrollingEnabled,
                false
            )
        )
        setFastScrollingEnabled(
            typedArray.getBoolean(
                R.styleable.AccordionView_isFastScrollingEnabled,
                false
            )
        )

        typedArray.recycle()
    }

    /**
     * @name onCreate
     * @author Coach Roebuck
     * @since 2.18
     * This method lives up to its name and sets the attributes for the "content" list.
     */
    private fun initAttributesForContentList() {
        setLayoutManagerForContentList()
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isNestedScrollingEnabled = false
    }

    /**
     * @name setLayoutManagerForContentList
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and sets the layout manager using the value of the total columns field.
     */
    private fun setLayoutManagerForContentList() {
        val layoutManager = if (totalColumns > 1) {
            GridLayoutManager(this.context, totalColumns)
        } else {
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }

        recyclerView.layoutManager = layoutManager
        alphabetRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        refresh()
    }

    /**
     * @name initAttributesForAlphabetList
     * @author Coach Roebuck
     * @since 2.18
     * This method lives up to its name and sets the attributes for the alphabet list.
     */
    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    private fun initAttributesForAlphabetList() {
        alphabetRecyclerView.setHasFixedSize(true)
        alphabetRecyclerView.setItemViewCacheSize(20)
        alphabetRecyclerView.isNestedScrollingEnabled = false

        alphabetViewAdapter.notifyDataSetChanged()

        alphabetRecyclerView.setOnTouchListener { _, event ->
            alphabetListTouched(event)
            true
        }
        alphabetRecyclerView.setOnGenericMotionListener { _, event ->
            alphabetListTouched(event)
            true
        }
    }

    /**
     * @name initAttributesForAlphabetList
     * @author Coach Roebuck
     * @since 2.18
     * This method will highlight the letter of the alphabet that is currently hovered on.
     * The current letter will be displayed in a pretty background as well,
     * and will follow the finger vertically.
     */
    private fun alphabetListTouched(event: MotionEvent) {
        val linearLayoutManager: LinearLayoutManager =
            alphabetRecyclerView.layoutManager as LinearLayoutManager

        if (event.action == MotionEvent.ACTION_DOWN
            || event.action == MotionEvent.ACTION_MOVE
            || event.action == MotionEvent.ACTION_HOVER_MOVE
        ) {
            for (i in 0 until linearLayoutManager.childCount) {
                linearLayoutManager.findViewByPosition(i)?.let { childView ->
                    val l = IntArray(2)
                    childView.getLocationInWindow(l)
                    val x: Int = l[0]
                    val y: Int = l[1]
                    val w: Int = childView.width
                    val h: Int = childView.height
                    val rx = event.rawX
                    val ry = event.rawY

                    if (rx < x || rx > x + w || ry < y || ry > y + h) {
                        childView.setBackgroundColor(Color.TRANSPARENT)
                    } else {
                        val bias =
                            kotlin.math.abs(
                                childView.top.toFloat()
                                        / linearLayoutManager.height.toFloat()
                            )
                        childView.setBackgroundColor(Color.parseColor("#FFC0C0C0"))
                        childView.performClick()
                        val params = currentLetterImageView.layoutParams as LayoutParams
                        params.verticalBias = bias
                        currentLetterImageView.layoutParams = params
                        currentLetterImageView.isVisible = true
                        currentTextView.isVisible = true
                    }
                }
            }
        } else if (event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_CANCEL
        ) {
            currentLetterImageView.isVisible = false
            currentTextView.isVisible = false
            for (i in 0 until linearLayoutManager.childCount) {
                val childView = linearLayoutManager.findViewByPosition(i)
                childView?.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    /**
     * @name onLetterSelected
     * @author Coach Roebuck
     * @since 2.18
     * The current letter will be set. Whether or not it will be displayed will depend on
     * the visibility status of the alphabet list view.
     */
    private fun onLetterSelected(c: Char) {
        val key = "$c"
        currentTextView.text = key
        scrollToAlphabeticSection(key)
    }

    /**
     * @name scrollToAlphabeticSection
     * @author Coach Roebuck
     * @since 2.18
     * If alphabetical scrolling is enabled,
     * then we are to scroll the "content" recycler view to the beginning of the specified section.
     */
    private fun scrollToAlphabeticSection(key: String) {
        if (isAlphabeticalScrollingEnabled) {
            val position = accordionViewAdapter.getPositionForKey(key)
            recyclerView.scrollToPosition(position)
        }
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
        } else if (accordionViewModel.canCollapse) {
            accordionViewAdapter.notifyItemRangeRemoved(index + 1, totalChildren)
        }
    }

    // endregion
}