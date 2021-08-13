package com.example.accordionview

import android.view.View
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView


/**
  * @author Coach Roebuck
 * @since 2.18
 * This component serves as the abstract view holder.
 * All view holders that subclass this component must contain a title and details in the view.
 * This component will set the title and details of the provided view.
 */
internal open class DefaultAccordionViewHolder
internal constructor(v: View) : RecyclerView.ViewHolder(v) {

    var isAnimationEnabled: Boolean = true
    private lateinit var callback: (model: AccordionViewModel) -> Unit

    protected var model: AccordionViewModel? = null
    protected var privateCallback: (() -> Unit)? = null

    init {
        v.setOnClickListener { onViewClicked() }
    }

    /**
          * @author Coach Roebuck
     * @since 2.18
     * The model and callback parameters are saved for future use.
     * "Fade-In" animation is also performed, if enabled on this component.
     * @param model the AccordionViewModel component
     * @param callback a callback block using the model as a parameter.
     */
    open fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        this.model = model
        this.callback = callback

        if (isAnimationEnabled && model?.children?.isEmpty() == true) {
            setAnimation()
        }
    }

    /**
          * @author Coach Roebuck
     * @since 2.18
     * This serves as the callback method for the onClick event.
     */
    protected fun onViewClicked() {
        model?.let {
            privateCallback?.invoke()
            callback.invoke(it)
        }
    }

    /**
          * @author Coach Roebuck
     * @since 2.18
     * Sets and starts a fade-in animation on this view.
     */
    private fun setAnimation() {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        itemView.startAnimation(anim)
    }

    /**
          * @author Coach Roebuck
     * @since 2.18
     * Clears the animation previously set on this view.
     */
    fun clearAnimation() {
        itemView.clearAnimation()
    }

}
