package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.responses.MasterInfo
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_master_info_user.*
import retrofit2.Response
import javax.inject.Inject

class MasterInfoUserActivity : AppCompatActivity() {

    var masterId: Int = -1

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_info_user)
        App.Inst.AppComponent.inject(this)
        masterId = intent.getIntExtra("id", -1)
        if(masterId != -1) {
            title = "Информация о мастере"
            loadInfo()
        }
    }

    fun loadInfo() {
        networkService.fetchMasterInfo(masterId).subscribe(consumer)
    }

    val consumer = object: SingleObserver<Response<MasterInfo>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribe")
        }

        override fun onSuccess(t: Response<MasterInfo>) {
            val info = t.body()
            if(info != null) {
                sketchname.text = info.name
                Glide.with(this@MasterInfoUserActivity).load(API.images + info.avatar).into(avatar)
                email.text = info.email
                phone.text = info.phone
                price.text = info.price.toString()

                feedbackList.removeAllViews()
                val inflater = LayoutInflater.from(this@MasterInfoUserActivity)
                if(info.feedbacks.size == 0) {
                    emptyText.visibility = View.VISIBLE
                } else info.feedbacks.forEach { f ->
                    val view = inflater.inflate(R.layout.feedback_master_item, feedbackList, false)
                    view.findViewById<TextView>(R.id.sketchname).text = f.sketchname
                    view.findViewById<TextView>(R.id.clientname).text = f.name
                    Glide.with(this@MasterInfoUserActivity).load(API.images + f.image).into(view.findViewById(R.id.image))
                    view.findViewById<TextView>(R.id.hours).text = "${f.hours} часов"
                    view.findViewById<RatingBar>(R.id.rated).progress = f.rate
                    view.findViewById<TextView>(R.id.comment).text = "Комментарий: ${f.comment}"
                    feedbackList.addView(view)
                }

            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
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