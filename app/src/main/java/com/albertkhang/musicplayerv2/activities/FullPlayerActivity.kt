package com.albertkhang.musicplayerv2.activities

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import com.albertkhang.musicplayerv2.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

class FullPlayerActivity : AppCompatActivity() {
    private var imgCoverBackground: ImageView? = null

    private val url_cover =
        "https://static.yeah1.com/uploads/editors/26/2019/11/21/1rDRXaIzn0IBFTphHKhABzyov6rOToLlwJOONJHg.jpeg"

    //    Top Icon
    private var flDownArrow: FrameLayout? = null
    private var flQueueMusic: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_player)

        addControl()
        addEvent()
    }

    private fun addControl() {
        imgCoverBackground = findViewById(R.id.imgCoverBackground)

//        Top Icon
        flDownArrow = findViewById(R.id.flDownArrow)
        flQueueMusic = findViewById(R.id.flQueueMusic)
    }

    private fun addEvent() {
        makeBackground()

        flDownArrow?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Down Arrow", Toast.LENGTH_SHORT).show()
        })

        flQueueMusic?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Queue Music", Toast.LENGTH_SHORT).show()
        })
    }

    private fun makeBackground() {
        val radius=resources.getInteger(R.integer.dimenFullPlayerCoverBackgroundRadius)
        val sampling=resources.getInteger(R.integer.dimenFullPlayerCoverBackgroundSampling)

        imgCoverBackground?.let {
            Glide.with(this)
                .load(url_cover)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
                .into(it)
        }
    }
}
