package com.albertkhang.musicplayerv2.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.albertkhang.musicplayerv2.fragments.MiniPlayerFragment
import com.albertkhang.musicplayerv2.R
import com.albertkhang.musicplayerv2.fake_singerName
import com.albertkhang.musicplayerv2.fake_songName
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity(), MiniPlayerFragment.OnFragmentInteractionListener {
    private var btnPlayMusic: Button? = null

    //MiniPlayer
    private val miniPlayerFragment = MiniPlayerFragment()
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addControl()
        addEvent()
    }

    private fun addControl() {
        btnPlayMusic = findViewById(R.id.btnPlayMusic)
    }

    private fun addEvent() {
        addMiniPlayer()

        btnPlayMusic?.setOnClickListener(View.OnClickListener {
            EventBus.getDefault().post("changeText")
        })
    }

    private fun addMiniPlayer() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flMiniPlayer, miniPlayerFragment)
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
