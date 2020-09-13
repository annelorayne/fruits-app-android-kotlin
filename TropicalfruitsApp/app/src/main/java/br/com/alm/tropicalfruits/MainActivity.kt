package br.com.alm.tropicalfruits

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.alm.tropicalfruits.adapters.FruitAndVegAdapter
import br.com.alm.tropicalfruits.models.Fruit
import br.com.alm.tropicalfruits.models.Results
import br.com.alm.tropicalfruits.remotedatasource.FruitsAPIInterface
import br.com.alm.tropicalfruits.remotedatasource.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var textViewErrors: TextView
    private lateinit var fruitsList: List<Fruit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.fruitsListView)
        searchView = findViewById(R.id.searchView)
        textViewErrors = findViewById(R.id.textViewErrors)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val adapter = listView.adapter as FruitAndVegAdapter
                    val fruitsListFiltered = fruitsList.filter {
                        it.name.toLowerCase().contains(query.toLowerCase())
                    }
                    if (fruitsListFiltered.isEmpty()) {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.fruit_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    adapter.setDataSource(fruitsListFiltered)
                    listView.adapter = adapter
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val adapter = listView.adapter as FruitAndVegAdapter
                    val fruitsListFiltered = fruitsList.filter {
                        it.name.toLowerCase().contains(newText.toLowerCase())
                    }
                    if (fruitsListFiltered.isEmpty()) {
                        Toast.makeText(
                            baseContext,
                            getString(R.string.fruit_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    adapter.setDataSource(fruitsListFiltered)
                    listView.adapter = adapter
                }
                return false
            }
        })
        getFruitsData()
    }

    fun createAdapter(list: List<Fruit>) {
        val adapter = FruitAndVegAdapter(
            baseContext,
            list
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFruit = list[position]
            val detailIntent = FruitDetailActivity.newIntent(baseContext, selectedFruit.name)
            startActivity(detailIntent)
        }
    }

    private fun getFruitsData() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(getString(R.string.fruit_api_url))

        val endpoint = retrofitClient.create(FruitsAPIInterface::class.java)
        val callback = endpoint.getFruits()

        callback.enqueue(object : Callback<Results> {
            override fun onFailure(call: Call<Results>, t: Throwable) {
                textViewErrors.visibility = View.VISIBLE
                if (t.message?.contains(getString(R.string.network_error_details), true)!!) {
                    textViewErrors.text = getString(R.string.network_error)
                } else {
                    textViewErrors.text = t.message
                }
            }

            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                if (response.isSuccessful) {
                    textViewErrors.visibility = View.GONE
                    val fruits: List<Fruit>? = response.body()?.results
                    if (fruits != null) {
                        fruitsList = fruits
                        createAdapter(fruitsList)
                    }
                } else {
                    textViewErrors.visibility = View.VISIBLE
                    textViewErrors.text = response.errorBody().toString()
                }
            }
        })
    }
}