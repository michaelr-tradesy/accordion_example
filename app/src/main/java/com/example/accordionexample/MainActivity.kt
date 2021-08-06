package com.example.accordionexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.accordionview.AccordionView
import com.example.accordionview.AccordionViewModel
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var accordionView: AccordionView
    private val maxDepth = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accordionView = findViewById(R.id.accordionView)
        accordionView.onCreate()
    }

    override fun onStart() {
        super.onStart()

        val list: List<AccordionViewModel> = generateRandomModels(abs(Random.nextInt()) %100)
        accordionView.onListChanged(list)
    }

    private fun generateRandomModels(
        size: Int,
        level: Int = 0
    ): List<AccordionViewModel> {
        val canCollapse = level < maxDepth
        val list: MutableList<AccordionViewModel> = mutableListOf()
        val types = AccordionViewModel.Type.values()
            .toMutableList()

        types.remove(AccordionViewModel.Type.Expandable)

        for(i in 1..size) {
            val type = if(canCollapse) {
                AccordionViewModel.Type.Expandable
            } else {
                val index = Random.nextInt()%types.size
                AccordionViewModel.Type.valueOf(index)
            }
            val children: List<AccordionViewModel> = if(canCollapse) {
                generateRandomModels(abs(Random.nextInt()) %10, level + 1)
            } else { listOf() }

            val isMultiColored = if(type == AccordionViewModel.Type.Color) {
                Random.nextBoolean()
            } else false
            list.add(generateRandomModel(children, canCollapse, type, isMultiColored))
        }
        return list.toList()
    }

    private fun generateRandomModel(
        children: List<AccordionViewModel> = listOf(),
        canCollapse: Boolean,
        type: AccordionViewModel.Type,
        isMultiColored: Boolean
    ): AccordionViewModel {
        val title = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) %12 + 1)
        val details = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) %128)
        return AccordionViewModel(
            title = title,
            details = details,
            children = children,
            type = type,
            isMultiColored = isMultiColored,
            canCollapse = canCollapse
        )
    }
}