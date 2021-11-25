package ru.oktemsec.app4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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

        val textView: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val editText: EditText = findViewById(R.id.editText)

        progressBar.visibility = View.INVISIBLE

        button.setOnClickListener {
            if (editText.text.toString() != "") {
                progressBar.visibility = View.VISIBLE
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val gitHubService: GitHubService = retrofit.create(GitHubService::class.java)
                val call: Call<GitResult> = gitHubService.getUsers(editText.text.toString())

                call.enqueue(object : Callback<GitResult> {
                    override fun onFailure(call: Call<GitResult>, t: Throwable) {
                        Log.e("brearey", "Failed", t)
                    }
                    override fun onResponse(
                        call: Call<GitResult>,
                        response: Response<GitResult>
                    ) {
                        Log.d("brearey", "Response received: ${response.body()}")
                        if (response.isSuccessful && response.body()?.items?.size!! > 0) {
                            val result: GitResult? = response.body()
                            val user = "Ссылка на Github: " + (result?.items?.get(0)?.html_url)
                            textView.text = user

                            progressBar.visibility = View.INVISIBLE
                        }
                        else {
                            try {
                                textView.text = response.errorBody().toString()
                                progressBar.visibility = View.INVISIBLE
                            } catch (e: IOException) {
                                Log.e("brearey", e.message.toString())
                            }
                        }
                    }
                })
            }
            else {
                editText.hint = "Введите username"
            }
        }
    }
}