package com.albertkhang.app.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.albertkhang.app.fragments.MiniPlayerFragment
import com.albertkhang.app.R
import com.albertkhang.app.fake_cover_url
import com.albertkhang.app.fake_singerName
import com.albertkhang.app.fake_songName
import com.albertkhang.app.utils.Song
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
            var data = Song(fake_songName, fake_singerName, fake_cover_url,null)
            EventBus.getDefault().post(data)
        })
    }

    private fun addMiniPlayer() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flMiniPlayer, miniPlayerFragment)
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
    }
}
