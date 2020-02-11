package com.albertkhang.app.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.albertkhang.app.*
import com.albertkhang.app.fragments.MiniPlayerFragment
import com.albertkhang.app.networks.SongsService
import com.albertkhang.app.networks.api_url
import com.albertkhang.app.utils.Song
import com.albertkhang.app.utils.Songs
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MiniPlayerFragment.OnFragmentInteractionListener {
//    private var btnPlayMusic: Button? = null

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
//        btnPlayMusic = findViewById(R.id.btnPlayMusic)
    }

    private fun addEvent() {
        addMiniPlayer()
        getSongsData()

//        btnPlayMusic?.setOnClickListener(View.OnClickListener {
////            addFakeSong()
//        })

//        val musicAPIFactory = MusicAPIFactory.getJsonAPI()
    }

    private fun getSongsData() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val songInterface = retrofit.create(SongsService.SongInterface::class.java)

        songInterface.get().enqueue(object : Callback<Songs> {
            override fun onResponse(call: Call<Songs>, response: Response<Songs>) {
                val songs = response.body()
                Log.d("enqueue", "size: ${songs?.songs?.size}")

                songs?.songs?.forEach {
                    Log.d("enqueue", "songName: ${it.songName}")
                }
            }

            override fun onFailure(call: Call<Songs>, t: Throwable) {
                Log.d("enqueue", "$t")
            }
        })
    }

//    private fun addFakeSong() {
//        val data = Song()
//        data.songName = fake_songName
//        data.singerName = fake_singerName
//        data.cover_url = fake_cover_url
//        EventBus.getDefault().post(data)
//    }

    private fun addMiniPlayer() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flMiniPlayer, miniPlayerFragment)
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
    }
}
