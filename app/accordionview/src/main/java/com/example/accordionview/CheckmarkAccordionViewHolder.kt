package com.example.accordionview

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible

internal class CheckmarkAccordionViewHolder(v: View) : TextAccordionViewHolder(v) {

    private val checkmark: ImageView = v.findViewById(R.id.checkmark)

    init {
        privateCallback = {
            model?.let {
                setCheckmark(!it.isSelected)
                it.isSelected = !it.isSelected
            }
        }
        checkmark.setOnClickListener { onViewClicked() }
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The selected status is set on the checkmark
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        setCheckmark(model?.isSelected == true)
    }

    /**
     * @name setcheckmark
     * @author Coach Roebuck
     * @since 2.18
     * Set the is checked indicator of the checkmark.
     * @param isChecked True / False
     */
    private fun setCheckmark(isChecked: Boolean) {
        checkmark.isVisible = isChecked
    }
}