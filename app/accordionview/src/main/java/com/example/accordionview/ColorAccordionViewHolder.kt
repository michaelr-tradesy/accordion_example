package com.example.accordionview

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * @name ColorAccordionViewHolder
 * @author Coach Roebuck
 * @since 2.18
 * This method uses the details of the model, which is passed in as a parameter,
 * in order to load this view.
 */
class ColorAccordionViewHolder(v: View) : TextAccordionViewHolder(v) {

    private val imageView: ImageView = v.findViewById(R.id.imageView)
    private val imageViewWidth = 256
    private val imageViewHeight = 256

    /**
     * @name bind
     * @author Coach Roebuck
     * @since 2.18
     * This method uses the details of the model, which is passed in as a parameter,
     * in order to load this view.
     * @param model an instance of the AccordionViewModel
     * @param callback the callback lambda block, which uses the specified model as a parameter
     */
    override fun bind(
        model: AccordionViewModel?,
        callback: (model: AccordionViewModel) -> Unit
    ) {
        super.bind(model, callback)

        model?.backgroundColor?.let {
            val backgroundColor = Color.parseColor(it)
            val strokeColor = (backgroundColor and 0x00ffffff) or (0xAA shl 24)
            val fillColor = (backgroundColor and 0x00ffffff) or (0x66 shl 24)
            val bitmap: Bitmap = if (model.isMultiColored) {
                drawMultiColoredImage()
            } else {
                drawVisualImage(fillColor)
            }
            val context = itemView.context
            val bgColorTint by lazy { ColorStateList.valueOf(backgroundColor) }
            imageView.imageTintList = bgColorTint
            imageView.background = BitmapDrawable(context.resources, bitmap)
        }
    }

    /**
     * @name drawVisualImage
     * @author Coach Roebuck
     * @since 2.18
     * This method is responsible for dynamically drawing a solid circle
     * in the absolute center of the canvas.
     * @param fillColor The numeric representation of the color
     * @return a Bitmap representation of the drawing
     */
    private fun drawVisualImage(fillColor: Int): Bitmap {
        val bitmap: Bitmap =
            Bitmap.createBitmap(imageViewWidth, imageViewHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        canvas.drawColor(fillColor, PorterDuff.Mode.CLEAR)
        paint.textAlign = Paint.Align.CENTER
        paint.color = fillColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = imageViewWidth * 0.05f
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawCircle(
            imageViewWidth * 0.5f,
            imageViewHeight * 0.5f,
            imageViewWidth * 0.5f,
            paint
        )

        canvas.save()
        canvas.restore()

        return bitmap
    }

    /**
     * @name drawMultiColoredImage
     * @author Coach Roebuck
     * @since 2.18
     * This method is responsible for dynamically drawing a multi-colored circle
     * in the absolute center of the canvas.
     * @return a Bitmap representation of the drawing
     */
    private fun drawMultiColoredImage(): Bitmap {
        val bitmap: Bitmap =
            Bitmap.createBitmap(imageViewWidth, imageViewHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val sectors = 180.0f
        val radius = imageViewWidth.coerceAtMost(imageViewHeight).toFloat() * 0.5f - 8f
        val radians = 2.0f * Math.PI.toFloat() / sectors
        val degrees = (radians * (180 / Math.PI)).toFloat()
        val rect = RectF()
        val paint = Paint()
        var i = 0

        rect.set(
            imageViewWidth / 2 - radius,
            imageViewHeight / 2 - radius,
            imageViewWidth / 2 + radius,
            imageViewHeight / 2 + radius
        )
        paint.strokeWidth = 8f
        paint.isAntiAlias = false
        paint.strokeCap = Paint.Cap.BUTT
        paint.style = Paint.Style.FILL

        while (i <= sectors) {
            val h = i / sectors * 360
            val s = 1f
            val l = 0.5f
            val c = (1 - abs(2 * l - 1)) * s
            val x = c * (1 - abs(h / 60 % 2 - 1))

            when {
                h > 300 -> paint.color = getIntFromColor(c, 0f, x)
                h > 240 -> paint.color = getIntFromColor(x, 0f, c)
                h > 180 -> paint.color = getIntFromColor(0f, x, c)
                h > 120 -> paint.color = getIntFromColor(0f, c, x)
                h > 60 -> paint.color = getIntFromColor(x, c, 0f)
                else -> paint.color = getIntFromColor(c, x, 0f)
            }

            canvas.drawArc(rect, i * degrees, degrees, true, paint)
            i++
        }

        canvas.save()
        canvas.restore()

        return bitmap
    }

    /**
     * @name getIntFromColor
     * @author Coach Roebuck
     * @since 2.18
     * This method is responsible for retrieving the numerical representation of the RGB color
     * provided.
     * @param red the color presenting red, ranging from 0 - 255
     * @param green the color presenting green, ranging from 0 - 255
     * @param blue the color presenting blue, ranging from 0 - 255
     * @return the numerical representation of the RGB color
     */
    private fun getIntFromColor(red: Float, green: Float, blue: Float): Int {
        var r = (255 * red).roundToInt()
        var g = (255 * green).roundToInt()
        var b = (255 * blue).roundToInt()

        r = r shl 16 and 0x00FF0000
        g = g shl 8 and 0x0000FF00
        b = b and 0x000000FF

        return -0x1000000 or r or g or b
    }
}
