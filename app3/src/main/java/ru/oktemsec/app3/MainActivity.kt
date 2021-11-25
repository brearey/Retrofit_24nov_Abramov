package ru.oktemsec.app3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView:TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.INVISIBLE

        button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val gitHubService: GitHubService = retrofit.create(GitHubService::class.java)
            val call: Call<List<Repos>> = gitHubService.getRepos("brearey")

            call.enqueue(object : Callback<List<Repos>> {
                override fun onFailure(call: Call<List<Repos>>, t: Throwable) {
                    Log.e("brearey", "Failed", t)
                }
                override fun onResponse(
                    call: Call<List<Repos>>,
                    response: Response<List<Repos>>
                ) {
                    Log.d("brearey", "Response received: ${response.body()}")
                    if (response.isSuccessful) {
                        textView.text = response.body().toString() + "\n"

                        response.body()?.forEach {
                            textView.append(it.name + "\n")
                        }
                        progressBar.visibility = View.INVISIBLE
                    }
                    else {
                        try {
                            textView.text = response.errorBody().toString()
                            progressBar.visibility = View.INVISIBLE
                        } catch (e:IOException) {
                            Log.e("brearey", e.message.toString())
                        }
                    }
                }
            })
        }
    }
}