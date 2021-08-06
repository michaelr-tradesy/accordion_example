package com.example.accordionview

import java.lang.StringBuilder

/**
 * @name AccordionView
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our accordion view. For models in the list that are expandable,
 * this component provide the ability to dynamically expand and collapse sub-lists.
 */
data class AccordionViewModel(
    var canCollapse: Boolean = true,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false,
    val title: String? = null,
    val details: String? = null,
    val type: Type = Type.Text,
    val backgroundColor: String = "#00FF0000",
    val isMultiColored: Boolean = true,
    var minPrice: Int = 100,
    var maxPrice: Int = 1000,
    val children: List<AccordionViewModel> = listOf(),
) {

    enum class Type {
        Category,
        Checkmark,
        Color,
        Text,
        Expandable,
        Price,
        Toggle;

        companion object {
            fun valueOf(index: Int): Type {
                values().map {
                    if(index == it.ordinal) {
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
