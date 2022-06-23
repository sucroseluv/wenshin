package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.responses.UserHistoryResponse
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_history_user.*
import retrofit2.Response
import javax.inject.Inject

class HistoryUserActivity : AppCompatActivity() {

    val consumer = object: SingleObserver<Response<Array<UserHistoryResponse>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<UserHistoryResponse>>) {
            val orders = t.body()
            if(orders != null && orders?.size > 0) {
                val inflater = LayoutInflater.from(this@HistoryUserActivity)
                historyList.removeAllViews()
                orders.forEach { s ->
                    val view1 = inflater.inflate(R.layout.user_order_history_item, historyList, false)
                    Glide.with(this@HistoryUserActivity)
                        .load(API.images + s.image)
                        .into(view1.findViewById<ImageView>(R.id.image))
                    view1.findViewById<TextView>(R.id.sketchname).text = s.name
                    if(s.rate != null) {
                        view1.findViewById<RatingBar>(R.id.rating).progress = s.rate * 2
                        view1.findViewById<TextView>(R.id.notRated).visibility = View.GONE
                        view1.findViewById<ConstraintLayout>(R.id.rated).visibility = View.VISIBLE
                    }
                    view1.setOnClickListener {
                        startActivity(Intent(this@HistoryUserActivity, HistoryOrderInfoUserActivity::class.java).putExtra("id", s.id))
                    }

                    historyList.addView(view1)
                }
            } else {
                emptyText.visibility = View.VISIBLE
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error ${e.message}")
        }

    }
    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_user)
        title = "Завершенные заказы"
        App.Inst.AppComponent.inject(this)
        createActivity()
    }

    fun createActivity() {
        networkService.getUserHistory().subscribe(consumer)
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