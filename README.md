# Accordion Example
This is a sample project that illustrates the application behavior of the Accordion View for the Android platform.
## Types of Views
These types of views, unless specified otherwise, display a title and details.
* Category: This represents a Category.
* Checkmark: This represents a Checkmark.
* Color: This represents a Color.
* Text: This represents a Text.
* Expandable: This represents a Expandable.
* Price: This represents the Price Range view. **_This view does not a title and details._**
* Toggle: This represents a Toggle.

## Example
To add the accordion to your project, it is suggested to first add a instance of this component to the layout. For example:
```javascript
 <com.example.accordionview.AccordionView
        android:id="@+id/accordionView"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
Configuring the accordion view with a list of objects to display and the callback method is easy. All that is required is to invoke the ```setCallback()``` and ```onListChanged()``` methods of the accordion view. For example:
```kotlin
 private lateinit var accordionView: AccordionView
 ...
 override fun onCreate(savedInstanceState: Bundle?) {
        val list: List<AccordionViewModel> = generateRandomModels(abs(Random.nextInt()) %100)

        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)
        accordionView = findViewById(R.id.accordionView)
        accordionView.setCallback { println("Accordion View Model Selected: $it") }
        accordionView.onListChanged(list)
    }
```
## AccordionViewModel
These are the available properties of the ```AccordionViewModel```:
* **model**: The physical object that must be attached to this view model. This property can be of any type.
* **canCollapse**: The corresponding view, if this indicator is set, has the capability to expand and collapse.
* **isExpanded**: The current UI state of whether or not the corresponding view is expanded.
* **isSelected**: The current UI state of whether or not the corresponding view is selected or checked.
* **title**: The title of this model to display in the corresponding view.
* **details**: The details of this model to display in the corresponding view.
* **type**: This displays the type of view to load into the accordion view.
* **backgroundColor**: Applies to all views of type Color of the corresponding view. This property will be used to set the background color of the image.
* **isMultiColored**: Applies to all views of type Color of the corresponding view. If this indicator is set, the image will consist of multiple colors. The background property will be ignored.
* **minPrice**: The minimum price of the corresponding view.
* **maxPrice**: The maximum price of the corresponding view.
* **children**: The child view models of the corresponding view.

## AccordionViewModel Types
These are the available types of ```AccordionViewModel```:
* **Category**: A category, consisting of a title, details, and a disclosure indicator.
* **Checkbox**: A Check box view, consisting of a title, details, and a check box.
* **Color**: A Color, consisting of a title, details, and an image containing a circle using the specified background color. If the corresponding view is to be multi-colored, then a multi-colored image is displayed instead.
* **Text**: A category, consisting of a title and details.
* **Expandable**: A category, consisting of a title details, and a UP / DOWN arrow indicator.
* **Price**: If this type is specified, A range slider is displayed. Individual text fields are also provided to allow the user to set the minimum and maximum prices respectively. No title and details will be displayed.
* **Toggle**: A Toggle, consisting of a title, details, and an ON/OFF toggle button.
