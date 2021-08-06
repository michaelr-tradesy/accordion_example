package com.example.accordionview

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat

/**
 * @name ExpandableAccordionViewHolder
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
class ExpandableAccordionViewHolder(v: View) : TextAccordionViewHolder(v) {

    private val disclosure: ImageView = v.findViewById(R.id.disclosure)

    init {
        privateCallback = {
            model?.let {
                if (model?.children?.isNotEmpty() == true) {
                    setDisclosure(!it.isExpanded, it.canCollapse)
                }
            }
        }
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The UI state of the disclosure icon is set
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        setDisclosure(
            false,
            model?.canCollapse == true && model.children.isNotEmpty()
        )
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The UI state of the disclosure icon is set
     * after the corresponding parent method has been invoked.
     * @param isExpanded if set, will load the down arrow image.
     * Otherwise, the up arrow image will be loaded
     * @param canCollapse if set, will hide the disclosure.
     */
    private fun setDisclosure(isExpanded: Boolean, canCollapse: Boolean) {
        val resourceId = if (isExpanded) {
            android.R.drawable.arrow_down_float
        } else {
            android.R.drawable.arrow_up_float
        }
        disclosure.background = ContextCompat.getDrawable(itemView.context, resourceId)
        disclosure.visibility = if (canCollapse) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}