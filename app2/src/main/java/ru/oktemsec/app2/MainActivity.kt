package ru.oktemsec.app2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar:ProgressBar = findViewById(R.id.progressBar)
        val button:Button = findViewById(R.id.button)
        val textView:TextView = findViewById(R.id.textView)
        progressBar.visibility = View.INVISIBLE

        button.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val gitHubService: GitHubService = RetrofitClient.getClient().create(GitHubService::class.java)
            val call: Call<User> = gitHubService.getUser("brearey")

            call.enqueue(object : Callback<User> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    // response.isSuccessfull() is true if the response code is 2xx
                    if (response.isSuccessful) {
                        val user: User? = response.body()

                        // Получаем json из github-сервера и конвертируем его в удобный вид
                        if (user != null) {
                            textView.setText(
                                """
                                        Аккаунт Github: ${user.name}
                                        Сайт: ${user.blog}
                                        Компания: ${user.company}
                                        """.trimIndent()
                            )
                        }
                        progressBar.visibility = View.INVISIBLE
                    } else {
                        val statusCode: Int = response.code()

                        // handle request errors yourself
                        val errorBody: ResponseBody? = response.errorBody()
                        try {
                            if (errorBody != null) {
                                textView.text = errorBody.string()
                                Log.d("brearey", errorBody.string())
                            }
                            progressBar.visibility = View.INVISIBLE
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<User?>?, throwable: Throwable) {
                    textView.text = "Что-то пошло не так: " + throwable.message.toString()
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