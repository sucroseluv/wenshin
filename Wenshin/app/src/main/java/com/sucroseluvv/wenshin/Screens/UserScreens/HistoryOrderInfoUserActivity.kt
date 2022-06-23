package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.responses.RateResponse
import com.sucroseluvv.wenshin.models.responses.UserHistoryInfoResponse
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_history_order_info_user.*
import retrofit2.Response
import javax.inject.Inject

class HistoryOrderInfoUserActivity : AppCompatActivity() {

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_order_info_user)
        App.Inst.AppComponent.inject(this)
        val id = intent.getIntExtra("id", -1)
        if(id != -1) {
            title = "Заказ №${id}"
            loadOrder()
        } else {
            finish()
        }
    }

    val ratingConsumer = object: SingleObserver<Response<RateResponse>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("ratingConsumer", "subscribed")
        }

        override fun onSuccess(t: Response<RateResponse>) {
            val response = t.body()
            if(response != null) {
                ratedOrder.visibility = View.VISIBLE
                rateOrder.visibility = View.GONE
                rated.progress = response.rate
                if(response?.comment != null)
                    commented.text = "Комментарий: ${response.comment}"
            } else {
                ErrorDialogFragment("Ошибка добавления комментария").show(supportFragmentManager, "error")
            }
        }

        override fun onError(e: Throwable) {
            Log.d("ratingConsumer", "error ${e.message}")
            ErrorDialogFragment(e.message ?: "Неизвестная ошибка","Ошибка добавления комментария").show(supportFragmentManager, "error")
        }

    }

    val consumer = object: SingleObserver<Response<UserHistoryInfoResponse>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<UserHistoryInfoResponse>) {
            val order = t.body()
            if(order != null) {
                number.text = "Заказ №${order.id}"
                Glide.with(this@HistoryOrderInfoUserActivity).load(API.images + order.image).into(sketchImage)
                sketchName.text = order.name
                workingHours.text = "Объем работы: ${order.working_hours} часов"

                Glide.with(this@HistoryOrderInfoUserActivity).load(API.images + order.avatar).into(masterImage)
                userName.text = order.masterName
                price.text = "Цена за час работы: ${order.price}₽"

                amount.text = "Сумма: ${order.amount}₽"

                if(order.rate == null) {
                    rateOrder.visibility = View.VISIBLE
                    sendRate.setOnClickListener {
                        if(rating.progress <= 0) {
                            Toast.makeText(this@HistoryOrderInfoUserActivity, "Оцените заказ от 1 до 5", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                        networkService.sendFeedbackToOrder(intent.getIntExtra("id", -1), rating.progress, comment.text.toString())
                            .subscribe(ratingConsumer)
                    }
                } else {
                    ratedOrder.visibility = View.VISIBLE
                    rated.progress = order.rate
                    if(order?.comment != null)
                        commented.text = "Комментарий: ${order.comment}"
                }

            } else {

            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error ${e.message}")
        }

    }

    fun loadOrder() {
        networkService.getUserOrderHistoryInfo(intent.getIntExtra("id", -1)).subscribe(consumer)
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}