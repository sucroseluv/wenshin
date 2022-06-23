package com.sucroseluvv.wenshin.network

import com.sucroseluvv.wenshin.models.Master
import com.sucroseluvv.wenshin.models.MasterBusyness
import com.sucroseluvv.wenshin.models.Message
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.requests.*
import com.sucroseluvv.wenshin.models.responses.*
import com.sucroseluvv.wenshin.network.services.AuthService
import com.sucroseluvv.wenshin.network.services.MasterService
import com.sucroseluvv.wenshin.network.services.OrderService
import com.sucroseluvv.wenshin.network.services.SketchService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.log

class NetworkService {
    private var sketchService: SketchService
    private var authService: AuthService
    private var masterService: MasterService
    private var orderService: OrderService
    constructor(serviceProvider: ServiceProvider) {
        sketchService = serviceProvider.createService(SketchService::class.java)
        authService = serviceProvider.createService(AuthService::class.java)
        masterService = serviceProvider.createService(MasterService::class.java)
        orderService = serviceProvider.createService(OrderService::class.java)
    }

    // auth
    fun login(loginRequest: LoginRequest): Single<Response<ResponseBody>> {
        return authService.login(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun register(request: RegisterRequest): Single<Response<SuccessResponse>> {
        return authService.register(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun accountInfo(): Single<Response<UserAccountResponse>> {
        return authService.account()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun changeAccountInfo(request: ChangeAccountInfoRequest): Single<Response<UserAccountResponse>> {
        return authService.changeAccountInfo(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    // sketches
    fun fetchSketches(): Single<Response<Array<Sketch>>> {
        return sketchService.fetchSketches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun fetchSketches(tags: Array<String>? = null, keyword: String? = null): Single<Response<Array<Sketch>>> {
        return sketchService.fetchSketches(tags, keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun fetchFavSketches(): Single<Response<Array<Sketch>>> {
        return sketchService.fetchFavSketches()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun fetchSketch(id: Int): Single<Response<Sketch>> {
        return sketchService.fetchSketch(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun fetchTags(): Single<Response<Array<String>>> {
        return sketchService.fetchTags()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun favoriteSketch(sketchId: Int, favorite: Boolean = true): Single<Response<FavoriteResponse>> {
        return sketchService.favorite(FavoriteRequest(favorite, sketchId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun uploadSketchImage(part: MultipartBody.Part): Single<Response<ImageUploadResponse>> {
        return sketchService.uploadSketchImage(part)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun createSketch(request: CreateSketchRequest): Single<Response<ResponseBody>> {
        return sketchService.createSketch(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchMasters(): Single<Response<Array<Master>>> {
        return masterService.fetchMasters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchMasterBusyness(masterId: Int): Single<Response<Array<MasterBusyness>>> {
        return masterService.fetchMasterBusyness(masterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchMasterInfo(masterId: Int): Single<Response<MasterInfo>> {
        return masterService.fetchMasterInfo(masterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun fetchMasterSchedule() : Single<Response<Array<ScheduleOrderResponse>>> {
        return orderService.schedule()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createOrder(orderInfo: CreateOrderRequest): Single<Response<IdResponse>> {
        return orderService.createOrder(orderInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createSessions(info: ExtraSessionsRequest): Single<Response<IdResponse>> {
        return orderService.createSessions(info)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun createConsultation(session: SessionRequest): Single<Response<IdResponse>> {
        return orderService.createConsultation(session)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserOrders(): Single<Response<Array<UserOrderInfo>>> {
        return orderService.getUserOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMasterOrders(): Single<Response<Array<MasterShortOrderInfo>>> {
        return orderService.getMasterOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserOrder(id: Int): Single<Response<UserOrderInfo>> {
        return orderService.getUserOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMasterOrder(id: Int): Single<Response<MasterOrderInfo>> {
        return orderService.getMasterOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserHistory(): Single<Response<Array<UserHistoryResponse>>> {
        return orderService.getUserHistoryOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserOrderHistoryInfo(id: Int): Single<Response<UserHistoryInfoResponse>> {
        return orderService.getUserHistoryOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun sendFeedbackToOrder(id: Int, rate: Int, comment: String?) : Single<Response<RateResponse>> {
        return orderService.sendFeedback(SendFeedbackRequest(id, comment, rate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun sendMessageToOrder(id: Int, message: String) : Single<Response<Message>> {
        return orderService.sendMessage(SendMessageRequest(id, message))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setPaidToSession(sessionId: Int) : Single<Response<ResponseBody>> {
        return orderService.setSessionPaid(SessionIdRequest(sessionId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setCompleteToSession(sessionId: Int) : Single<Response<ResponseBody>> {
        return orderService.setSessionCompleted(SessionIdRequest(sessionId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setCancelToSession(sessionId: Int) : Single<Response<ResponseBody>> {
        return orderService.setSessionCancelled(SessionIdRequest(sessionId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMessages(orderId: Int, messageId: Int? = null) : Single<Response<Array<Message>>> {
        return orderService.messages(orderId, messageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}