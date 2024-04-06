package com.example.sucrose.Retrofit

import com.example.hu_tao.Retrofit.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call

interface AuthService {
    @FormUrlEncoded
    @POST("OAuth/Token")

    fun getToken(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String

    ): Call<TokenResponse>
}