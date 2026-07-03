/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.mohit.emovibes.di

import com.mohit.emovibes.lyrics.LyricsHelper
import com.mohit.emovibes.lyrics.LyricsPreloadManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LyricsHelperEntryPoint {
    fun lyricsHelper(): LyricsHelper
    fun lyricsPreloadManager(): LyricsPreloadManager
}