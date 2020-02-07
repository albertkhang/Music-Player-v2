package com.albertkhang.musicplayerv2.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.albertkhang.musicplayerv2.fragments.MiniPlayerFragment
import com.albertkhang.musicplayerv2.R

class MainActivity : AppCompatActivity(), MiniPlayerFragment.OnFragmentInteractionListener {
    private var flMiniPlayerFragment: FrameLayout? = null

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
        flMiniPlayerFragment = findViewById(R.id.flMiniPlayerFragment)
    }

    private fun addEvent() {
        addMiniPlayer()
    }

    private fun addMiniPlayer() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flMiniPlayerFragment, miniPlayerFragment)
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
