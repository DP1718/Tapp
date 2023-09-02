package com.example.mytestingapp.util

import android.util.Log
import com.example.mytestingapp.BuildConfig

object JLog {

    const val LOG_TAG = "LOGPRINT"

    private val LogDebug = BuildConfig.DEBUG
    fun logVerbose(message: String){
        if(LogDebug) {
            Log.v(LOG_TAG,message)
        }
    }

    fun logError(message: String){
        if(LogDebug) {
            Log.e(LOG_TAG,message)
        }
    }

    fun logInfo(message: String){
        if(LogDebug) {
            Log.i(LOG_TAG,message)
        }
    }

    fun logWarning(message: String){
        if(LogDebug) {
            Log.w(LOG_TAG,message)
        }
    }

    fun logDebug(message: String){
        if(LogDebug) {
            Log.d(LOG_TAG,message)
        }
    }
}