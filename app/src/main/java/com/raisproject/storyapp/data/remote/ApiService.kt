package com.raisproject.storyapp.data.remote

import com.raisproject.storyapp.data.response.DetailResponse
import com.raisproject.storyapp.data.response.LoginResponse
import com.raisproject.storyapp.data.response.ErrorResponse
import com.raisproject.storyapp.data.response.StoryResponse
import com.raisproject.storyapp.data.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : ErrorResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") auth: String,
    ) : StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") auth: String
    ) : DetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
        @Header("Authorization") auth: String
    ) : UploadResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
        @Header("Authorization") auth: String
    ) : StoryResponse
}