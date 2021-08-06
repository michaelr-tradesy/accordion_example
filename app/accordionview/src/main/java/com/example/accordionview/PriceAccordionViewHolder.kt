package com.example.accordionview

import android.view.View
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.*

/**
 * @name AccordionView
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
internal class PriceAccordionViewHolder(v: View) : DefaultAccordionViewHolder(v) {

    /**
     * This indicator instructs the text watcher to ignore changes in the value if set.
     */
    private var isRangeSliderUpdating: Boolean = false
    private var minPrice: TextInputEditText = v.findViewById(R.id.minPrice)
    private var maxPrice: TextInputEditText = v.findViewById(R.id.maxPrice)
    private var rangeSlider: RangeSlider = v.findViewById(R.id.rangeSlider)

    init {
        val textWatcher = AccordionTextWatcher(actionBlock = {
            if(!isRangeSliderUpdating) {
            updateRangeSlider()
            }
        })
        rangeSlider = v.findViewById(R.id.rangeSlider)
        minPrice.addTextChangedListener(textWatcher)
        maxPrice.addTextChangedListener(textWatcher)
    }

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * Bind the view with the contents of the specified model.
     * The title and details are set on the view.
     * "Fade-In" animation is also performed, if enabled on this component.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        var min = 1f
        var max = 10000f

        super.bind(model, callback)

        model?.minPrice?.let {
            minPrice.setText(it.toString())
            min = minPrice.text.toString().toFloat()
        }
        model?.maxPrice?.let {
            maxPrice.setText(it.toString())
            max = maxPrice.text.toString().toFloat()
        }

        rangeSlider.setValues(min, max)
        rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        rangeSlider.addOnChangeListener { _, _, fromUser ->
            this.isRangeSliderUpdating = fromUser
            if (fromUser) {
                updateRangeText()
                onViewClicked()
            }
            this.isRangeSliderUpdating = false
        }
        rangeSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("USD")
            format.format(value.toDouble())
        }
    }

    private fun updateRangeText() {
        model?.minPrice = rangeSlider.values[0].toInt()
        model?.maxPrice = rangeSlider.values[1].toInt()
        model?.minPrice?.let { minPrice.setText(it.toString()) }
        model?.maxPrice?.let { maxPrice.setText(it.toString()) }
    }

    private fun updateRangeSlider() {
        var min = model?.minPrice
        var max = model?.maxPrice
        var mustNotify = false
        try {
            val value = minPrice.text.toString().toInt()
            if (isValueWithinRange(value)
                && value <= rangeSlider.values[1]) {
                min = value
                mustNotify = true
            }
        } catch (t: Throwable) {

        }
        try {
            val value = maxPrice.text.toString().toInt()
            if (isValueWithinRange(value)
                && value >= rangeSlider.values[0]) {
                max = value
                mustNotify = true
            }
        } catch (t: Throwable) {

        }
        min?.let { minimum ->
            max?.let { maximum ->
                if (minimum > maximum) {
                    min = maximum
                    max = minimum
                }
            }
        }

        if (mustNotify) {
            rangeSlider.setValues(min?.toFloat(), max?.toFloat())
            min?.let { model?.minPrice = it }
            max?.let { model?.maxPrice = it }
            onViewClicked()
        }
    }

    private fun isValueWithinRange(value: Int) =
        value >= rangeSlider.valueFrom && value <= rangeSlider.valueTo
}
