package com.example.accordionview

import android.view.View
import android.widget.TextView

internal class TwoColumnTextAccordionViewHolder(v: View) : DefaultAccordionViewHolder(v) {

    private val leftTitle: TextView = v.findViewById(R.id.leftTitle)
    private val rightTitle: TextView = v.findViewById(R.id.rightTitle)
    private val leftDetails: TextView = v.findViewById(R.id.leftDetails)
    private val rightDetails: TextView = v.findViewById(R.id.rightDetails)

    init {
        v.setOnClickListener { onViewClicked() }
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * Both sets of titles and details are set
     * after the corresponding parent method has been invoked.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)
        leftTitle.text = model?.title
        leftDetails.text = model?.details
        rightTitle.text = model?.subTitle
        rightDetails.text = model?.subDetails
    }
}
