package br.com.alm.tropicalfruits.models

import com.google.gson.annotations.SerializedName

data class Fruit(
    @SerializedName("tfvname")
    var name : String,
    @SerializedName("botname")
    var botname : String,
    @SerializedName("othname")
    var othname : String,
    @SerializedName("description")
    var description : String,
    @SerializedName("imageurl")
    var imageUrl : String
)