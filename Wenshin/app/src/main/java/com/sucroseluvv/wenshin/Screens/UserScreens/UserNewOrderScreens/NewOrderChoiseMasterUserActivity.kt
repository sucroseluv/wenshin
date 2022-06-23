package com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.MasterInfoUserActivity
import com.sucroseluvv.wenshin.models.Master
import com.sucroseluvv.wenshin.models.getMasterFio
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_new_order_choise_master_user.*
import retrofit2.Response
import javax.inject.Inject

class NewOrderChoiseMasterUserActivity : AppCompatActivity() {

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order_choise_master_user)
        App.Inst.AppComponent.inject(this)
        createActivity()
        title="Выбор мастера"
    }
    val consumer: SingleObserver<Response<Array<Master>>> = object:
        SingleObserver<Response<Array<Master>>> {
        override fun onSubscribe(d: Disposable) {
            //txt.text = "subscribed"
        }

        override fun onSuccess(t: Response<Array<Master>>) {
            //txt.text = "size: " + t.body()?.size

            mastersList.removeAllViews()
            if(t.body() != null)
                t.body()!!.forEach { master ->
                    val masterView = LayoutInflater.from(this@NewOrderChoiseMasterUserActivity)
                        .inflate(R.layout.new_order_master_item_choise, mastersList, false)
                    val img = masterView.findViewById<ImageView>(R.id.avatar)
                    Glide.with(this@NewOrderChoiseMasterUserActivity)
                        .load(API.images + master.avatar)
                        .into(img)
                    masterView.findViewById<TextView>(R.id.sketchname).text = getMasterFio(master)
                    masterView.findViewById<TextView>(R.id.price).text = master.price.toString()
                    masterView.findViewById<ImageView>(R.id.info).setOnClickListener {
                        startActivity(Intent(this@NewOrderChoiseMasterUserActivity, MasterInfoUserActivity::class.java).putExtra("id", master.id))
                    }
                    masterView.setOnClickListener{ v ->
                        setResult(200, Intent().putExtra("next", intent.getBooleanExtra("next", false))
                            .putExtra("master", Gson().toJson(master)))
                        finish()
                    }
                    mastersList.addView(masterView)
                }
        }

        override fun onError(e: Throwable) {
            //txt.text = e.message
        }
    }
    fun createActivity() {
        networkService.fetchMasters().subscribe(consumer)
    }

    fun createFake() {
        Glide.with(this)
            .load("https://syndicatetattoo.ru/s/article/112/cnt_4_1.jpg")
            .into(avatar)

        Glide.with(this)
            .load("https://tattoo-sketches.com/wp-content/uploads/2018/08/Sergei-Menshhikov-.png")
            .into(avatar1)

        Glide.with(this)
            .load("https://styletattoo.ru/image/data/viktor-rassadniy.jpg")
            .into(avatar2)
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