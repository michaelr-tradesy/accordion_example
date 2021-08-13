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
import org.apache.commons.lang3.RandomStringUtils


class MainActivity : AppCompatActivity() {

    private lateinit var accordionView: AccordionView
    private val maxDepth = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accordionView = findViewById(R.id.accordionView)
    }

    override fun onStart() {
        super.onStart()

        val list: List<AccordionViewModel> = generateRandomModels(
            abs(Random.nextInt()) % 100
        )
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
                onGenerateRegularList(item)
            }
            R.id.chart_view -> {
                onChartList(item)
            }
            R.id.color_view -> {
                onColorList(item)
            }
            R.id.group_view -> {
                onGroupList(item)
            }
            R.id.alphabetical_view -> {
                onAlphabeticalList(item)
            }
            R.id.two_column_view -> {
                onTwoColumnList(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onTwoColumnList(item: MenuItem) {
        val size: Int = abs(Random.nextInt()) % 100
        val list: MutableList<AccordionViewModel> = mutableListOf(
            generateRandomModel(type = AccordionViewModel.Type.TwoColumnHeader)
        )

        for (i in 1..size) {
            list.add(generateRandomModel(type = AccordionViewModel.Type.TwoColumnDetails))
        }

        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setTotalColumns(1)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(true)
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun onAlphabeticalList(item: MenuItem) {
        val list: MutableList<AccordionViewModel> = mutableListOf()
        val alphabet = getString(R.string.alphabet).toCharArray()

        alphabet.map {
            val children: List<AccordionViewModel> =
                generateRandomModels(
                    size = abs(Random.nextInt()) % 10,
                    level = 1,
                    preferredType = AccordionViewModel.Type.Text
                )

            list.add(
                AccordionViewModel(
                    title = it.toString(),
                    children = children,
                    type = AccordionViewModel.Type.Header
                )
            )
        }
        accordionView.setAlphabeticalScrollingEnabled(true)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(true)
        accordionView.setTotalColumns(1)
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun onGroupList(item: MenuItem) {
        accordionView.setTotalColumns(1)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        val list = generateGroupRandomModels(abs(Random.nextInt()) % 100)
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun onColorList(item: MenuItem) {
        accordionView.setTotalColumns(3)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        val list: List<AccordionViewModel> = generateRandomColorModels(
            size = abs(Random.nextInt()) % 100
        )
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun onChartList(item: MenuItem) {
        val list = generateGroupRandomModels(abs(Random.nextInt()) % 100)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(true)
        accordionView.setTotalColumns(1)
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun onGenerateRegularList(item: MenuItem) {
        val list = generateRandomModels(abs(Random.nextInt()) % 100)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setTotalColumns(1)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        accordionView.onListChanged(list)
        showToast(item.title.toString())
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun generateRandomColorModels(size: Int): List<AccordionViewModel> {
        val list: MutableList<AccordionViewModel> = mutableListOf()

        for (i in 1..size) {
            val type = AccordionViewModel.Type.Color
            val isMultiColored = if (type == AccordionViewModel.Type.Color) {
                Random.nextBoolean()
            } else false
            list.add(generateRandomModel(type = type, isMultiColored = isMultiColored))
        }
        return list.toList()
    }

    private fun generateGroupRandomModels(size: Int): List<AccordionViewModel> {
        val list: MutableList<AccordionViewModel> = mutableListOf()

        for (i in 1..size) {
            val type = AccordionViewModel.Type.Header
            val children: List<AccordionViewModel> =
                generateRandomModels(
                    size = abs(Random.nextInt()) % 10,
                    level = 1
                )

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
        val types: MutableList<AccordionViewModel.Type> = mutableListOf(
            AccordionViewModel.Type.Category,
            AccordionViewModel.Type.Checkbox,
            AccordionViewModel.Type.Checkmark,
            AccordionViewModel.Type.Price,
            AccordionViewModel.Type.Text,
            AccordionViewModel.Type.Toggle,
            AccordionViewModel.Type.TwoColumnDetails
        )

        for (i in 1..size) {
            val type = if (canCollapse) {
                AccordionViewModel.Type.Expandable
            } else {
                preferredType ?: run {
                    val index = abs(Random.nextInt() % types.size)
                    types[index]
                }
            }
            val children: List<AccordionViewModel> = if (canCollapse) {
                generateRandomModels(
                    size = abs(Random.nextInt()) % 10,
                    level = level + 1
                )
            } else {
                listOf()
            }

            val isMultiColored = if (type == AccordionViewModel.Type.Color) {
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
        isMultiColored: Boolean = false,
        titlePrefix: String? = "",
        detailsPrefix: String? = "",
        subTitlePrefix: String? = "",
        subDetailsPrefix: String? = "",
    ): AccordionViewModel {
        val title = titlePrefix +
            RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) % 12 + 1)
        val details = detailsPrefix +
            RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) % 128)
        val subTitle = subTitlePrefix +
            RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) % 12 + 1)
        val subDetails = subDetailsPrefix +
            RandomStringUtils.randomAlphabetic(abs(Random.nextInt()) % 128)
        return AccordionViewModel(
            title = title,
            details = details,
            children = children,
            type = type,
            isMultiColored = isMultiColored,
            canCollapse = canCollapse,
            subTitle = subTitle,
            subDetails = subDetails,
        )
    }
}