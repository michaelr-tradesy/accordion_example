package com.example.accordionview

import android.view.View
import android.widget.TextView

internal class HeaderAccordionViewHolder(v: View) : DefaultAccordionViewHolder(v) {
    private val title: TextView = v.findViewById(R.id.title)

    /**
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The title and details are set
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        title.text = model?.title
    }

}
