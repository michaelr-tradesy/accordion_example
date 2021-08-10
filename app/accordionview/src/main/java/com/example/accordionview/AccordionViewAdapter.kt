package com.example.accordionview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @name AccordionViewAdapter
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as the adapter for our Accordion View
 */
internal class AccordionViewAdapter(
    private val list: List<AccordionViewModel> = listOf(),
    private val callback: (AccordionViewModel) -> Unit = { _ -> }
) : RecyclerView.Adapter<DefaultAccordionViewHolder>() {

    private var isAlternatingRowBackgroundColorsEnabled: Boolean = false
    private var isCurrentlyAlternatingBackgroundColor = true

    /**
     * @name isAnimationEnabled
     * @author Coach Roebuck
     * @since 2.18
     * If this is enabled, animation will be performed at the time we bind to each view holder.
     */
    var isAnimationEnabled: Boolean = true

    /**
     * @name onCreateViewHolder
     * @author Coach Roebuck
     * @since 2.18
     * Binds the given View to the position.
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultAccordionViewHolder {
        val type = AccordionViewModel.Type.valueOf(viewType)
        val layout = when(type) {
            AccordionViewModel.Type.Category -> R.layout.view_holder_accordion_category
            AccordionViewModel.Type.Checkbox -> R.layout.view_holder_accordion_checkmark
            AccordionViewModel.Type.Color -> R.layout.view_holder_accordion_color
            AccordionViewModel.Type.Header -> R.layout.view_holder_accordion_header
            AccordionViewModel.Type.Expandable -> R.layout.view_holder_accordion_expandable
            AccordionViewModel.Type.Price -> R.layout.view_holder_accordion_price
            AccordionViewModel.Type.Text -> R.layout.view_holder_accordion_text
            AccordionViewModel.Type.Toggle -> R.layout.view_holder_accordion_toggle
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(
                layout,
                parent,
                false
            )

        return when(type) {
            AccordionViewModel.Type.Category -> CategoryAccordionViewHolder(view)
            AccordionViewModel.Type.Checkbox -> CheckboxAccordionViewHolder(view)
            AccordionViewModel.Type.Color -> ColorAccordionViewHolder(view)
            AccordionViewModel.Type.Text -> TextAccordionViewHolder(view)
            AccordionViewModel.Type.Header -> HeaderAccordionViewHolder(view)
            AccordionViewModel.Type.Expandable -> ExpandableAccordionViewHolder(view)
            AccordionViewModel.Type.Price -> PriceAccordionViewHolder(view)
            AccordionViewModel.Type.Toggle -> ToggleAccordionViewHolder(view)
        }
    }

    /**
     * @name onBindViewHolder
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

        println("position=[$position] type=[$type] " +
                "isAlternatingRowBackgroundColorsEnabled=[${this.isAlternatingRowBackgroundColorsEnabled}]" +
                " isCurrentlyAlternatingBackgroundColor=[${this.isCurrentlyAlternatingBackgroundColor}]")

        if(this.isAlternatingRowBackgroundColorsEnabled) {
            when (type) {
                AccordionViewModel.Type.Header -> {
                    this.isCurrentlyAlternatingBackgroundColor = false
                }
                AccordionViewModel.Type.Expandable -> {
                    this.isCurrentlyAlternatingBackgroundColor = true
                }
                else -> {
                    if(this.isCurrentlyAlternatingBackgroundColor) {
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.header_background_text_color))
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                    }
                    this.isCurrentlyAlternatingBackgroundColor = !this.isCurrentlyAlternatingBackgroundColor
                }
            }
        } else if(!(type == AccordionViewModel.Type.Header || type == AccordionViewModel.Type.Expandable)) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /**
     * @name onViewDetachedFromWindow
     * @author Coach Roebuck
     * @since 2.18
     * Called when a view created by this adapter has been detached from its window.
     * @param holder Holder of the view being detached
     */
    override fun onViewDetachedFromWindow(holder: DefaultAccordionViewHolder) {
        holder.clearAnimation()
    }

    /**
     * @name getItemCount
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
     * @name getItemViewType
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

    /**
     * @name setUsesAlternatingRowBackgroundColors
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
     * @name indexOf
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
            if(it == accordionViewModel) {
                return total
            }
            total += 1 + if (isExpanded(it)) {
                val pair = indexOfChild(accordionViewModel, it)
                indexfound = pair.second
                pair.first
            } else {
                0
            }

            if(indexfound > -1) {
                return indexfound + total
            }
        }

        return indexfound
    }

    /**
     * @name indexOfChild
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
            if(accordionViewModel == it) {
                indexFound = total
            }

            val pair = indexOfChild(accordionViewModel, it)
            total += 1 + pair.first
            indexFound = pair.second
        }

        return Pair(total, indexFound)
    }

    /**
     * @name getTotalItems
     * @author Coach Roebuck
     * @since 2.18
     * Count the total number of VISIBLE items.
     * @return the total number of VISIBLE items.
     */
    private fun getTotalItems(): Int {
        var total = 0

        list.map {
            total += 1 + if (isExpanded(it)) {
                totalChildren(it)
            } else {
                0
            }
        }

        return total
    }

    /**
     * @name totalChildren
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
     * @name getModel
     * @author Coach Roebuck
     * @since 2.18
     * Get the model at the specified position of VISIBLE items
     * @param position the current position in the list. This list is treated as a tree.
     * @return if the item is not out of bounds, the model is returned. Else, null is returned.
     */
    private fun getModel(position: Int): AccordionViewModel? {
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
     * @name getModelChild
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

    private fun isExpanded(model: AccordionViewModel): Boolean =
        model.isExpanded || model.type == AccordionViewModel.Type.Header
}
