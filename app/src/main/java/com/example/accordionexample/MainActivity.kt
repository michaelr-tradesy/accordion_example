package com.example.accordionexample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.accordionview.AccordionView
import com.example.accordionview.AccordionViewModel
import kotlin.math.abs
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private enum class ViewType {
        Regular, Chart, Color, Alphabetical, Group,
    }

    private lateinit var accordionView: AccordionView
    private val maxDepth = 1
    private var currentView = ViewType.Regular
    private var isAlternativeColorsEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accordionView = findViewById(R.id.accordionView)
    }

    override fun onStart() {
        super.onStart()

        val list: List<AccordionViewModel> = generateRandomModels(abs(Random.nextInt()) %100)
        accordionView.setCallback {
            println("Accordion View Model Selected: $it")
        }
        accordionView.onListChanged(list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.regular_view -> {
                val list = generateRandomModels(abs(Random.nextInt()) %100)
                accordionView.setTotalColumns(1)
                accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
                accordionView.onListChanged(list)
                showToast(item.title.toString())
            }
            R.id.chart_view -> {
                accordionView.setIsAlternatingRowBackgroundColorsEnabled(isAlternativeColorsEnabled)
                isAlternativeColorsEnabled = !isAlternativeColorsEnabled
                accordionView.refresh()
                showToast(item.title.toString())
            }
            R.id.color_view -> {
                accordionView.setTotalColumns(3)
                accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
                val list: List<AccordionViewModel> = generateRandomModels(
                    size = abs(Random.nextInt()) %100,
                    preferredType = AccordionViewModel.Type.Color
                )
                accordionView.onListChanged(list)
                showToast(item.title.toString())
            }
            R.id.group_view -> {
                accordionView.setTotalColumns(1)
                accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
                val list = generateGroupRandomModels(abs(Random.nextInt()) %100)
                accordionView.onListChanged(list)
                showToast(item.title.toString())
            }
            R.id.alphabetical_view -> {
                accordionView.setTotalColumns(1)
                showToast(item.title.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

//    override fun setSupportActionBar(toolbar: Toolbar?) {
//        super.setSupportActionBar(toolbar)
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(true)
//        toolbar?.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        findViewById<Toolbar>(R.id.toolbar).apply {
//            setSupportActionBar(this)
//        }
//    }

    private fun generateGroupRandomModels(size: Int): List<AccordionViewModel> {
        val list: MutableList<AccordionViewModel> = mutableListOf()

        for(i in 1..size) {
            val type = AccordionViewModel.Type.Header
            val children: List<AccordionViewModel> = generateRandomModels(abs(Random.nextInt()) %10, 1)

            list.add(generateRandomModel(children = children, type = type))
        }
        return list.toList()

    }

    private fun generateRandomModels(
        size: Int,
        level: Int = 0,
        preferredType: AccordionViewModel.Type? = null
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
                preferredType ?: run {
                    val index = Random.nextInt()%types.size
                    AccordionViewModel.Type.valueOf(index)
                }
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
        canCollapse: Boolean = false,
        type: AccordionViewModel.Type,
        isMultiColored: Boolean = false
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