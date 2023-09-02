package com.example.mytestingapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.mytestingapp.R
import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.databinding.ActivitySecondBinding
import com.example.mytestingapp.ui.activities.MainActivity.Companion.DOWNLOAD_FILE_FOLDER
import com.google.gson.Gson
import java.io.File

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var photoDataModel: PhotoDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if(bundle != null){
              val strData = bundle.getString(MainActivity.AUTHOR_PHOTO_ID_KEY)
            photoDataModel = Gson().fromJson(strData, PhotoDataModel::class.java)
        }

        initSetup()
    }

    private fun initSetup() {
        binding.includeSecondScreen.imgBack.visibility = View.VISIBLE
        binding.includeSecondScreen.tvTitleCommonHeader.text = getString(R.string.lbl_photo_detail)
        binding.includeSecondScreen.imgBack.setOnClickListener {
            finish()
        }

        if(photoDataModel?.downloadUrl!!.contains(DOWNLOAD_FILE_FOLDER)){
            Glide
                .with(this)
                .load(File(photoDataModel?.downloadUrl))
                .centerCrop()
                .placeholder(R.drawable.ic_browser_not_supported_24)
                .into(binding.imgViewDetail)
        }else{
            Glide
                .with(this)
                .load(photoDataModel?.downloadUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_browser_not_supported_24)
                .into(binding.imgViewDetail)
        }
    }
}