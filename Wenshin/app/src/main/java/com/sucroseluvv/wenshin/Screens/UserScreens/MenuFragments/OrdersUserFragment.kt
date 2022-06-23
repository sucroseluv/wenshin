package com.sucroseluvv.wenshin.Screens.UserScreens.MenuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.CreateConsultationActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.NewOrderUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.OrderInfoUserActivity
import com.sucroseluvv.wenshin.common.WenshinNotificationManager
import com.sucroseluvv.wenshin.models.responses.UserOrderInfo
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_orders_user.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class OrdersUserFragment : Fragment() {

    val orderUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == 200) {
            loadOrders()
            if (res.data?.getIntExtra("newOrder", -1) != -1) {
                orderInfoUserActivityLauncher.launch(
                    Intent(
                        activity,
                        OrderInfoUserActivity::class.java
                    ).putExtra("id", res.data?.getIntExtra("newOrder", -1))
                )
            }
        }
    }
    val orderInfoUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        { res->
            if(res.resultCode == 200) {
                loadOrders()
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var networkService: NetworkService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.Inst.AppComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders_user, container, false)
        createOrders(view)
        loadOrders()
        return view
    }
    fun stringifyDate(tempDate: Date): String {
        val day = tempDate.date
        val month = tempDate.month
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
        return "$formatDay.$formatMonth.2022"
    }

    val consumer = object: SingleObserver<Response<Array<UserOrderInfo>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<UserOrderInfo>>) {
            orderList.removeAllViews()
            if(t.body() != null) {
                val res = t.body()
                res?.forEach { order ->
                    val view = LayoutInflater.from(this@OrdersUserFragment.activity).inflate(R.layout.user_order_info_item, orderList, false)
                    val isConsult = if(order.master_id == 4) true else false

                    view.findViewById<TextView>(R.id.number).text = order.id.toString()
                    view.findViewById<TextView>(R.id.sketchName).text = order.sketchName.toString()
                    val count = order.working_hours
                    var hourText = "час"
                    if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
                    if(arrayOf(5,6,7,8,9,10).any { n -> n==count }) hourText = "часов"
                    view.findViewById<TextView>(R.id.sketchDurability).text = "${order.working_hours} ${hourText}"
                    view.findViewById<TextView>(R.id.userName).text = order.name.toString()
                    view.findViewById<TextView>(R.id.price).text = order.amount.toString()
                    if(isConsult) {
                        view.findViewById<TextView>(R.id.sketchLabel).text = "Консультация"
                        view.findViewById<TextView>(R.id.sketchName).visibility = View.GONE
                        view.findViewById<TextView>(R.id.sketchDurability).visibility = View.GONE
                        view.findViewById<TextView>(R.id.dash).visibility = View.GONE
                        view.findViewById<TextView>(R.id.masterLabel).text = "Консультант: "
                        view.findViewById<TextView>(R.id.nextSeansTextView).text = "Сеанс: "
                        view.findViewById<TextView>(R.id.price).visibility = View.GONE
                        view.findViewById<TextView>(R.id.ruble).visibility = View.GONE
                    }

                    if(order.sessions.any { s -> s.status == "inProgress" }){
                        val nextSession =  order.sessions.first { s -> s.status == "inProgress" }
                        val nextSessionText = "${stringifyDate(nextSession.date)} с ${nextSession.hours.first()}:00 до ${nextSession.hours.last()+1}:00"
                        view.findViewById<TextView>(R.id.sessionFromTo).text = nextSessionText
                    } else {
                        view.findViewById<TextView>(R.id.nextSeansTextView).visibility = View.GONE
                        view.findViewById<TextView>(R.id.sessionFromTo).visibility = View.GONE
                    }
                    view.setOnClickListener {
                        orderInfoUserActivityLauncher.launch(Intent(activity,OrderInfoUserActivity::class.java).putExtra("id", order.id))
                    }
                    orderList.addView(view)

                }
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
        }

    }

    fun loadOrders() {
        networkService.getUserOrders().subscribe(consumer)
    }

    fun createOrders(view: View) {
        val createOrder = view.findViewById<ConstraintLayout>(R.id.createOrderButton)
        val createConsultation = view.findViewById<ConstraintLayout>(R.id.createConsultationButton)

        createOrder.setOnClickListener{
            val intent = Intent(activity, NewOrderUserActivity::class.java)
            orderUserActivityLauncher.launch(intent)
        }
        createConsultation.setOnClickListener{
            val intent = Intent(activity, CreateConsultationActivity::class.java)
            orderUserActivityLauncher.launch(intent)
        }
    }
}