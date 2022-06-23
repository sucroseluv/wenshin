package com.sucroseluvv.wenshin.network.services

import com.sucroseluvv.wenshin.models.requests.ChangeAccountInfoRequest
import com.sucroseluvv.wenshin.models.requests.LoginRequest
import com.sucroseluvv.wenshin.models.requests.RegisterRequest
import com.sucroseluvv.wenshin.models.responses.ErrorResponse
import com.sucroseluvv.wenshin.models.responses.SuccessResponse
import com.sucroseluvv.wenshin.models.responses.UserAccountResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @POST("/auth")
    fun login(
        @Body body: LoginRequest
    ): Single<Response<ResponseBody>>

    @POST("/register")
    fun register(
        @Body body: RegisterRequest
    ): Single<Response<SuccessResponse>>

    @GET("/account")
    fun account(): Single<Response<UserAccountResponse>>

    @POST("/changeAccountInfo")
    fun changeAccountInfo(
        @Body body: ChangeAccountInfoRequest
    ): Single<Response<UserAccountResponse>>


}