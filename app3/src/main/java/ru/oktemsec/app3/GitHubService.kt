package ru.oktemsec.app3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("/users/{username}/repos")
    fun getRepos(@Path("username") username:String): Call<List<Repos>>
}