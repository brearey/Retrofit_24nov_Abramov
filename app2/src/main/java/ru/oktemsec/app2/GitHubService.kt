package ru.oktemsec.app2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("/users/{username}")
    fun getUser(@Path("username") userName:String): Call<User>
}