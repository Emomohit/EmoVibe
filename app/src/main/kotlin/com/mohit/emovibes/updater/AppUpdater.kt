package com.mohit.emovibes.updater

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.mohit.emovibes.BuildConfig
import timber.log.Timber
import java.io.File

object AppUpdater {


    fun downloadAndInstallUpdate(context: Context, downloadUrl: String) {
        try {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(downloadUrl)
            
            // Delete old updates if they exist to avoid clutter
            val destinationFile = File(context.externalCacheDir, "update.apk")
            if (destinationFile.exists()) {
                destinationFile.delete()
            }

            val request = DownloadManager.Request(uri)
                .setTitle("EMOVibes Update")
                .setDescription("Downloading the latest version...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(destinationFile))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadId = downloadManager.enqueue(request)

            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        installApk(ctxt, destinationFile)
                        ctxt.unregisterReceiver(this)
                    }
                }
            }
            context.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to download update")
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        try {
            if (!apkFile.exists()) return

            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.FileProvider",
                apkFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e, "Failed to install APK")
        }
    }
}
