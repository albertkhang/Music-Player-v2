package com.albertkhang.app.fragments

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.albertkhang.app.R
import com.albertkhang.app.activities.FullPlayerActivity
import com.albertkhang.app.animations.RotationView
import com.albertkhang.app.networks.SongsService
import com.albertkhang.app.networks.api_song_url
import com.albertkhang.app.utils.Song
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_mini_player.flFavorite
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MiniPlayerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    companion object {
        var mediaPlayer: MediaPlayer? = null
        var rotationView: RotationView? = null
        var fractionValue: Float? = null

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MiniPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var flMiniPlayer: FrameLayout? = null

    private var imgCover: CircleImageView? = null

    private var imgFavorite: ImageView? = null
    private var isFavoriteClicked = false

    private var imgPlayPause: ImageView? = null

    private var txtSongName: TextView? = null
    private var txtSingerName: TextView? = null

    private var currentSong: Song? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addControl()
        addEvent()
    }

    private fun addControl() {
        flMiniPlayer = view?.findViewById(R.id.flMiniPlayer)

        imgCover = view?.findViewById(R.id.imgCover)

        imgFavorite = view?.findViewById(R.id.imgFavorite)
        imgPlayPause = view?.findViewById(R.id.imgPlayPause)
        rotationView = RotationView()
        rotationView?.view = imgCover

        txtSongName = view?.findViewById(R.id.txtSongName)
        txtSingerName = view?.findViewById(R.id.txtSingerName)
    }

    private fun addEvent() {
        flMiniPlayer?.setOnClickListener(View.OnClickListener {
            if (currentSong != null) {
                openFullPlayerActivity()
            }
        })

        flFavorite.setOnClickListener(View.OnClickListener {
            changeFavoriteIcon()
        })

        imgPlayPause?.setOnClickListener(View.OnClickListener {
            if (mediaPlayer != null) {
                changeMusicStatus()
                changeCoverStatus()
                changePlayPauseIcon()
            } else {
                Toast.makeText(view?.context, "Please select a song!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setDefaultRotationCover() {
        rotationView?.getDefault()
        rotationView?.start()
        rotationView?.pause()
    }

    private fun resetDefaultRotateCover() {
        rotationView?.resetAnimator()
    }

    private fun changeCoverStatus() {
        if (mediaPlayer?.isPlaying == true && rotationView?.isNull() != true) {
            rotationView?.resume()
        } else {
            rotationView?.pause()
        }
    }

    private fun bindThenStart() {
        mediaPlayer = MediaPlayer.create(context, Uri.parse("file:${getSongDir()}"))
        imgPlayPause?.callOnClick()
    }

    private fun setCompletionMediaPlayerListener() {
        mediaPlayer?.setOnCompletionListener {
            imgPlayPause?.setImageResource(R.drawable.ic_play)
            rotationView?.pause()
            fractionValue = rotationView?.getAnimatedFraction()

            Log.d("_mediaPlayer", "setOnCompletionListener")
        }
    }

    private fun changeMusicStatus() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        } else {
            mediaPlayer?.pause()
        }
    }

    private fun changePlayPauseIcon() {
        if (mediaPlayer?.isPlaying == true) {
            imgPlayPause?.setImageResource(R.drawable.ic_pause)
        } else {
            imgPlayPause?.setImageResource(R.drawable.ic_play)
        }
    }

    private fun changeFavoriteIcon() {
        if (!isFavoriteClicked) {
            //change into clicked icon
            imgFavorite?.setImageResource(R.drawable.ic_favorite)
            isFavoriteClicked = true
        } else {
            imgFavorite?.setImageResource(R.drawable.ic_favorite_border)
            isFavoriteClicked = false
        }
    }

    private fun openFullPlayerActivity() {
        val intent = Intent(view?.context, FullPlayerActivity::class.java)
        intent.putExtra("songName", currentSong?.songName)
        intent.putExtra("singerName", currentSong?.singers())
        intent.putExtra("cover_url", currentSong?.album?.cover_url)
        intent.putExtra("duration", currentSong?.duration)

        fractionValue = rotationView?.getAnimatedFraction()

        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(data: Any) {
        when (data) {
            is Song -> {
                currentSong = data.copy()

                txtSongName?.setText(data.songName)
                txtSingerName?.setText(data.singers())

                imgCover?.let {
                    Glide.with(this)
                        .load(data.album.cover_url)
                        .into(it)
                }

                if (mediaPlayer != null) {
                    Log.d("onEvent", "mediaPlayer != null")

                    mediaPlayer?.stop()
                    mediaPlayer = null
                    rotationView?.end()
                    changePlayPauseIcon()
                    resetDefaultRotateCover()
                }

                if (isSongExists()) {
                    Log.d("isSongExists", "bindThenStart()")
                    bindThenStart()
                } else {
                    Log.d("isSongExists", "downloadThenStart()")
                    downloadThenStart()
                }
                setCompletionMediaPlayerListener()
                setDefaultRotationCover()
            }
        }
    }

    private fun downloadThenStart() {
        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(api_song_url)
            .build()

        val songInterface = retrofit.create(SongsService.SongInterface::class.java)
        songInterface.get(currentSong!!.getSongUrlName()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val file = File(getSongDir())
                Log.d("downloadThenStart", "$file")

                val inputStream = response.body()?.byteStream()
                val fileOutputStream = FileOutputStream(file)

                var line = inputStream?.read()

                while (line != -1) {
                    line?.let { fileOutputStream.write(it) }
                    line = inputStream?.read()
                }

                inputStream?.close()
                fileOutputStream.close()

                mediaPlayer = MediaPlayer.create(context, Uri.parse("file:${getSongDir()}"))
                imgPlayPause?.callOnClick()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("downloadThenStart", "$t")
            }
        })
    }

    private fun isSongExists(): Boolean {
        val file = File(getSongDir())

        if (file.exists()) {
            Log.d("isSongExists", "file.exists")
            return true
        }

        return false
    }

    private fun getFileSongName(): String {
        val filename = "${currentSong?.getSongUrlMD5()}.mp3"
        Log.d("getFileSongName", filename)

        return filename
    }

    private fun getSongDir(): String {
        val fileSongName = getFileSongName()

        val dir =
            "${Environment.getExternalStorageDirectory().path}/${Environment.DIRECTORY_DOWNLOADS}/${fileSongName}"
        Log.d("getSongDir", "$dir")

        return dir
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        updateUIStatusWhenBack()
        setCompletionMediaPlayerListener()
    }

    private fun updateUIStatusWhenBack() {
        changePlayPauseIcon()
        changeCoverStatus()
        updateCoverFraction()
    }

    private fun updateCoverFraction() {
        if (fractionValue != rotationView?.getAnimatedFraction()) {
            rotationView?.setAnimatedFraction(fractionValue)
        }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}