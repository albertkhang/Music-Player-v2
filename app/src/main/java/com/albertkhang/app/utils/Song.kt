package com.albertkhang.app.utils

data class Song constructor(
    var songName: String? = null,
    var singerName: String? = null,

    var cover_url: String? = null,
    var song_url: String? = null,

    var duration: Int = 0,
    var isLiked: Boolean = false
)