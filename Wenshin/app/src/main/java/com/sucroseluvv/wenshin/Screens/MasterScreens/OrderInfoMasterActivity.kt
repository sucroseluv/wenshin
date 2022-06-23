package com.sucroseluvv.wenshin.Screens.MasterScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.Common.OrderMessagingActivity
import com.sucroseluvv.wenshin.models.responses.MasterOrderInfo
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_order_info_master.*
import kotlinx.android.synthetic.main.activity_order_info_master.consultDate
import kotlinx.android.synthetic.main.activity_order_info_master.status
import kotlinx.android.synthetic.main.activity_order_info_user.amount
import kotlinx.android.synthetic.main.activity_order_info_user.messaging
import kotlinx.android.synthetic.main.activity_order_info_user.orderNumber
import kotlinx.android.synthetic.main.activity_order_info_user.sessionsList
import kotlinx.android.synthetic.main.activity_order_info_user.sketchDescription
import kotlinx.android.synthetic.main.activity_order_info_user.sketchHours
import kotlinx.android.synthetic.main.activity_order_info_user.sketchImage
import kotlinx.android.synthetic.main.activity_order_info_user.sketchName
import kotlinx.android.synthetic.main.activity_order_info_user.userEmail
import kotlinx.android.synthetic.main.activity_order_info_user.userName
import kotlinx.android.synthetic.main.order_info_master_session_item.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class OrderInfoMasterActivity : AppCompatActivity() {

    var orderId: Int = -1
    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info_master)
        App.Inst.AppComponent.inject(this)
        title = "Детали заказа"
        orderId = intent.getIntExtra("id", -1)
        if(orderId != -1)
            loadInfo()
    }

    fun loadInfo() {
        networkService.getMasterOrder(orderId).subscribe(consumer)
    }

    val consumer = object: SingleObserver<Response<MasterOrderInfo>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<MasterOrderInfo>) {
            val info = t.body()
            if (info != null) {
                if(info.master_id == 4) {
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

    val completeConsumer = object: SingleObserver<Response<ResponseBody>> {
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

    fun stringifyDate(tempDate: Date): String {
        val day = tempDate.date
        val month = tempDate.month + 1
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month < 10) "0" + (month) else (month)
        return "$formatDay.$formatMonth.2022"
    }

    fun stringifyHours(hours: Int): String {
        val count = hours
        var hourText = "час"
        if(arrayOf(2,3,4,22,23,24).any { n -> n==count }) hourText = "часа"
        if(arrayOf(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,25,26,27,28,29,30).any { n -> n==count }) hourText = "часов"
        return "${hours} ${hourText}"
    }

    fun setupUserInfo(info: MasterOrderInfo) {
        orderNumber.text = info.id.toString()
        status.text = if(info.status == "orderInProgress") "Активен" else "Завершен"

        Glide.with(this).load(API.images + info.image).into(sketchImage)
        sketchName.text = info.sketchName
        sketchDescription.text = info.description
        sketchHours.text = stringifyHours(info.working_hours)

        userName.text = info.name
        userEmail.text = info.email
        userPhone.text = info.phone
        messaging.setOnClickListener {
            startActivity(Intent(this, OrderMessagingActivity::class.java).putExtra("id", info.id))
        }
        amount.text = info.amount.toString()

        sessionsList.removeAllViews()
        val inflater = LayoutInflater.from(this)
        info.sessions.forEach { s ->
            val view = inflater.inflate(R.layout.order_info_master_session_item, sessionsList, false)
            view.findViewById<TextView>(R.id.consultDate).text = stringifyDate(s.date)
            view.findViewById<TextView>(R.id.startTimeConsult).text = s.hours.first().toString()
            view.findViewById<TextView>(R.id.endTimeConsult).text = (s.hours.last()+1).toString()
            view.findViewById<TextView>(R.id.duration).text = stringifyHours(s.hours.size)
            view.findViewById<TextView>(R.id.amount).text = (info.price * s.hours.size).toString()

            val statusTv = view.findViewById<TextView>(R.id.status)
            if(s.status == "inProgress") statusTv.text = "Ожидание сеанса"
            else if(s.status == "completed") statusTv.text = "Завершен"
            else if(s.status == "cancelled") statusTv.text = "Отменен"
            if(s.isPaid != null && s.isPaid != 0) {
                view.findViewById<TextView>(R.id.isPaid).text = "Оплачен"
            }

            val dateAndTime = Calendar.getInstance()
            val now = Date(
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
            val sessionMonth = s.date.month+1
            val sessionDate = s.date.date
            val sessionGone = (now.month+1>sessionMonth) || (now.month+1==sessionMonth) && (now.date>=sessionDate)

            view.findViewById<LinearLayout>(R.id.managementBlock).visibility = if(s.status == "inProgress" && sessionGone) View.VISIBLE else View.GONE
            view.findViewById<Button>(R.id.completeButton).setOnClickListener {
                networkService.setCompleteToSession(s.id!!).subscribe(completeConsumer)
            }
            view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
                networkService.setCancelToSession(s.id!!).subscribe(completeConsumer)
            }

            sessionsList.addView(view)
        }
    }

    fun setupConsultInfo(info: MasterOrderInfo) {
        regularOrder.visibility = View.GONE
        consultOrder.visibility = View.VISIBLE
        val session = info.sessions.first()

        consultOrderId.text = "Заказ №${info.id}"
        consultDate.text = stringifyDate(info.sessions.first().date)
        val formatStart = if(session.hours.first() < 10) ("0"+session.hours.first()) else session.hours.first().toString()
        val formatEnd = if(session.hours.first()+1 < 10) ("0"+(session.hours.first()+1)) else (session.hours.first()+1).toString()
        consultTime.text = "с ${formatStart}:00 до ${formatEnd}:00"
        consultUserName.text = info.name
        consultUserEmail.text = info.email
        consultUserPhone.text = info.phone
        consultMessaging.setOnClickListener {
            startActivity(Intent(this, OrderMessagingActivity::class.java).putExtra("id", info.id))
        }
        consultNotDone.setOnClickListener {
            networkService.setCompleteToSession(info.id).subscribe(completeConsumer)
        }
        consultDone.setOnClickListener {
            networkService.setCancelToSession(info.id).subscribe(completeConsumer)
        }

        val dateAndTime = Calendar.getInstance()
        val now = Date(
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH))
        val sessionMonth = session.date.month+1
        val sessionDate = session.date.date
        val sessionGone = (now.month+1>sessionMonth) || (now.month+1==sessionMonth) && (now.date>=sessionDate)

        consultManagementBlock.visibility = if(session.status == "inProgress" && sessionGone) View.VISIBLE else View.GONE

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