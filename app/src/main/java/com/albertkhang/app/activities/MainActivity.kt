package com.albertkhang.app.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertkhang.app.*
import com.albertkhang.app.adapters.SongsAdapter
import com.albertkhang.app.fragments.MiniPlayerFragment
import com.albertkhang.app.networks.SongsService
import com.albertkhang.app.networks.api_url
import com.albertkhang.app.utils.Song
import com.albertkhang.app.utils.Songs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MiniPlayerFragment.OnFragmentInteractionListener {
    //RecyclerView
    private var rvSongs: RecyclerView? = null
    private var songList: MutableList<Song>? = null
    private var songsAdapter: SongsAdapter? = null

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
        rvSongs = findViewById(R.id.rvSongs)
        songList = mutableListOf()
        songsAdapter = SongsAdapter(this)

        rvSongs?.adapter = songsAdapter
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        rvSongs?.layoutManager = manager

        addMiniPlayer()
    }

    private fun addEvent() {
        getSongsData()
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
                    songList?.add(it)
                    Log.d("enqueue", "songName: ${it.songName}")
                }

                songsAdapter?.update(songList!!)
            }

            override fun onFailure(call: Call<Songs>, t: Throwable) {
                Log.d("enqueue", "$t")
            }
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
