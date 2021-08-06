package com.example.accordionview

import android.text.Editable
import android.text.TextWatcher

/**
 * @name AccordionTextWatcher
 * @author Coach Roebuck
 * @since 2.18
 * This component serves as our text watcher for changes to text from within the accordion view.
 */
internal class AccordionTextWatcher(
    private var actionBlock: (() -> Unit),
    private var preBlock: (() -> Unit)? = null,
    private var postBlock: (() -> Unit)? = null
) : TextWatcher {

    override fun afterTextChanged(p0: Editable?) {
        preBlock?.let { it() }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        postBlock?.let { it() }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        actionBlock()
    }
}