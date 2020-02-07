package com.albertkhang.musicplayerv2.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.albertkhang.musicplayerv2.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_full_player.*
import java.util.jar.Manifest

class FullPlayerActivity : AppCompatActivity() {
    private var imgCoverBackground: ImageView? = null

    //    Top Icon
    private var flDownArrow: FrameLayout? = null
    private var flQueueMusic: FrameLayout? = null

    //Song And Singer Name
    private var txtSongName: TextView? = null
    private var txtSingerName: TextView? = null

    //Playback Icon
    private var flRewind: FrameLayout? = null
    private var flPlayPause: FrameLayout? = null
    private var flForward: FrameLayout? = null

    private var flRepeat: FrameLayout? = null
    private var flFavorite: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_player)

        addControl()
        addEvent()
    }

    private fun addControl() {
        imgCoverBackground = findViewById(R.id.imgCoverBackground)

        //Top Icon
        flDownArrow = findViewById(R.id.flDownArrow)
        flQueueMusic = findViewById(R.id.flQueueMusic)

        //Song And Singer Name
        txtSongName = findViewById(R.id.txtSongName)
        txtSingerName = findViewById(R.id.txtSingerName)

        //Playback Icon
        flRewind = findViewById(R.id.flRewind)
        flPlayPause = findViewById(R.id.flPlayPause)
        flForward = findViewById(R.id.flForward)

        flRepeat = findViewById(R.id.flRepeat)
        flFavorite = findViewById(R.id.flFavorite)
    }

    private fun addEvent() {
        txtSongName?.setText(intent.getStringExtra("songName"))
        txtSingerName?.setText(intent.getStringExtra("singerName"))

        val cover_url = intent.getStringExtra("cover_url")

        setCover(cover_url)
        makeBlurCoverBackground(cover_url)

        setOnClick()
    }

    private fun setOnClick() {
        flDownArrow?.setOnClickListener(View.OnClickListener {
            finish()
        })

        flQueueMusic?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Queue Music", Toast.LENGTH_SHORT).show()
        })

        flRepeat?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show()
        })

        flRewind?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Rewind", Toast.LENGTH_SHORT).show()
        })

        flPlayPause?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Play Pause", Toast.LENGTH_SHORT).show()
        })

        flForward?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Forward", Toast.LENGTH_SHORT).show()
        })

        flFavorite?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show()
        })
    }

    private fun setCover(url: String) {
        Glide.with(this)
            .load(url)
            .error(R.color.colorIcon)
            .placeholder(R.color.colorIcon)
            .into(imgCover)
    }

    private fun makeBlurCoverBackground(url: String) {
        val radius = resources.getInteger(R.integer.dimenFullPlayerCoverBackgroundRadius)
        val sampling = resources.getInteger(R.integer.dimenFullPlayerCoverBackgroundSampling)

        imgCoverBackground?.let {
            Glide.with(this)
                .load(url)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(radius, sampling)))
                .error(R.color.colorIcon)
                .placeholder(R.color.colorIcon)
                .into(it)
        }
    }
}
