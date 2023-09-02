package com.example.mytestingapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mytestingapp.R
import com.example.mytestingapp.data.model.PhotoDataModel
import com.example.mytestingapp.ui.activities.MainActivity.Companion.AUTHOR_PHOTO_ID_KEY
import com.example.mytestingapp.ui.activities.MainActivity.Companion.DOWNLOAD_FILE_FOLDER
import com.example.mytestingapp.ui.activities.SecondActivity
import com.google.gson.Gson
import java.io.File


class AuthorPhotosLoadAdapter(private val activity: Activity) : RecyclerView.Adapter<AuthorPhotosLoadAdapter.DataViewHolder>() {

    private var data: ArrayList<PhotoDataModel> = ArrayList<PhotoDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.romitem_data, parent,false)
        )

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int){
         val dataModel = data?.get(position)
        holder.tvName.text = dataModel?.author

        if(dataModel?.downloadUrl!!.contains(DOWNLOAD_FILE_FOLDER)){
            Glide
                .with(activity)
                .load(File(dataModel?.downloadUrl))
                .centerCrop()
                .apply(RequestOptions().override(140, 180))
                .placeholder(R.drawable.ic_browser_not_supported_24)
                .into(holder.imgView)
        }else{
            Glide
                .with(activity)
                .load(dataModel?.downloadUrl)
                .centerCrop()
                .apply(RequestOptions().override(140, 180))
                .placeholder(R.drawable.ic_browser_not_supported_24)
                .into(holder.imgView)
        }



        holder.itemView.setOnClickListener {
            val dataModel = data?.get(position)
            nextActivityCall(dataModel)
        }

    }

    // data view holder class
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var tvName: TextView
         var imgView: ImageView

        init {
            tvName = itemView.findViewById(R.id.tvAuthorName) as TextView
            imgView = itemView.findViewById(R.id.imgViewLoad) as ImageView
        }
    }

    fun updatedDataSet(mList: ArrayList<PhotoDataModel>?) {
        if (mList != null) {
            this.data = (mList)
        }
        notifyDataSetChanged()
    }

    private fun nextActivityCall(dataModel: PhotoDataModel?) {
        val dataStr = Gson().toJson(dataModel)
        activity.startActivity(Intent(activity, SecondActivity::class.java)
            .putExtra(AUTHOR_PHOTO_ID_KEY,dataStr))
    }

}