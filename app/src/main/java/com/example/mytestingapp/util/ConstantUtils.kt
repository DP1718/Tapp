package com.example.mytestingapp.util

import android.content.Context
import android.widget.Toast

object ConstantUtils {

    fun Context.showToastLong(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }
}