package br.com.alm.tropicalfruits.remotedatasource

import br.com.alm.tropicalfruits.models.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FruitsAPIInterface {

    @GET("tfvjsonapi.php?search=all")
    fun getFruits() : Call<Results>

    @GET("tfvjsonapi.php")
    fun getFruit(
        @Query("tfvitem") fruitName: String
    ): Call<Results>
}
