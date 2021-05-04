package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonWidth = 0

    private var startCircle = 0f
    private var endCircle = 360f
    private var progressCircle = 0f

    private val animDuration = 2000L

    private var colorInactive = 0
    private var colorActive = 0
    private var textColor = 0
    private var arcColor = 0
    private var text: String = ""

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Completed -> {
                text = resources.getText(R.string.button_name).toString()
                buttonWidth = 0
            }
            ButtonState.Clicked -> {
                text = resources.getText(R.string.button_loading).toString()
                buttonLoadingAnimation()
            }
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }


    init {
        isClickable = true
        buttonState = ButtonState.Completed

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            colorInactive = getColor(R.styleable.LoadingButton_colorInactive, ContextCompat.getColor(context, R.color.colorPrimary))
            colorActive = getColor(R.styleable.LoadingButton_colorActive, ContextCompat.getColor(context, R.color.colorPrimaryDark))
            textColor = getColor(R.styleable.LoadingButton_textColor, ContextCompat.getColor(context, R.color.white))
            arcColor = getColor(R.styleable.LoadingButton_arcColor, Color.YELLOW)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = colorInactive
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = colorActive
        canvas?.drawRect(0f, 0f, buttonWidth.toFloat(), heightSize.toFloat(), paint)

        paint.color = textColor
        canvas?.drawText(text, widthSize / 2f, heightSize / 1.65f, paint)

        paint.color = arcColor
        val xPos = widthSize / 1.25f
        val yPos = heightSize / 4f
        val size = heightSize / 2f
        canvas?.drawArc(xPos, yPos, xPos + size, yPos + size, startCircle, progressCircle, true, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Clicked
        return super.performClick()
    }

    private fun buttonLoadingAnimation() {
        val loaderAnim = ValueAnimator.ofInt(0, widthSize).apply {
            duration = animDuration
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                buttonWidth = it.animatedValue as Int
                invalidate()
            }
            doOnEnd {
                buttonState = ButtonState.Completed
            }
        }

        val circleAnim = ValueAnimator.ofFloat(startCircle, endCircle).apply {
            duration = animDuration
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                progressCircle = animatedValue as Float
                invalidate()
            }
            doOnEnd {
                progressCircle = 0f
            }
        }

        loaderAnim.start()
        circleAnim.start()
    }

    companion object {
        private const val TAG = "LoadingButton"
    }
}