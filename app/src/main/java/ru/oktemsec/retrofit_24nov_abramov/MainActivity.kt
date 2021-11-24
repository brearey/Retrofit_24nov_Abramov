package ru.oktemsec.retrofit_24nov_abramov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.view.View

import android.widget.TextView




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button:Button = findViewById(R.id.button)
        button.setOnClickListener {
            val gitHubService: GitHubService = RetrofitClient.getClient().create(GitHubService::class.java)
            val call: Call<List<Contribution>> = gitHubService.repoContributors("square", "picasso")

            call.enqueue(object : Callback<List<Contribution>> {
                override fun onResponse(
                    call: Call<List<Contribution>>,
                    response: Response<List<Contribution>>
                ) {
                    val textView = findViewById<View>(R.id.textView) as TextView
                    textView.text = response.body().toString()
                    Log.d("brearey", response.body().toString())
                }

                override fun onFailure(call: Call<List<Contribution>>, throwable: Throwable) {
                    val textView = findViewById<View>(R.id.textView) as TextView
                    textView.text = "Что-то пошло не так: " + throwable.message.toString()
                    Log.d("brearey", throwable.message.toString())
                }
            })
        }
    }
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}