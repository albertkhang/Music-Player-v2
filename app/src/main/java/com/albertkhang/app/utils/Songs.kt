package com.albertkhang.app.utils

import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.security.MessageDigest

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
    var contributors: List<Contributors>,

    @SerializedName("album")
    var album: Album,

    var isFavorite: Boolean
) {
    fun singers(): String {
        if (contributors.size < 2) {
            return contributors[0].singerName
        } else {
            var singers = ""

            for (i in contributors.indices) {
                if (i != 0) {
                    singers += ", "
                }

                singers += contributors[i].singerName
            }

            return singers
        }
    }

    fun getSongNameMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest: ByteArray = md.digest(song_url.toByteArray())

        val bigInteger = BigInteger(1, messageDigest)
        var hashText = bigInteger.toString(16)
        while (hashText.length < 32) {
            hashText = "0$hashText"
        }

        return hashText
    }
}

data class Contributors(
    @SerializedName("name")
    var singerName: String
)

data class Album(
    @SerializedName("cover_big")
    var cover_url: String
)

