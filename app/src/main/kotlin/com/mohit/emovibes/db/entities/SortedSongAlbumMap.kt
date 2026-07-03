/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.mohit.emovibes.db.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    viewName = "sorted_song_album_map",
    value = "SELECT * FROM song_album_map ORDER BY `index`",
)
data class SortedSongAlbumMap(
    @ColumnInfo(index = true) val songId: String,
    @ColumnInfo(index = true) val albumId: String,
    val index: Int,
)
