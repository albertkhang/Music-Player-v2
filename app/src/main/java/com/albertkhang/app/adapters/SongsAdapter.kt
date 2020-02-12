package com.albertkhang.app.adapters

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.albertkhang.app.R
import com.albertkhang.app.utils.Song
import com.bumptech.glide.Glide

class SongsAdapter(val context: Context) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private var songs: MutableList<Song> = mutableListOf()

    fun update(songs: MutableList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_song, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(songs[position].album.cover_url)
            .placeholder(R.drawable.ic_music_layer_default_cover)
            .error(R.drawable.ic_music_layer_default_cover)
            .into(holder.imgCover)

        holder.txtSongName.text = songs[position].songName
        Log.d("songsAdapter", songs[position].songName)

        holder.txtSingerName.text = songs[position].singers()
        Log.d("songsAdapter", songs[position].singers())

        changeFavoriteIcon(holder, position)

        holder.itemView.setOnClickListener { view ->
            onItemClickListener?.onItemClickListener(
                view,
                position
            )
        }

        holder.flSongItem.setOnClickListener { view -> run { holder.itemView.callOnClick() } }
    }

    private fun changeFavoriteIcon(holder: ViewHolder, position: Int) {
        if (songs[position].isFavorite) {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flSongItem: FrameLayout = itemView.findViewById(R.id.flSongItem)

        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)

        val txtSongName: TextView = itemView.findViewById(R.id.txtSongName)
        val txtSingerName: TextView = itemView.findViewById(R.id.txtSingerName)

        val imgFavorite: ImageView = itemView.findViewById(R.id.imgFavorite)
        val imgMore: ImageView = itemView.findViewById(R.id.imgMore)
    }
}