package com.albertkhang.app.activities

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.albertkhang.app.*
import com.albertkhang.app.adapters.SongsAdapter
import com.albertkhang.app.fragments.MiniPlayerFragment
import com.albertkhang.app.networks.SongsService
import com.albertkhang.app.networks.api_url
import com.albertkhang.app.utils.Song
import com.albertkhang.app.utils.Songs
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.greenrobot.eventbus.EventBus
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
        requestPermission()

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
        songsAdapter?.setOnItemClickListener(object : SongsAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int) {
                EventBus.getDefault().post(songList?.get(position))
            }

        })
    }

    private fun requestPermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun getSongsData() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiInterface = retrofit.create(SongsService.ApiInterface::class.java)

        apiInterface.get().enqueue(object : Callback<Songs> {
            override fun onResponse(call: Call<Songs>, response: Response<Songs>) {
                val songs = response.body()
                Log.d("apiInterface", "size: ${songs?.songs?.size}")

                songs?.songs?.forEach {
                    songList?.add(it)
                    Log.d("apiInterface", "songName: ${it.songName}")
                }

                songsAdapter?.update(songList!!)
            }

            override fun onFailure(call: Call<Songs>, t: Throwable) {
                Log.d("apiInterface", "$t")
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
