package br.com.alm.tropicalfruits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.alm.tropicalfruits.models.Fruit
import br.com.alm.tropicalfruits.models.Results
import br.com.alm.tropicalfruits.remotedatasource.FruitsAPIInterface
import br.com.alm.tropicalfruits.remotedatasource.NetworkUtils
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FruitDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "fruit_name"

        fun newIntent(context: Context, fruitName: String): Intent {
            val detailIntent = Intent(context, FruitDetailActivity::class.java)

            detailIntent.putExtra(EXTRA_NAME, fruitName)
            return detailIntent
        }
    }

    private lateinit var textViewErrors: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_detail)

        textViewErrors = findViewById(R.id.textViewErrors)
        val textViewName: TextView = findViewById(R.id.textViewName)
        val textViewBotname: TextView = findViewById(R.id.textViewBotname)
        val textViewOthname: TextView = findViewById(R.id.textViewOthname)
        val textViewDescription: TextView = findViewById(R.id.textViewDescription)
        val textViews = listOf(textViewName, textViewBotname, textViewOthname, textViewDescription)

        val fruitName = intent.extras?.getString(EXTRA_NAME)
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(getString(R.string.fruit_api_url))

        val endpoint = retrofitClient.create(FruitsAPIInterface::class.java)
        if (!fruitName.isNullOrEmpty()) {
            val callback = endpoint.getFruit(fruitName)
            callback.enqueue(object : Callback<Results> {
                override fun onFailure(call: Call<Results>, t: Throwable) {
                    updateTextViewsVisibilities(View.GONE, textViews)
                    if (t.message?.contains(getString(R.string.network_error_details), true)!!) {
                        textViewErrors.text = getString(R.string.network_error)
                    } else {
                        textViewErrors.text = t.message
                    }
                }

                override fun onResponse(call: Call<Results>, response: Response<Results>) =
                    if (response.isSuccessful) {
                        textViewErrors.visibility = View.GONE
                        val fruit: Fruit? = response.body()?.results?.get(0)
                        if (fruit != null) {
                            textViewName.text = fruitName
                            textViewBotname.text = fruit.botname
                            textViewOthname.text = fruit.othname
                            textViewDescription.text = fruit.description
                            updateTextViewsVisibilities(View.VISIBLE, textViews)

                            val imageView: ImageView = findViewById(R.id.imageView)
                            val url = fruit.imageUrl.replace("http:", "https:")
                            Picasso.with(baseContext).load(url).placeholder(R.mipmap.ic_launcher)
                                .into(imageView)
                        } else
                            updateTextViewsVisibilities(View.GONE, textViews)
                        textViewErrors.text = getString(R.string.fruit_not_found_error)
                    } else {
                        updateTextViewsVisibilities(View.GONE, textViews)
                        textViewErrors.text = response.errorBody().toString()
                    }
            })
        }
    }

    private fun updateTextViewsVisibilities(visibility: Int, textViews: List<TextView>) {
        for (textView in textViews) {
            textView.visibility = visibility
        }
        if (View.GONE == visibility) {
            textViewErrors.visibility = View.VISIBLE
        } else if (View.VISIBLE == visibility) {
            textViewErrors.visibility = View.GONE
        }
    }
}
