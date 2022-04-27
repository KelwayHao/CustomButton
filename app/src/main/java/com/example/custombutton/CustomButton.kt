package com.example.custombutton

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class CustomButton(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        const val MARGIN_TEXT = 40f
        const val CONTOUR_THICKNESS = 4f
        const val MARGIN_STROKE = 4f + CONTOUR_THICKNESS / 2f
        const val TEXT_SIZE = 48f
    }

    private val buttonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokeButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = CONTOUR_THICKNESS
    }

    private var buttonColor: Int = 0
        set(value) {
            buttonPaint.color = value
            strokeButtonPaint.color = value
            field = value
        }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = context.getColor(R.color.black)
    }

    private var text: String

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.CustomButton, 0, 0).apply {
            text = getString(R.styleable.CustomButton_text).toString()
            buttonColor = getColor(R.styleable.CustomButton_buttonColor, R.style.Theme_CustomButton)
        }
    }

    private var textWidth: Float = 0f
    private var textHeight: Float = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var centerTextWidth = 0f
    private var centerTextHeight = 0f

    private val buttonPath: Path by lazy {
        Path().apply {
            moveTo(
                centerX - centerTextWidth - MARGIN_TEXT,
                centerY - centerTextHeight - MARGIN_TEXT
            )
            lineTo(
                centerX - centerTextWidth - MARGIN_TEXT,
                centerY + centerTextHeight + MARGIN_TEXT
            )
            lineTo(
                centerX + centerTextWidth + MARGIN_TEXT / 2,
                centerY + centerTextHeight + MARGIN_TEXT
            )
            lineTo(centerX + centerTextWidth + MARGIN_TEXT, centerY)
            lineTo(
                centerX + centerTextWidth + MARGIN_TEXT / 2,
                centerY - centerTextHeight - MARGIN_TEXT
            )
            close()
        }
    }
    private val strokeButtonPath: Path by lazy {
        Path().apply {
            moveTo(
                centerX - centerTextWidth - MARGIN_TEXT,
                centerY - centerTextHeight - MARGIN_TEXT - MARGIN_STROKE
            )
            lineTo(
                centerX - centerTextWidth - MARGIN_TEXT,
                centerY + centerTextHeight + MARGIN_TEXT + MARGIN_STROKE
            )
            lineTo(
                centerX + centerTextWidth + MARGIN_TEXT / 2 + MARGIN_STROKE / 2,
                centerY + centerTextHeight + MARGIN_TEXT + MARGIN_STROKE
            )
            lineTo(centerX + centerTextWidth + MARGIN_TEXT + MARGIN_STROKE, centerY)
            lineTo(
                centerX + centerTextWidth + MARGIN_TEXT / 2 + MARGIN_STROKE / 2,
                centerY - centerTextHeight - MARGIN_TEXT - MARGIN_STROKE
            )
            close()
        }
    }

    private var textBounds: Rect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerX = width / 2f
        centerY = height / 2f
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textWidth = textBounds.width().toFloat()
        textHeight = textBounds.height().toFloat()
        centerTextWidth = textWidth / 2f
        centerTextHeight = textHeight / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(strokeButtonPath, strokeButtonPaint)
        canvas.drawPath(buttonPath, buttonPaint)
        canvas.drawText(text, centerX - centerTextWidth, centerY + centerTextHeight, textPaint)
    }
}