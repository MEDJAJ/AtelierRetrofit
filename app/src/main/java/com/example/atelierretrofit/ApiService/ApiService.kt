package com.example.atelierretrofit.ApiService

import com.example.atelierretrofit.Model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>
}
