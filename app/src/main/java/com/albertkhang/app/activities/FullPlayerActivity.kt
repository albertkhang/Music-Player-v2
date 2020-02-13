package com.albertkhang.app.activities

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
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

    //Timestamp
    private var txtCurrentTimestamp: TextView? = null
    private var txtEndTimestamp: TextView? = null
    private val timestampHandler = Handler()
    private val timestampRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer?.isPlaying == true) {
                val timestamp = mediaPlayer?.currentPosition?.div(1000)
                txtCurrentTimestamp?.text = getTimestamp(timestamp!!)

                timestampHandler.postDelayed(this, 1000)
            }
        }
    }

    //Song And Singer Name
    private var txtSongName: TextView? = null
    private var txtSingerName: TextView? = null

    //SeekBar
    private var sbSeekBar: SeekBar? = null
    private val seekBarHandler = Handler()
    private val seekBarRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer?.isPlaying == true) {
                sbSeekBar?.progress = mediaPlayer!!.currentPosition
                seekBarHandler.postDelayed(this, 1000)
            }
        }
    }

    //Playback Icon
    private var flRewind: FrameLayout? = null
    private var flPlayPause: FrameLayout? = null
    private var imgPlayPause: ImageView? = null
    private var flForward: FrameLayout? = null

    private var flRepeat: FrameLayout? = null
    private var flFavorite: FrameLayout? = null

    private var mediaPlayer: MediaPlayer? = null
    private var rotationView: RotationView? = null

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

        //Timestamp
        txtCurrentTimestamp = findViewById(R.id.txtCurrentTimestamp)
        txtEndTimestamp = findViewById(R.id.txtEndTimestamp)

        //Song And Singer Name
        txtSongName = findViewById(R.id.txtSongName)
        txtSingerName = findViewById(R.id.txtSingerName)

        //SeekBar
        sbSeekBar = findViewById(R.id.sbSeekBar)

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
        changeSeekBarStatus()
        changeTimestampStatus()
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

    private fun changeSeekBarStatus() {
        if (mediaPlayer?.isPlaying == true) {
            runUpdateSeekBar()
        } else {
            stopUpdateSeekBar()
        }
    }

    private fun changeTimestampStatus() {
        if (mediaPlayer?.isPlaying == true) {
            runUpdateTimestamp()
        } else {
            stopUpdateTimestamp()
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
            MiniPlayerFragment.fractionValue = rotationView?.getAnimatedFraction()

            mediaPlayer?.seekTo(0)
            sbSeekBar?.progress = 0
            txtCurrentTimestamp?.text = getTimestamp(0)
        }
    }

    private fun getIntentData() {
        txtSongName?.setText(intent.getStringExtra("songName"))
        txtSingerName?.setText(intent.getStringExtra("singerName"))

        val cover_url = intent.getStringExtra("cover_url")
        setCover(cover_url)
        makeBlurCoverBackground(cover_url)

//        Using below line if music url is full song
//        txtEndTimestamp?.text = getTimestamp(intent.getIntExtra("duration", 0))

        txtEndTimestamp?.text = getTimestamp(mediaPlayer!!.duration / 1000)
        sbSeekBar?.max = mediaPlayer!!.duration
    }

    private fun runUpdateSeekBar() {
        seekBarRunnable.run()
    }

    private fun stopUpdateSeekBar() {
        seekBarHandler.removeCallbacks(seekBarRunnable)
    }

    private fun runUpdateTimestamp() {
        timestampRunnable.run()
    }

    private fun stopUpdateTimestamp() {
        timestampHandler.removeCallbacks(timestampRunnable)
    }

    private fun getTimestamp(duration: Int): String {
        //Format: 01:01
        Log.d("getTimestamp", duration.toString())

        val minute = duration / 60
        val second = duration % 60

        var result = ""

        if (minute > 9) {
            result += minute
        } else {
            result += "0$minute"
        }

        result += ":"

        if (second > 9) {
            result += second
        } else {
            result += "0$second"
        }

        return result
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
            changeSeekBarStatus()
            changeTimestampStatus()
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
