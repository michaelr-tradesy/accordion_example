package com.example.accordionview

import android.view.View
import android.widget.ToggleButton

/**
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
internal class ToggleAccordionViewHolder(v: View) : TextAccordionViewHolder(v) {

    private val toggleButton: ToggleButton = v.findViewById(R.id.toggleButton)

    init {
        val myCallback: (() -> Unit) = {
            model?.let {
                toggleButton.toggle()
                it.isSelected = !it.isSelected
            }
        }
        privateCallback = myCallback
        toggleButton.setOnClickListener {
            privateCallback = null
            onViewClicked()
            privateCallback = myCallback
        }
    }

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The UI state of the toggle button is set
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        toggleButton.isSelected = model?.isSelected == true
    }
}