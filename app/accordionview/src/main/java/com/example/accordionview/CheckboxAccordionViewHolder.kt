package com.example.accordionview

import android.view.View
import android.widget.CheckBox

/**
 * @name AccordionView
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
internal class CheckboxAccordionViewHolder(v: View) : TextAccordionViewHolder(v) {

    private val checkbox: CheckBox = v.findViewById(R.id.checkbox)

    init {
        privateCallback = {
            model?.let {
                setCheckbox(!it.isSelected)
                it.isSelected = !it.isSelected
            }
        }
        checkbox.setOnClickListener { onViewClicked() }
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The selected status is set on the checkbox
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        setCheckbox(model?.isSelected == true)
    }

    /**
     * @name setCheckbox
     * @author Coach Roebuck
     * @since 2.18
     * Set the is checked indicator of the checkbox.
     * @param isChecked True / False
     */
    private fun setCheckbox(isChecked: Boolean) {
        checkbox.isChecked = isChecked
    }
}