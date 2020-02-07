package com.albertkhang.musicplayerv2.activities

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.albertkhang.musicplayerv2.R
import jp.wasabeef.blurry.Blurry

class FullPlayerActivity : AppCompatActivity() {
    private var clFullPlayerActivity: ConstraintLayout? = null

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
//        clFullPlayerActivity = findViewById(R.id.clFullPlayerActivity)

//        Top Icon
        flDownArrow = findViewById(R.id.flDownArrow)
        flQueueMusic = findViewById(R.id.flQueueMusic)
    }

    private fun addEvent() {
//        makeBackground()

        flDownArrow?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Down Arrow", Toast.LENGTH_SHORT).show()
        })

        flQueueMusic?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Queue Music", Toast.LENGTH_SHORT).show()
        })
    }

    private fun makeBackground() {
//        clFullPlayerActivity?.post {
//            Blurry.with(this)
//                .radius(18)
//                .sampling(7)
//                .async()
//                .onto(clFullPlayerActivity)
//        }
    }
}
