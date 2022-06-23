package com.sucroseluvv.wenshin.Screens.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.adapters.MessagesAdapter
import com.sucroseluvv.wenshin.models.Message
import com.sucroseluvv.wenshin.models.UserInfo
import com.sucroseluvv.wenshin.models.UserType
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_messaging.*
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderMessagingActivity : AppCompatActivity() {

    var orderId = -1
    var lastMessageId = 0
    var subscription: Disposable? = null
    @Inject
    lateinit var userInfo: UserInfo
    @Inject
    lateinit var networkService: NetworkService

    var adapter: MessagesAdapter = MessagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_messaging)
        App.Inst.AppComponent.inject(this)
        orderId = intent.getIntExtra("id", -1)
        adapter = MessagesAdapter(userInfo)
        if(orderId != -1) {
            title="Заказ №${orderId}"
            if(userInfo.role == UserType.master)
                findViewById<TextView>(R.id.title).text = "Переписка с пользователем"
            recycler_view.adapter = adapter
            recycler_view.itemAnimator = DefaultItemAnimator()
            recycler_view.layoutManager = LinearLayoutManager(this)
            createActivity()
            createTimer()
            send.setOnClickListener {
                sendMessage()
            }
        }

    }

    val consumer = object: SingleObserver<Response<Array<Message>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<Message>>) {
            val messages = t.body()
            if(messages != null && messages.size > 0) {
                adapter.addMessages(messages!!)
                recycler_view.scrollToPosition(adapter.itemCount-1)
                lastMessageId = messages.last().id
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error ${e.message}")
        }

    }

    val sendConsumer = object: SingleObserver<Response<Message>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("sendConsumer", "subscribed")
        }

        override fun onSuccess(t: Response<Message>) {
            val message = t.body()
            if(message != null) {
                adapter.addMessages(arrayOf(message))
                recycler_view.scrollToPosition(adapter.itemCount-1)
                lastMessageId = message.id
            }
        }

        override fun onError(e: Throwable) {
            Log.d("sendConsumer", "error ${e.message}")
        }

    }

    fun createActivity() {
        networkService.getMessages(orderId).subscribe(consumer)
    }

    fun sendMessage() {
        if(message.text == null || message.text.toString().length == 0 ) {
            Toast.makeText(this, "Сообщение не должно быть пустым", Toast.LENGTH_SHORT).show()
            return
        }
        networkService.sendMessageToOrder(orderId, message.text.toString()).subscribe(sendConsumer)
        message.text.clear()
    }

    fun createTimer() {
        subscription = Observable.interval(1000, 1500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                networkService.getMessages(orderId, lastMessageId).subscribe(consumer)
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

    override fun onDestroy() {
        subscription?.dispose()
        super.onDestroy()
    }

}