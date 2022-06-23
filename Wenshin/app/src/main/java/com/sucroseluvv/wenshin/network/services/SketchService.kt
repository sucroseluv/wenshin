package com.sucroseluvv.wenshin.network.services

import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.requests.CreateSketchRequest
import com.sucroseluvv.wenshin.models.requests.FavoriteRequest
import com.sucroseluvv.wenshin.models.responses.FavoriteResponse
import com.sucroseluvv.wenshin.models.responses.ImageUploadResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface SketchService {

    @GET("/sketches")
    fun fetchSketches(
    ) : Single<Response<Array<Sketch>>>

    @GET("/sketches")
    fun fetchSketches(
        @Query("tags") tags: Array<String>?,
        @Query("keyword") keyword: String?
    ) : Single<Response<Array<Sketch>>>

    @GET("/favSketches")
    fun fetchFavSketches() : Single<Response<Array<Sketch>>>

    @POST("/createSketch")
    fun createSketch(@Body request: CreateSketchRequest) : Single<Response<ResponseBody>>

    @GET("/sketch/{id}")
    fun fetchSketch(
        @Path("id") id: Int
    ): Single<Response<Sketch>>

    @POST("/favorite")
    fun favorite(
        @Body request: FavoriteRequest
    ): Single<Response<FavoriteResponse>>

    @GET("/tags")
    fun fetchTags(): Single<Response<Array<String>>>


    @Multipart
    @POST("/uploadSketchImage")
    fun uploadSketchImage(
        @Part image: MultipartBody.Part
    ): Single<Response<ImageUploadResponse>>
}