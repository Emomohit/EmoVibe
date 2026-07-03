/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.mohit.emovibes.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.mohit.emovibes.BuildConfig
import com.mohit.emovibes.constants.EnableUpdateNotificationKey
import com.mohit.emovibes.constants.UpdateChannel
import com.mohit.emovibes.constants.UpdateChannelKey

class UpdateCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val dataStore = applicationContext.dataStore

            val isEnabled = dataStore.data.map { it[EnableUpdateNotificationKey] ?: false }.first()
            if (!isEnabled) return Result.success()

            val updateChannel = dataStore.data.map {
                it[UpdateChannelKey]?.let { value ->
                    try { UpdateChannel.valueOf(value) } catch (e: Exception) { UpdateChannel.STABLE }
                } ?: UpdateChannel.STABLE
            }.first()

            if (updateChannel == UpdateChannel.NIGHTLY) return Result.success()

            Updater.getLatestVersionName().onSuccess { latestVersion ->
                if (!Updater.isSameVersion(latestVersion, BuildConfig.VERSION_NAME)) {
                    UpdateNotificationManager.notifyIfNewVersion(applicationContext, latestVersion)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
