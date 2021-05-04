package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonWidth = 0

    private var colorInactive = 0
    private var colorActive = 0
    private var textColor = 0
    private var text: String = ""

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Completed -> {
                text = resources.getText(R.string.button_name).toString()
                buttonWidth = 0
            }
            ButtonState.Clicked ->{
                text = resources.getText(R.string.button_loading).toString()
                buttonLoadingAnimation()
            }
            ButtonState.Loading ->{

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
            colorInactive = getColor(R.styleable.LoadingButton_colorInactive, Color.CYAN)
            colorActive = getColor(R.styleable.LoadingButton_colorActive, Color.BLUE)
            textColor = getColor(R.styleable.LoadingButton_textColor, Color.WHITE)
//            text = getString(R.styleable.LoadingButton_text) as String
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = colorInactive
        canvas?.drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(), paint)

        paint.color = colorActive
        canvas?.drawRect(0f,0f,(widthSize.toFloat()/100) * buttonWidth, heightSize.toFloat(),paint)

        paint.color = textColor
        canvas?.drawText(text, widthSize / 2f, heightSize / 2f, paint)
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
        val buttonAnimator = ValueAnimator.ofInt(0, widthSize).apply {
            duration = 3000
            repeatMode = ValueAnimator.RESTART
            repeatCount = 0
            addUpdateListener {
                buttonWidth = it.animatedValue as Int
                Log.d(TAG,"${animatedValue as Int}")
                invalidate()
            }
        }
        buttonAnimator.doOnEnd {
            buttonState = ButtonState.Completed
            //Log.d(TAG, "Completed")
            Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
        }
        buttonAnimator.start()
    }

    companion object{
        private const val TAG = "LoadingButton"
    }
}