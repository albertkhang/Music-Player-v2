package com.albertkhang.app.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.albertkhang.app.R

class ActiveImage constructor(private val context: Context) {
    fun activeRepeatDrawable(): Drawable {
        return getActiveIconDrawable(R.drawable.ic_repeat)
    }

    fun inactiveRepeatDrawable(): Drawable {
        return getInactiveIconDrawable(R.drawable.ic_repeat)
    }

    fun activeFavoriteDrawable(): Drawable {
        return getActiveIconDrawable(R.drawable.ic_favorite)
    }

    fun inactiveFavoriteDrawable(): Drawable {
        return getInactiveIconDrawable(R.drawable.ic_favorite_border)
    }

    private fun getActiveIconDrawable(iconId: Int): Drawable {
        val drawable =
            AppCompatResources.getDrawable(context, iconId)

        val unwrappedDrawable = drawable?.constantState?.newDrawable()?.mutate()

        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, context.resources.getColor(R.color.colorMain))

        return wrappedDrawable
    }

    private fun getInactiveIconDrawable(iconId: Int): Drawable {
        return AppCompatResources.getDrawable(context, iconId)!!
    }
}