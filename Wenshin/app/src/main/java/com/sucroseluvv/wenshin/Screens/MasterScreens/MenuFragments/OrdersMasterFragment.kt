package com.sucroseluvv.wenshin.Screens.MasterScreens.MenuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.MasterScreens.OrderInfoMasterActivity
import com.sucroseluvv.wenshin.models.responses.MasterOrderInfo
import com.sucroseluvv.wenshin.models.responses.MasterShortOrderInfo
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import org.w3c.dom.Text
import retrofit2.Response
import javax.inject.Inject

class OrdersMasterFragment : Fragment() {

    var orderList: LinearLayout? = null

    @Inject
    lateinit var networkService: NetworkService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.Inst.AppComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders_master, container, false)
        createView(view)
        return view
    }

    fun createView(view: View) {
        orderList = view.findViewById(R.id.orderList)
        loadOrders()
    }

    val consumer = object: SingleObserver<Response<Array<MasterShortOrderInfo>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<MasterShortOrderInfo>>) {
            val orders = t.body()
            if(orders != null && orders.size > 0) {
                orderList?.removeAllViews()
                orders.forEach { o ->
                    val view = layoutInflater.inflate(R.layout.master_short_order_info, orderList, false)
                    Glide.with(activity!!).load(API.images+o.image).into(view.findViewById<ImageView>(R.id.image))
                    view.findViewById<TextView>(R.id.clientname).text = o.clientname
                    view.findViewById<TextView>(R.id.sketchname).text = o.name
                    view.findViewById<TextView>(R.id.orderNumber).text = o.id.toString()
                    view.setOnClickListener {
                        startActivity(Intent(activity!!,OrderInfoMasterActivity::class.java).putExtra("id", o.id))
                    }
                    if(o.masterId == 4) {
                        view.findViewById<TextView>(R.id.sketchLabel).text = "Консультация"
                        view.findViewById<TextView>(R.id.sketchname).visibility = View.INVISIBLE
                    }
                    orderList?.addView(view)
                }
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
        }

    }

    fun loadOrders() {
        networkService.getMasterOrders().subscribe(consumer)
    }
}