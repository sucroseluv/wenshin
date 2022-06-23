package com.sucroseluvv.wenshin.network.services

import com.sucroseluvv.wenshin.models.Message
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.requests.*
import com.sucroseluvv.wenshin.models.responses.*
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface OrderService {

    @POST("/createOrder")
    fun createOrder(
        @Body request: CreateOrderRequest
    ) : Single<Response<IdResponse>>

    @POST("/createSessions")
    fun createSessions(
        @Body request: ExtraSessionsRequest
    ) : Single<Response<IdResponse>>

    @POST("/createConsultation")
    fun createConsultation(
        @Body request: SessionRequest
    ) : Single<Response<IdResponse>>

    @GET("/orders")
    fun getUserOrders() : Single<Response<Array<UserOrderInfo>>>

    @GET("/masterOrders")
    fun getMasterOrders() : Single<Response<Array<MasterShortOrderInfo>>>

    @GET("/order/{id}")
    fun getUserOrder(@Path("id") id: Int) : Single<Response<UserOrderInfo>>

    @GET("/order/{id}")
    fun getMasterOrder(@Path("id") id: Int) : Single<Response<MasterOrderInfo>>

    @GET("/orderHistory")
    fun getUserHistoryOrders() : Single<Response<Array<UserHistoryResponse>>>

    @GET("/orderHistory/{id}")
    fun getUserHistoryOrder(@Path("id") id: Int) : Single<Response<UserHistoryInfoResponse>>

    @POST("/sendFeedback")
    fun sendFeedback(@Body sendFeedbackRequest: SendFeedbackRequest) : Single<Response<RateResponse>>

    @POST("/sendMessage")
    fun sendMessage(@Body sendMessageRequest: SendMessageRequest) : Single<Response<Message>>

    @POST("/setSessionPaid")
    fun setSessionPaid(@Body sessionIdRequest: SessionIdRequest) : Single<Response<ResponseBody>>

    @POST("/setSessionCompleted")
    fun setSessionCompleted(@Body sessionIdRequest: SessionIdRequest) : Single<Response<ResponseBody>>

    @POST("/setSessionCancelled")
    fun setSessionCancelled(@Body sessionIdRequest: SessionIdRequest) : Single<Response<ResponseBody>>

    @GET("/messages")
    fun messages(@Query("orderId") orderId: Int, @Query("messageId") messageId: Int? = null) : Single<Response<Array<Message>>>

    @GET("/schedule")
    fun schedule() : Single<Response<Array<ScheduleOrderResponse>>>

}