package com.example.mytestingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PhotoDataModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("idnumber"     ) var idnumber    : Int? = null,

    @SerializedName("id"           ) var id          : String? = null,
    @SerializedName("author"       ) var author      : String? = null,
    @SerializedName("width"        ) var width       : Int?    = null,
    @SerializedName("height"       ) var height      : Int?    = null,
    @SerializedName("url"          ) var url         : String? = null,
    @SerializedName("download_url" ) var downloadUrl : String? = null
)
