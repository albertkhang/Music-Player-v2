package com.albertkhang.app.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.albertkhang.app.R
import com.albertkhang.app.animations.RotationView
import com.albertkhang.app.fragments.MiniPlayerFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_full_player.*


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
    private var imgPlayPause: ImageView? = null
    private var flForward: FrameLayout? = null

    private var flRepeat: FrameLayout? = null
    private var flFavorite: FrameLayout? = null

    private var mediaPlayer: MediaPlayer? = null
    private var rotationView: RotationView? = null

    override fun onCreate(savedInstanceState: Bundle?)  {
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
        imgPlayPause = findViewById(R.id.imgPlayPause)
        flForward = findViewById(R.id.flForward)

        flRepeat = findViewById(R.id.flRepeat)
        flFavorite = findViewById(R.id.flFavorite)

        mediaPlayer = MiniPlayerFragment.mediaPlayer
        rotationView = RotationView()
        rotationView?.view = imgCover
        updateRotationStatus()
    }

    private fun addEvent() {
        getIntentData()
        setOnClick()

        setCompletionMediaPlayerListener()
        changeRotationStatus()
        changePlayPauseIcon()
    }

    override fun onStop() {
        MiniPlayerFragment.fractionValue = rotationView?.getAnimatedFraction()
        super.onStop()
    }

    private fun changeMusicStatus() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        } else {
            mediaPlayer?.pause()
        }
    }

    private fun changeRotationStatus() {
        if (mediaPlayer?.isPlaying == true && rotationView?.isNull() != true) {
            rotationView?.resume()
        } else {
            rotationView?.pause()
        }
    }

    private fun updateRotationStatus() {
        rotationView?.getDefault()
        rotationView?.setAnimatedFraction(MiniPlayerFragment.fractionValue)
        rotationView?.start()
        rotationView?.pause()
    }

    private fun changePlayPauseIcon() {
        if (mediaPlayer?.isPlaying == true) {
            imgPlayPause?.setImageResource(R.drawable.ic_pause)
        } else {
            imgPlayPause?.setImageResource(R.drawable.ic_play)
        }
    }

    private fun setCompletionMediaPlayerListener() {
        mediaPlayer?.setOnCompletionListener {
            imgPlayPause?.setImageResource(R.drawable.ic_play)
            rotationView?.pause()
        }
    }

    private fun getIntentData() {
        txtSongName?.setText(intent.getStringExtra("songName"))
        txtSingerName?.setText(intent.getStringExtra("singerName"))

        val cover_url = intent.getStringExtra("cover_url")

        setCover(cover_url)
        makeBlurCoverBackground(cover_url)
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
            changeMusicStatus()
            changeRotationStatus()
            changePlayPauseIcon()
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
