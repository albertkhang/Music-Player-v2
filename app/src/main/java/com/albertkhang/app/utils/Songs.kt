package com.albertkhang.app.utils

import com.google.gson.annotations.SerializedName

data class Songs(
    @SerializedName("data")
    var songs: List<Song>
)

data class Song(
    @SerializedName("title")
    var songName: String,

    @SerializedName("duration")
    var duration: Int,

    @SerializedName("preview")
    var song_url: String,

    @SerializedName("contributors")
    var contributors: List<Contributors>
)

data class Contributors(
    @SerializedName("name")
    var singerName: String,

    @SerializedName("picture_big")
    var cover_url: String
)