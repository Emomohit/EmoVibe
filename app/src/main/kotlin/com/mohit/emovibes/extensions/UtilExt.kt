/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.mohit.emovibes.extensions

fun <T> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        null
    }
