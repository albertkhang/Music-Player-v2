package com.albertkhang.app.fragments

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
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
import com.albertkhang.app.utils.Song
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_mini_player.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    private var flMiniPlayer: FrameLayout? = null

    private var imgCover: CircleImageView? = null

    private var imgFavorite: ImageView? = null
    private var isFavoriteClicked = false

    private var mediaPlayer: MediaPlayer? = null
    private var imgPlayPause: ImageView? = null
    private var rotateView: RotationView? = null

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
        rotateView = RotationView()
        rotateView?.view = imgCover

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

    private fun getDefaultRotateCover() {
        rotateView?.getDefault()
    }

    private fun resetDefaultRotateCover() {
        rotateView?.resetAnimator()
    }

    private fun changeCoverStatus() {
        if (mediaPlayer?.isPlaying == true) {
            if (rotateView?.isNull() == true) {
                getDefaultRotateCover()
                startRotateCover()
            } else {
                resumeRotateCover()
            }
        } else {
            pauseRotateCover()
        }
    }

    private fun startRotateCover() {
        rotateView?.start()
    }

    private fun resumeRotateCover() {
        rotateView?.resume()
    }

    private fun pauseRotateCover() {
        rotateView?.pause()
    }

    private fun endRotateCover() {
        if (rotateView != null) {
            rotateView?.end()
        }
    }

    private fun bindSongToMediaPlayer() {
        mediaPlayer = MediaPlayer.create(view?.context, R.raw.co_nhu_khong_co_34s)
    }

    private fun setCompletionMediaPlayerListener() {
        mediaPlayer?.setOnCompletionListener {
            imgPlayPause?.setImageResource(R.drawable.ic_play)
            rotateView?.pause()

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
        intent.putExtra("singerName", currentSong?.singerName)
        intent.putExtra("cover_url", currentSong?.cover_url)

        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(data: Any) {
        when (data) {
            is Song -> {
                currentSong = data.copy()

                txtSongName?.setText(data.songName)
                txtSingerName?.setText(data.singerName)

                imgCover?.let {
                    Glide.with(this)
                        .load(data.cover_url)
                        .into(it)
                }

                if (mediaPlayer != null) {
                    mediaPlayer?.stop()
                    mediaPlayer = null
                    endRotateCover()
                    changePlayPauseIcon()
                    resetDefaultRotateCover()
                }

                bindSongToMediaPlayer()
                setCompletionMediaPlayerListener()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MiniPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}