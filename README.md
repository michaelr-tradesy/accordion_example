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
## AccordionView
```
AccordionView : ConstraintLayout
```
Provides a recycler view with row collapsing and expansion capabilities. 
Provides several types of child views to represent each row.

### Parameters
None

### Constructors
#### <init>
Provides a skeletal implementation of this component.
```kotlin
AccordionView(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
```

### Functions
#### refresh
Sends a notification to refresh the UI. This should be invoked when the entire list has been replaced.
```kotlin
open fun refresh() : Unit
```
#### onListChanged
Replaces any existing list with a new list, which will trigger a refresh notification to this component.
```kotlin
fun onListChanged(list: List<AccordionViewModel>)
```
#### setCallback
Provides a callback block of code to be invoked in response to changes in information within this component.
```kotlin
fun setCallback(callback: (AccordionViewModel) -> Unit)
```
#### setTotalColumns
Sets the total number of columns for each row that is rendered inside this component.
```kotlin
fun setTotalColumns(value: Int)
```
#### setAlphabeticalScrollingEnabled
Sets an attribute that will control the visibility of alphabetical scrolling capabilities.
```kotlin
fun setAlphabeticalScrollingEnabled(value: Boolean)
```
#### setIsAlternatingRowBackgroundColorsEnabled
Sets an attribute that will determine whether the background of each row will use alternating colors. 
```kotlin
fun setIsAlternatingRowBackgroundColorsEnabled(value: Boolean)
```

### Extension Properties
#### header_background_alternate_color_1
This represents the default background color resource value. We have the ability to overwrite the default value, which is shown below.
```kotlin
<color name="header_background_alternate_color_1">#00000000</color>
```
#### header_background_alternate_color_2
This represents the alternate background color resource value. We have the ability to overwrite the default value, which is shown below. 
```kotlin
<color name="header_background_alternate_color_2">#E1E1E1</color>
```
#### view_holder_background
This represents the default background drawable of each cell of this component. We have the ability to overwrite the default value, which is shown below.
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:drawable="@drawable/view_holder_background_selected"
        android:state_enabled="true"
        android:state_selected="true"/>

    <item android:drawable="@drawable/view_holder_background_pressed"
        android:state_enabled="true"
        android:state_pressed="true"/>

    <item android:drawable="@drawable/view_holder_background_default"/>
</selector>
```


### Extension Functions

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
* **Checkmark**: A Check mark view, consisting of a title, details, and a check mark image. 
* **Color**: A Color, consisting of a title, details, and an image containing a circle using the specified background color. If the corresponding view is to be multi-colored, then a multi-colored image is displayed instead. 
* **Expandable**: A category, consisting of a title details, and a UP / DOWN arrow indicator. 
* **Header**: A category, consisting of a title and details. 
* **Label**: A category, consisting of a title and details. User interaction is disabled. 
* **Price**: If this type is specified, A range slider is displayed. Individual text fields are also provided to allow the user to set the minimum and maximum prices respectively. No title and details will be displayed. 
* **Text**: A category, consisting of a title and details. This view is selectable. 
* **Toggle**: A Toggle, consisting of a title, details, and an ON/OFF toggle button. 
* **TwoColumnHeader**: This item contains two columns of information in the same view. Both columns will consists of a title and details. Because this view is treated as a header, alternating colors will not be applied to this view. 
* **TwoColumnDetails**: This item contains two columns of information in the same view. Both columns will consists of a title and details. Alternating colors will be applied to this view. 

        