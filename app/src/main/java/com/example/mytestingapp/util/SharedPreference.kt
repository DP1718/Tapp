package com.example.mytestingapp.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Shared Preference Util class
 */
class SharedPreferenceUtil(val context: Context) {

    private val PREFS_NAME = "data_pref"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, text: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putString(KEY_NAME, text)

        editor!!.commit()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()
    }

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putLong(KEY_NAME, value)

        editor.commit()
    }

    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putBoolean(KEY_NAME, status!!)

        editor.commit()
    }

    fun getValue(KEY_NAME: String): String? {

        return sharedPref.getString(KEY_NAME, null)


    }
    fun getValue(KEY_NAME: String,DEFAULT_VALUE:String): String? {

        return sharedPref.getString(KEY_NAME, DEFAULT_VALUE)
    }

    fun getValueInt(KEY_NAME: String): Int {

        return sharedPref.getInt(KEY_NAME, 0)
    }


    fun getValueInt(KEY_NAME: String,DEFAULT_VALUE:Int): Int {
        return sharedPref.getInt(KEY_NAME, DEFAULT_VALUE)
    }

    fun getValueLong(KEY_NAME: String): Long {

        return sharedPref.getLong(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {

        return sharedPref.getBoolean(KEY_NAME, defaultValue)

    }


    /**
     * clear all data from shared preference.
     */
    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.commit()
    }


    /**
     * remove value from shared preference as key wise.
     */
    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }
}