package com.example.accordionview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class AlphabetViewAdapter(private val alphabet: String, private val callback: (Char) -> Unit)
    : RecyclerView.Adapter<AlphabetViewAdapter.AlphabetViewHolder>(){

    class AlphabetViewHolder(v: View): RecyclerView.ViewHolder(v) {

        private val textView = v.findViewById<TextView>(android.R.id.text1)
        private var callback: ((Char) -> Unit)? = null
        private var value: Char = ' '

        init {
            v.isSoundEffectsEnabled = false
            v.setOnClickListener { onViewClicked() }
        }

        private fun onViewClicked() {
            callback?.invoke(value)
        }

        fun bind(value: Char, callback: (Char) -> Unit) {
            this.callback = callback
            this.value = value
            textView.text = value.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlphabetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.view_holder_alphabet_letter,
                parent,
                false
            )
        val height = parent.measuredHeight / itemCount

        view.minimumHeight = height
        view.layoutParams.height = height

        return AlphabetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlphabetViewHolder, position: Int) {
        val value = alphabet.toList()[position]
        holder.bind(value, callback)
    }

    override fun getItemCount(): Int {
        return alphabet.toList().size
    }

}
