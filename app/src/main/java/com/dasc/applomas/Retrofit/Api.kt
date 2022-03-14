package com.dasc.applomas.Retrofit

import com.dasc.applomas.Modelos.RespuestaData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface Api {
    @FormUrlEncoded
    @POST
    suspend fun FolioUltimo(
        @Url url:String,
        @Field("idAsunto") idAsunto: String
    ): Response<RespuestaData>

    @POST
    suspend fun ObtenAsuntos(
        @Url url:String,
        @Field("idAsunto") idAsunto: String
    ): Response<RespuestaData>

    @POST
    suspend fun getAsuntos(
        @Url url:String,
    ): Response<RespuestaData>
}