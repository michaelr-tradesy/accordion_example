package com.example.accordionexample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.accordionview.AccordionView
import com.example.accordionview.AccordionViewModel
import org.apache.commons.lang3.RandomStringUtils
import kotlin.math.abs
import kotlin.random.Random


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

        accordionView.setCallback {
            println("Accordion View Model Selected: $it")
        }
        onAlphabeticalList()
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

        showToast(item.title.toString())

        when (item.itemId) {
            R.id.alphabetical_view -> {
                onAlphabeticalList()
            }
            R.id.chart_view -> {
                onChartList()
            }
            R.id.color_view -> {
                onColorList()
            }
            R.id.expandable_view -> {
                onExpandableList()
            }
            R.id.group_view -> {
                onGroupList()
            }
            R.id.two_column_view -> {
                onTwoColumnList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onTwoColumnList() {
        val list: MutableList<AccordionViewModel> = mutableListOf(
            AccordionViewModel(
                title = "US",
                type = AccordionViewModel.Type.TwoColumnHeader,
                subTitle = "EU",
            )
        )

        val rows: List<Pair<String, String>> = listOf(
            Pair("4.5 - 5", "35"),
            Pair("5.5 - 6", "36"),
            Pair("6.5 - 7", "37"),
            Pair("7.5 - 8", "38"),
            Pair("8.5 - 9", "39"),
            Pair("9.5 - 10", "40"),
            Pair("10.5 - 11", "41"),
            Pair("11.5 - 12", "42"),
            Pair("12.5 - 13", "43"),
        )
        rows.map { row ->
            list.add(
                AccordionViewModel(
                    title = row.first,
                    type = AccordionViewModel.Type.TwoColumnDetails,
                    subTitle = row.second,
                )
            )
        }

        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setTotalColumns(1)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(true)
        accordionView.onListChanged(list)
    }

    private fun onAlphabeticalList() {
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
    }

    private fun onExpandableList() {
        val size = abs(Random.nextInt()) % 100
        val list: MutableList<AccordionViewModel> = mutableListOf()
        val type = AccordionViewModel.Type.Expandable
        val types: MutableList<AccordionViewModel.Type> = mutableListOf(
            AccordionViewModel.Type.Category,
            AccordionViewModel.Type.Checkbox,
            AccordionViewModel.Type.Checkmark,
            AccordionViewModel.Type.Label,
            AccordionViewModel.Type.Price,
            AccordionViewModel.Type.Text,
            AccordionViewModel.Type.Toggle,
        )

        for (i in 1..size) {
            val totalChildren = abs(Random.nextInt()) % 10
            val children: MutableList<AccordionViewModel> = mutableListOf()

            for (j in 1..totalChildren) {
                val index = abs(Random.nextInt() % types.size)

                children.add(generateRandomModel(type = types[index]))
            }

            list.add(generateRandomModel(children = children, type = type))
        }

        accordionView.setTotalColumns(1)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        accordionView.onListChanged(list)

    }

    private fun onGroupList() {
        accordionView.setTotalColumns(1)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        val list = generateGroupRandomModels(abs(Random.nextInt()) % 100)
        accordionView.onListChanged(list)
    }

    private fun onColorList() {
        accordionView.setTotalColumns(3)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(false)
        val list: List<AccordionViewModel> = generateRandomColorModels(
            size = abs(Random.nextInt()) % 100
        )
        accordionView.onListChanged(list)
    }

    private fun onChartList() {
        val list = generateGroupRandomModels(abs(Random.nextInt()) % 100)
        accordionView.setAlphabeticalScrollingEnabled(false)
        accordionView.setIsAlternatingRowBackgroundColorsEnabled(true)
        accordionView.setTotalColumns(1)
        accordionView.onListChanged(list)
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