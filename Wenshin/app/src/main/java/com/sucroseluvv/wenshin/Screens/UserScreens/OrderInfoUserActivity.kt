 package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.Common.OrderMessagingActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderSeansesUserActivity
import com.sucroseluvv.wenshin.models.Session
import com.sucroseluvv.wenshin.models.requests.ExtraSessionsRequest
import com.sucroseluvv.wenshin.models.requests.getSessionsRequestList
import com.sucroseluvv.wenshin.models.responses.IdResponse
import com.sucroseluvv.wenshin.models.responses.UserOrderInfo
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_order_info_user.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject


 class OrderInfoUserActivity : AppCompatActivity() {

     val choiseSessionsUserActivityLauncher = registerForActivityResult(
         ActivityResultContracts.StartActivityForResult()
     ) { res ->
         if(res.resultCode == 200) {
             val chosenSessions = Gson().fromJson<Array<Session>>(res.data?.getStringExtra("master"),Array<Session>::class.java)
             chosenSessions.forEach { s ->
                 Log.d("ses", "date - ${s.date}")
                 Log.d("ses", "hours - ${s.hours.joinToString(", ")}")
             }
             networkService.createSessions(ExtraSessionsRequest(orderId, getSessionsRequestList(chosenSessions))).subscribe(extraConsumer)
             //setSesions(chosenSessions)
         }
     }

    var orderId: Int = -1
    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.Inst.AppComponent.inject(this)
        setContentView(R.layout.activity_order_info_user)
        title = "Детали заказа"
        orderId = intent.getIntExtra("id", -1)
        if(orderId != -1) {
            loadInfo()
        }
    }

    fun loadInfo() {
        networkService.getUserOrder(orderId).subscribe(consumer)
    }

    val consumer = object: SingleObserver<Response<UserOrderInfo>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<UserOrderInfo>) {
            val info = t.body()
            if (info != null) {
                if(info.master_id == 4){
                    setupConsultInfo(info)
                } else {
                    setupUserInfo(info)
                }
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
        }

    }

    val paidConsumer = object: SingleObserver<Response<ResponseBody>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("paidConsumer", "subscribed")
        }

        override fun onSuccess(t: Response<ResponseBody>) {
            if (t.code() == 200) {
                loadInfo()
            }
        }

        override fun onError(e: Throwable) {
            Log.d("paidConsumer", "error: ${e.message}")
        }

    }
    val extraConsumer = object: SingleObserver<Response<IdResponse>> {
         override fun onSubscribe(d: Disposable) {
             Log.d("paidConsumer", "subscribed")
         }

         override fun onSuccess(t: Response<IdResponse>) {
             if (t.code() == 201) {
                 loadInfo()
             }
         }

         override fun onError(e: Throwable) {
             Log.d("paidConsumer", "error: ${e.message}")
         }

     }


    fun stringifyDate(tempDate: Date): String {
        val day = tempDate.date
        val month = tempDate.month
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
        return "$formatDay.$formatMonth.2022"
    }

    fun stringifyHours(hours: Int): String {
        val count = hours % 10
        var hourText = "час"
        if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
        if(arrayOf(5,6,7,8,9).any { n -> n==count }) hourText = "часов"
        if(hours % 100>=10 && hours % 100 <= 20) hourText = "часов"
        return "${hours} ${hourText}"
    }

    fun setupUserInfo(info: UserOrderInfo) {
        setupExtraSessions(info)

        orderNumber.text = info.id.toString()

        Glide.with(this).load(API.images + info.image).into(sketchImage)
        sketchName.text = info.sketchName
        sketchDescription.text = info.description
        sketchHours.text = stringifyHours(info.working_hours)

        Glide.with(this).load(API.images + info.avatar).into(masterAvatar)
        userName.text = info.name
        userEmail.text = "${info.price.toString()} ₽ за час работы"
        messaging.setOnClickListener {
            startActivity(Intent(this, OrderMessagingActivity::class.java).putExtra("id", info.id))
        }
        var sum = 0.0
        info.sessions.forEach { s ->
            sum += s.hours.size * info.price
        }
        amount.text = (info.amount).toString()
        if(sum > info.amount) {
            amount2.visibility = View.VISIBLE
            textView601.visibility = View.VISIBLE
            textView591.visibility = View.VISIBLE
            amount2.text = sum.toString()
        }

        sessionsList.removeAllViews()
        val inflater = LayoutInflater.from(this)
        info.sessions.forEach { s ->
            val view = inflater.inflate(R.layout.order_infio_user_session_item, sessionsList, false)
            view.findViewById<TextView>(R.id.consultDate).text = stringifyDate(s.date)
            view.findViewById<TextView>(R.id.startTimeConsult).text = s.hours.first().toString()
            view.findViewById<TextView>(R.id.endTimeConsult).text = (s.hours.last()+1).toString()
            view.findViewById<TextView>(R.id.duration).text = stringifyHours(s.hours.size)
            view.findViewById<TextView>(R.id.amount).text = (info.price * s.hours.size).toString()

            val statusTv = view.findViewById<TextView>(R.id.status)
            if(s.status == "inProgress") statusTv.text = "Ожидание сеанса"
            else if(s.status == "completed") statusTv.text = "Завершен"
            else if(s.status == "cancelled") statusTv.text = "Отменен"

            view.findViewById<Button>(R.id.completeButton).setOnClickListener {
                networkService.setPaidToSession(s.id!!).subscribe(paidConsumer)
            }
            if(s.isPaid != null && s.isPaid != 0) {
                view.findViewById<TextView>(R.id.isPaid).visibility = View.VISIBLE
                view.findViewById<Button>(R.id.completeButton).visibility = View.GONE
            } else {
                view.findViewById<TextView>(R.id.isPaid).visibility = View.GONE
                view.findViewById<Button>(R.id.completeButton).visibility = View.VISIBLE
            }

            sessionsList.addView(view)
        }
    }
     fun setupExtraSessions(info: UserOrderInfo) {
         var sessionHours = 0
         for (session in info.sessions) {
             if(session.status != null && (session.status=="inProgress" || session.status=="completed"))
                sessionHours += session.hours.size
         }
         if(sessionHours < info.working_hours) {
             extraSeansesText.text = "Некоторые сеансы были пропущены, нужно распределить ${stringifyHours(info.working_hours-sessionHours)}."
             extraSessions.visibility = View.VISIBLE
         } else {
             extraSessions.visibility = View.GONE
         }

         createExtraSessions.setOnClickListener {
             val intent = Intent(this, NewOrderSeansesUserActivity::class.java)
             intent.putExtra("hours", info.working_hours-sessionHours)
             intent.putExtra("masterId", info.master_id.toString())
             intent.putExtra("minDate", info.sessions.last().date)
             choiseSessionsUserActivityLauncher.launch(intent)
         }
     }

     fun setupConsultInfo(info: UserOrderInfo) {
         regularOrder.visibility = View.GONE
         consultOrder.visibility = View.VISIBLE
         consultationOrderId.text = "Заказ №${info.id}"
         val session = info.sessions.first()
         consultantDate.text = "${session.date.date}.${session.date.month+1}.2022"
         val formatStart = if(session.hours.first() < 10) ("0"+session.hours.first()) else session.hours.first().toString()
         val formatEnd = if(session.hours.first()+1 < 10) ("0"+(session.hours.first()+1)) else (session.hours.first()+1).toString()
         startTimeConsult.text = formatStart
         endTimeConsult.text = formatEnd
         messagingConsult.setOnClickListener {
             startActivity(Intent(this, OrderMessagingActivity::class.java).putExtra("id", info.id))
         }
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