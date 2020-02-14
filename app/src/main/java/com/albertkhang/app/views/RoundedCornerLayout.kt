package com.albertkhang.app.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class RoundedCornerLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var radius = 20f

    override fun draw(canvas: Canvas?) {
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val path = Path()
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        canvas?.clipPath(path)

        super.draw(canvas)
    }
}