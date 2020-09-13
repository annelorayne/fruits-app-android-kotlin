package br.com.alm.tropicalfruits.models

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("results")
    var results : List<Fruit>
)