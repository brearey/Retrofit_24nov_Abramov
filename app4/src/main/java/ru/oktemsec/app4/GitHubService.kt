package ru.oktemsec.app4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {
    @GET("/search/users")
    fun getUsers(@Query("q") name: String = "brearey"): Call<GitResult>
}