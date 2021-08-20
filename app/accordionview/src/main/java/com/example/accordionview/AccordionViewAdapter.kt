package com.example.accordionview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as the adapter for our Accordion View
 */
internal class AccordionViewAdapter(
    private val list: List<AccordionViewModel> = listOf(),
    private val callback: (AccordionViewModel) -> Unit = { _ -> }
) : RecyclerView.Adapter<DefaultAccordionViewHolder>(), SectionIndexer {

    // region Properties
    /**
     * @author Coach Roebuck
     * @since 2.18
     * If this is enabled, animation will be performed at the time we bind to each view holder.
     */
    var isAnimationEnabled: Boolean = true

    /**
     * @author Coach Roebuck
     * @since 2.18
     * If this is enabled, the background of every other view holder will be a different color.
     * That background color is predetermined inside the styles.
     */
    private var isAlternatingRowBackgroundColorsEnabled: Boolean = false

    /**
     * @author Coach Roebuck
     * @since 2.18
     * This indicator is controlled by this adapter, and discloses the current status
     * of the background color on the next view holder.
     */
    private var isCurrentlyAlternatingBackgroundColor = true

    /**
     * @author Coach Roebuck
     * @since 2.18
     * This is our alphabet section.
     * The key represents the letter in the alphabet and each group / section,
     * while the value represents the index in which that section / group starts.
     */
    private val sectionMap = hashMapOf<String, Int>()

    // endregion

    // region Override Methods

    // region RecyclerView.Adapter Override Methods

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Binds the given View to the position.
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * We will first inflate the appropriate layout for the specified view type.
     * Then we will instantiate the appropriate view holder.
     * It is possible for the same layout to be applied to the same view holder
     * (e.g. at the time of creation, the Two column text view holder).
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultAccordionViewHolder {
        val type = AccordionViewModel.Type.valueOf(viewType)
        val layout = getNextLayout(type)
        val view = LayoutInflater.from(parent.context)
            .inflate(
                layout,
                parent,
                false
            )

        return getNextViewHolder(type, view)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DefaultAccordionViewHolder, position: Int) {
        val model = getModel(position)
        holder.isAnimationEnabled = isAnimationEnabled
        holder.bind(model, callback)
        val type = model?.type

        setBackgroundForModelView(type, holder)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Called when a view created by this adapter has been detached from its window.
     * @param holder Holder of the view being detached
     */
    override fun onViewDetachedFromWindow(holder: DefaultAccordionViewHolder) {
        holder.clearAnimation()
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return getTotalItems()
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Returns the View type defined by the adapter.
     *
     * @param position the indexed position relative to the list
     * @return The type of the view assigned by the adapter.
     */
    override fun getItemViewType(position: Int): Int {
        val model = getModel(position)
        return model?.type?.ordinal ?: AccordionViewModel.Type.Text.ordinal
    }

    // endregion

    // region SectionIndexer Override Methods

    /**
     * Returns an array of objects representing sections of the list. The
     * returned array and its contents should be non-null.
     * <p>
     * The list view will call toString() on the objects to get the preview text
     * to display while scrolling. For example, an adapter may return an array
     * of Strings representing letters of the alphabet. Or, it may return an
     * array of objects whose toString() methods return their section titles.
     *
     * @return the array of section objects
     */
    override fun getSections(): Array<Any> {
        return sectionMap.keys.sorted().toTypedArray()
    }

    /**
     * Given the index of a section within the array of section objects, returns
     * the starting position of that section within the adapter.
     * <p>
     * If the section's starting position is outside of the adapter bounds, the
     * position must be clipped to fall within the size of the adapter.
     *
     * @param key the key relevant to the sections, when applicable.
     * @return the starting position of that section (identified by key) within the adapter,
     *         constrained to fall within the adapter bounds.
     *         If this key is not found,
     */
    fun getPositionForKey(key: String): Int {
        if (sectionMap.containsKey(key)) {
            sectionMap[key]?.let { return it } ?: return -1
        }
        return -1
    }

    /**
     * Given the index of a section within the array of section objects, returns
     * the starting position of that section within the adapter.
     * <p>
     * If the section's starting position is outside of the adapter bounds, the
     * position must be clipped to fall within the size of the adapter.
     *
     * @param sectionIndex the index of the section within the array of section
     *            objects
     * @return the starting position of that section within the adapter,
     *         constrained to fall within the adapter bounds
     */
    override fun getPositionForSection(sectionIndex: Int): Int {
        return if (sectionIndex > -1 && sectionIndex < sectionMap.size) {
            val key = sectionMap.keys.sorted().toList()[sectionIndex]
            sectionMap[key] ?: -1
        } else {
            -1
        }
    }

    /**
     * Given a position within the adapter, returns the index of the
     * corresponding section within the array of section objects.
     * <p>
     * If the section index is outside of the section array bounds, the index
     * must be clipped to fall within the size of the section array.
     * <p>
     * For example, consider an indexer where the section at array index 0
     * starts at adapter position 100. Calling this method with position 10,
     * which is before the first section, must return index 0.
     *
     * @param position the position within the adapter for which to return the
     *            corresponding section index
     * @return the index of the corresponding section within the array of
     *         section objects, constrained to fall within the array bounds
     */
    override fun getSectionForPosition(position: Int): Int {
        var output = -1
        sectionMap.map {
            if (position > it.value) {
                output++
            }
        }
        return output
    }

    // endregion

    // endregion

    // region Public Methods

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Sets the ability to use alternating row background colors.
     * @param value true or false
     */
    fun setIsAlternatingRowBackgroundColorsEnabled(value: Boolean) {
        this.isAlternatingRowBackgroundColorsEnabled = value
        this.isCurrentlyAlternatingBackgroundColor = this.isAlternatingRowBackgroundColorsEnabled
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Returns the index of the specified model, relative to the list.
     * @param accordionViewModel the specified AccordionViewModel
     * @return the index in which the model is found in the list.
     * If the model does not exist in the list, then a -1 is returned.
     */
    fun indexOf(accordionViewModel: AccordionViewModel): Int {
        var total = 0
        var indexfound = -1

        list.map {
            if (it == accordionViewModel) {
                return total
            }
            total += 1 + if (isExpanded(it)) {
                val pair = indexOfChild(accordionViewModel, it)
                indexfound = pair.second
                pair.first
            } else {
                0
            }

            if (indexfound > -1) {
                return indexfound + total
            }
        }

        return indexfound
    }

    // endregion

    // region Private Methods

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and retrieves the layout that is mapped to the specified type.
     * @param type This is the enumeration value
     * @return A numeric value representing a layout that is mapped to each type.
     */
    private fun getNextLayout(type: AccordionViewModel.Type) = when (type) {
        AccordionViewModel.Type.Category -> R.layout.view_holder_accordion_category
        AccordionViewModel.Type.Checkbox -> R.layout.view_holder_accordion_checkbox
        AccordionViewModel.Type.Checkmark -> R.layout.view_holder_accordion_checkmark
        AccordionViewModel.Type.Color -> R.layout.view_holder_accordion_color
        AccordionViewModel.Type.Header -> R.layout.view_holder_accordion_header
        AccordionViewModel.Type.Expandable -> R.layout.view_holder_accordion_expandable
        AccordionViewModel.Type.Label -> R.layout.view_holder_accordion_text
        AccordionViewModel.Type.Price -> R.layout.view_holder_accordion_price
        AccordionViewModel.Type.Text -> R.layout.view_holder_accordion_text
        AccordionViewModel.Type.Toggle -> R.layout.view_holder_accordion_toggle
        AccordionViewModel.Type.TwoColumnHeader -> R.layout.view_holder_accordion_two_column_header
        AccordionViewModel.Type.TwoColumnDetails -> R.layout.view_holder_accordion_two_column_details
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and retrieves the view holder that is mapped to the specified type.
     * @param type This is the enumeration value
     * @return An instantiated view holder that is mapped to each type.
     */
    private fun getNextViewHolder(
        type: AccordionViewModel.Type,
        view: View
    ) = when (type) {
        AccordionViewModel.Type.Category -> CategoryAccordionViewHolder(view)
        AccordionViewModel.Type.Checkbox -> CheckboxAccordionViewHolder(view)
        AccordionViewModel.Type.Checkmark -> CheckmarkAccordionViewHolder(view)
        AccordionViewModel.Type.Color -> ColorAccordionViewHolder(view)
        AccordionViewModel.Type.Expandable -> ExpandableAccordionViewHolder(view)
        AccordionViewModel.Type.Header -> HeaderAccordionViewHolder(view)
        AccordionViewModel.Type.Label -> LabelAccordionViewHolder(view)
        AccordionViewModel.Type.Price -> PriceAccordionViewHolder(view)
        AccordionViewModel.Type.Text -> TextAccordionViewHolder(view)
        AccordionViewModel.Type.Toggle -> ToggleAccordionViewHolder(view)
        AccordionViewModel.Type.TwoColumnHeader,
        AccordionViewModel.Type.TwoColumnDetails -> TwoColumnTextAccordionViewHolder(view)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and sets the background for the current view holder.
     * If isAlternatingRowBackgroundColorsEnabled has been CLEARED,
     * then the background of the specified view holder will be set to the default drawable resource
     * defined in the styles.
     * * If isAlternatingRowBackgroundColorsEnabled has been enabled, then we must refer to logic.
     * If the current view holder is a Header, then we LEAVE THE BACKGROUND ALONE. However,
     * we clear the isCurrentlyAlternatingBackgroundColor indicator.
     * We want and expect the very next row to use the default background.
     * * If the current view holder is either an Expandable or a TwoColumnHeader,
     * then we will also LEAVE THE BACKGROUND ALONE. However, this time
     * we SET the isCurrentlyAlternatingBackgroundColor indicator.
     * We want and expect the very next row to use the alternate background.
     * The background of all other types of view holders will be determined by the status of the
     * isCurrentlyAlternatingBackgroundColor indicator, which will be toggled between TRUE / FALSE
     * after setting the next background.
     * @param type This is the enumeration value
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     */
    private fun setBackgroundForModelView(
        type: AccordionViewModel.Type?,
        holder: DefaultAccordionViewHolder
    ) {
        if (this.isAlternatingRowBackgroundColorsEnabled
            && type != AccordionViewModel.Type.Color
        ) {
            when (type) {
                AccordionViewModel.Type.Header -> {
                    this.isCurrentlyAlternatingBackgroundColor = false
                }
                AccordionViewModel.Type.Expandable,
                AccordionViewModel.Type.TwoColumnHeader -> {
                    this.isCurrentlyAlternatingBackgroundColor = true
                }
                else -> {
                    if (this.isCurrentlyAlternatingBackgroundColor) {
                        holder.itemView.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.view_holder_alternate_background_2
                        )
                    } else {
                        holder.itemView.background = ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.view_holder_alternate_background_1
                        )
                    }
                    this.isCurrentlyAlternatingBackgroundColor =
                        !this.isCurrentlyAlternatingBackgroundColor
                }
            }
        } else if (canSetDefaultBackground(type)) {
            holder.itemView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.view_holder_background
            )
        }
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and determines whether the default background can be set
     * for the current type.
     * @param type This is the enumeration value
     * @return TRUE if condition is met; otherwise, FALSE.
     */
    private fun canSetDefaultBackground(type: AccordionViewModel.Type?): Boolean {
        return !(type == AccordionViewModel.Type.Header
                || type == AccordionViewModel.Type.Expandable
                || type == AccordionViewModel.Type.Color
                || type == AccordionViewModel.Type.TwoColumnHeader)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * @param accordionViewModel the specified AccordionViewModel that we are trying to find.
     * @param root this refers to the topmost item from the list we are using
     * @return the index in which the model is found in the list.
     * If the model does not exist in the list, then a -1 is returned.
     */
    private fun indexOfChild(
        accordionViewModel: AccordionViewModel,
        root: AccordionViewModel
    ): Pair<Int, Int> {
        var total = 0
        var indexFound = -1

        root.children.map {
            if (accordionViewModel == it) {
                indexFound = total
            }

            val pair = indexOfChild(accordionViewModel, it)
            total += 1 + pair.first
            indexFound = pair.second
        }

        return Pair(total, indexFound)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Count the total number of VISIBLE items.
     * @return the total number of VISIBLE items.
     */
    private fun getTotalItems(): Int {
        var total = 0

        sectionMap.clear()
        list.map {
            sectionMap[it.title.first().uppercaseChar().toString()] = total
            total += 1 + if (isExpanded(it)) {
                totalChildren(it)
            } else {
                0
            }
        }

        return total
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Count the total number of VISIBLE child items of the specified model.
     * @param accordionViewModel the topmost AccordionViewModel from the list we are currently using
     * @return the total number of VISIBLE items.
     */
    private fun totalChildren(accordionViewModel: AccordionViewModel): Int {
        var total = 0

        accordionViewModel.children.map {
            total += 1 + totalChildren(it)
        }

        return total
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Get the model at the specified position of VISIBLE items
     * @param position the current position in the list. This list is treated as a tree.
     * @return if the item is not out of bounds, the model is returned. Else, null is returned.
     */
    fun getModel(position: Int): AccordionViewModel? {
        var model: AccordionViewModel? = null
        var total = 0

        list.map {
            if (total == position) {
                model = it
                total += 1
            } else if (total < position && isExpanded(it)) {
                val pair: Pair<Int, AccordionViewModel?> = getModelChild(position, total + 1, it)
                total = pair.first
                model = pair.second
            } else {
                total += 1
            }
        }

        return model
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Get the model at the specified position of VISIBLE items
     * @param position the position in which to retrieve teh item
     * @param currentPosition the current position in the list. This list is treated as a tree.
     * @param parent the topmost AccordionViewModel from the list we are currently using
     * @return A pair of results.
     * The first item will represent the total number of VISIBLE children that the topmost item
     * contains.
     * THe second item will contain the model that has been found.
     * if the position is not out of bounds, then null is returned.
     */
    private fun getModelChild(
        position: Int,
        currentPosition: Int,
        parent: AccordionViewModel
    ): Pair<Int, AccordionViewModel?> {
        var model: AccordionViewModel? = null
        var total = currentPosition

        parent.children.map {
            if (total == position) {
                model = it
                total += 1
            } else if (total < position && isExpanded(it)) {
                total += 1
                val pair = getModelChild(position, total, it)
                total = pair.first
                model = pair.second
            } else {
                total += 1
            }
        }

        return Pair(total, model)
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Lives up to its name and determines the current expanded status of the specified model.
     * The model must either be a HEADER or its isExpanded status must be set
     * @param model The accordion view model
     * @return TRUE if condition is met; otherwise, FALSE.
     */
    private fun isExpanded(model: AccordionViewModel): Boolean =
        model.isExpanded || model.type == AccordionViewModel.Type.Header

    // endregion
}
