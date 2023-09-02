package com.example.mytestingapp.util

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File


class DownloadUtils {

     fun downloadImage(context: Context, url: String, filename:String, filePath:String) {
//        val filename = url.substring(url.lastIndexOf("/") + 1)
//            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/UnifiedClothes/" + filename)
        val file = File(filePath + filename)
        Log.d("Environment", "Environment extraData=" + filePath)
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(filename)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val referenceID = downloadManager.enqueue(request)
         Log.d("Environment", "Environment extraData===" + referenceID)
    }
}