package com.example.accordionview

/**
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
data class AccordionViewModel(
    /**
     * The title of this model to display in the corresponding view.
     */
    val title: String,
    /**
     * The physical object that must be attached to this view model.
     * This property can be of any type.
     */
    val model: Any? = null,
    /**
     * The corresponding view, if this indicator is set, has the capability to expand and collapse.
     */
    var canCollapse: Boolean = true,
    /**
     * The current UI state of whether or not the corresponding view is expanded.
     */
    var isExpanded: Boolean = false,
    /**
     * The current UI state of whether or not the corresponding view is selected or checked.
     */
    var isSelected: Boolean = false,
    /**
     * The details of this model to display in the corresponding view.
     */
    val details: String? = null,
    /**
     * A secondary title of this model to display in the corresponding view.
     */
    val subTitle: String? = null,
    /**
     * Secondary details of this model to display in the corresponding view.
     */
    val subDetails: String? = null,
    /**
     * This displays the type of view to load into the accordion view
     * @see com.example.accordionview.AccordionViewModel.Type
     */
    val type: Type = Type.Text,
    /**
     * Applies to all views of type Color of the corresponding view.
     * This property will be used to set the background color of the image.
     * @see com.example.accordionview.AccordionViewModel.Type.Color
     */
    val backgroundColor: String = "#00FF0000",
    /**
     * Applies to all views of type Color of the corresponding view.
     * If this indicator is set, the image will consist of multiple colors.
     * The background property will be ignored.
     * @see com.example.accordionview.AccordionViewModel.Type.Color
     */
    val isMultiColored: Boolean = true,
    /**
     * The minimum price of the corresponding view.
     */
    var minPrice: Int = 100,
    /**
     * The maximum price of the corresponding view.
     */
    var maxPrice: Int = 1000,
    /**
     * The child view models of the corresponding view.
     */
    val children: List<AccordionViewModel> = listOf(),
) {

    enum class Type {
        /**
         * A category, consisting of a title, details, and a disclosure indicator.
         */
        Category,

        /**
         * A Check box view, consisting of a title, details, and a check box.
         */
        Checkbox,

        /**
         * A Check mark view, consisting of a title, details, and a check mark image.
         */
        Checkmark,

        /**
         * A Color, consisting of a title, details,
         * and an image containing a circle using the specified background color.
         * If the corresponding view is to be multi-colored,
         * then a multi-colored image is displayed instead.
         */
        Color,

        /**
         * A category, consisting of a title details, and a UP / DOWN arrow indicator.
         */
        Expandable,

        /**
         * A category, consisting of a title and details.
         */
        Header,

        /**
         * A category, consisting of a title and details. User interaction is disabled.
         */
        Label,

        /**
         * If this type is specified,
         * A range slider is displayed. Individual text fields are also provided
         * to allow the user to set the minimum and maximum prices respectively.
         * No title and details will be displayed.
         */
        Price,

        /**
         * A category, consisting of a title and details. This view is selectable.
         */
        Text,

        /**
         * A Toggle, consisting of a title, details, and an ON/OFF toggle button.
         */
        Toggle,

        /**
         * This item contains two columns of information in the same view.
         * Both columns will consists of a title and details.
         * Because this view is treated as a header,
         * alternating colors will not be applied to this view.
         */
        TwoColumnHeader,

        /**
         * This item contains two columns of information in the same view.
         * Both columns will consists of a title and details.
         * Alternating colors will be applied to this view.
         */
        TwoColumnDetails;

        companion object {
            fun valueOf(index: Int): Type {
                values().map {
                    if (index == it.ordinal) {
                        return it
                    }
                }

                return Text
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()

        sb.append("AccordionViewModel:\n")
        sb.append("\tcanCollapse=[$canCollapse]\n")
        sb.append("\tisExpanded=[$isExpanded]\n")
        sb.append("\tisSelected=[$isSelected]\n")
        sb.append("\ttitle=[$title]\n")
        sb.append("\tdetails=[$details]\n")
        sb.append("\ttype=[$type]\n")
        sb.append("\tbackgroundColor=[$backgroundColor]\n")
        sb.append("\tisMultiColored=[$isMultiColored]\n")
        sb.append("\tminPrice=[$minPrice]\n")
        sb.append("\tmaxPrice=[$maxPrice]\n")
        sb.append("\tchildren=[$children]\n")

        return sb.toString()
    }
}
