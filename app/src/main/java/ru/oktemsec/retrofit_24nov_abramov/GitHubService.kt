package ru.oktemsec.retrofit_24nov_abramov

import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    fun repoContributors(@Path("owner") owner:String, @Path("repo") repo:String): Call<List<Contribution>>

}