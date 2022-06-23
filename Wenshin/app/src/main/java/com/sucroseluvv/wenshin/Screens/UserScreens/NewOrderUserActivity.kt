package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderChoiseMasterUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderChoiseSketchUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens.NewOrderSeansesUserActivity
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.*
import com.sucroseluvv.wenshin.models.requests.CreateOrderRequest
import com.sucroseluvv.wenshin.models.requests.getSessionsRequestList
import com.sucroseluvv.wenshin.models.responses.IdResponse
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_new_order_user.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class NewOrderUserActivity : AppCompatActivity() {
    var resSketch: Sketch? = null
    var resMaster: Master? = null
    var resSessions: Array<Session>? = null

    val choiseSketchUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->

        if(res.resultCode == 200) {
            val chosenSketch = Gson().fromJson<Sketch>(res.data?.getStringExtra("sketch"),Sketch::class.java)
            setSketch(chosenSketch)
        }

        val runNext = res.data?.getBooleanExtra("next", false) ?: false
        if (runNext) {
            val intent = Intent(this, NewOrderChoiseMasterUserActivity::class.java)
            intent.putExtra("next", true)
            choiseMasterUserActivityLauncher.launch(intent)
        }
    }
    val choiseMasterUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if(res.resultCode == 200) {
            val chosenMaster = Gson().fromJson<Master>(res.data?.getStringExtra("master"),Master::class.java)
            setMaster(chosenMaster)
        }

        val runNext = res.data?.getBooleanExtra("next", false) ?: false
        if (runNext) {
            if(resSketch != null && resMaster != null) {
                if(resSketch == null || resMaster == null)
                    return@registerForActivityResult
                val intent = Intent(this, NewOrderSeansesUserActivity::class.java)
                intent.putExtra("hours", resSketch?.working_hours)
                intent.putExtra("masterId", resMaster?.id.toString())
                choiseSessionsUserActivityLauncher.launch(intent)
            }
        }
    }
    val choiseSessionsUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if(res.resultCode == 200) {
            val chosenSessions = Gson().fromJson<Array<Session>>(res.data?.getStringExtra("master"),Array<Session>::class.java)
            chosenSessions.forEach { s ->
                Log.d("ses", "date - ${s.date}")
                Log.d("ses", "hours - ${s.hours.joinToString(", ")}")

            }
            setSesions(chosenSessions)
        }
    }

    @Inject
    lateinit var appcontext: Context

    fun setSketch(sketch: Sketch?) {
        if(sketch == null) {
            chosenSketch.visibility = View.GONE
            notChosenSketch.visibility = View.VISIBLE
            resSketch = null
        } else {
            chosenSketch.visibility = View.VISIBLE
            notChosenSketch.visibility = View.GONE
            resSketch = sketch
            setSesions(null)
            resSketch as Sketch

            resSketch.let {
                it as Sketch
                Glide.with(this).load(API.images + it.image).into(imageChosenSketch)
                nameChosenSketch.text = it.name
                sketchHours.text = getSketchHoursString(it)
            }
        }
        showSessionsAsNeed()
    }
    fun setMaster(master: Master?) {
        if(master == null) {
            chosenMaster.visibility = View.GONE
            notChosenMaster.visibility = View.VISIBLE
            resMaster = null
        } else {
            chosenMaster.visibility = View.VISIBLE
            notChosenMaster.visibility = View.GONE
            resMaster = master
            setSesions(null)
            resMaster as Master


            resMaster.let {
                it as Master
                Glide.with(this).load(API.images + it.avatar).into(imageChosenMaster)
                nameChosenMaster.text = getMasterFio(it)
                chosenMasterPrice.text = it.price.toString()
            }
        }
        showSessionsAsNeed()
    }
    fun stringifyDate(tempDate: Date): String {
        val day = tempDate.date
        val month = tempDate.month
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
        return "$formatDay.$formatMonth.$year"
    }

    fun setSesions(sessions: Array<Session>?) {
        if(sessions == null) {
            notChosenSessions.visibility = View.VISIBLE
            chosenSessions.visibility = View.GONE
            resSessions = null
        } else {
            notChosenSessions.visibility = View.GONE
            chosenSessions.visibility = View.VISIBLE
            resSessions = sessions
            sessionsList.removeAllViews()
            var num = 1
            var sum: Float = 0f
            resSessions?.forEach { session ->
                val view = LayoutInflater.from(this).inflate(R.layout.new_order_user_session_result, sessionsList, false)
                view.findViewById<TextView>(R.id.consultDate).text = stringifyDate(session.date)
                view.findViewById<TextView>(R.id.sessionFromTo).text = "с ${session.hours.first()}:00 до ${session.hours.last()+1}:00"
                view.findViewById<TextView>(R.id.amount).text = (session.hours.size * resMaster!!.price).toString()
                view.findViewById<TextView>(R.id.duration).text = getSessionHoursString(session)
                view.findViewById<TextView>(R.id.sessionNumber).text = num.toString()
                num++
                sum += session.hours.size * resMaster!!.price
                sessionsList.addView(view)
            }
            totalAmount.text = sum.toString()
        }

        showSessionsAsNeed()
    }

    fun showSessionsAsNeed() {
        if(resSketch == null || resMaster == null) {
            resSessions = null
            sessionsBlock.visibility = View.GONE
        } else if (resSessions == null) {
            sessionsBlock.visibility = View.VISIBLE
            chosenSessions.visibility = View.GONE
            notChosenSessions.visibility = View.VISIBLE
        } else {
            sessionsBlock.visibility = View.VISIBLE
            chosenSessions.visibility = View.VISIBLE
            notChosenSessions.visibility = View.GONE
        }
    }

    @Inject
    lateinit var networkService: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.Inst.AppComponent.inject(this)
        setContentView(R.layout.activity_new_order_user)
        title="Заказ"
        createActivity()
        runChoiseSequence()
    }

    fun runChoiseSequence() {
        val intent = Intent(this, NewOrderChoiseSketchUserActivity::class.java)
        intent.putExtra("next", true)
        choiseSketchUserActivityLauncher.launch(intent)
    }

    fun createActivity() {
        actionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        chooseSketch.setOnClickListener {
            val intent = Intent(this, NewOrderChoiseSketchUserActivity::class.java)
            choiseSketchUserActivityLauncher.launch(intent)
        }
        chooseOtherSketch.setOnClickListener {
            val intent = Intent(this, NewOrderChoiseSketchUserActivity::class.java)
            choiseSketchUserActivityLauncher.launch(intent)
        }

        chooseMaster.setOnClickListener {
            val intent = Intent(this, NewOrderChoiseMasterUserActivity::class.java)
            choiseMasterUserActivityLauncher.launch(intent)
        }
        chooseOtherMaster.setOnClickListener {
            val intent = Intent(this, NewOrderChoiseMasterUserActivity::class.java)
            choiseMasterUserActivityLauncher.launch(intent)
        }

        chooseSessions.setOnClickListener {
            if(resSketch == null || resMaster == null) {
                Log.d("sessions", (resSketch == null).toString() +" - "+ (resMaster == null).toString())
                return@setOnClickListener
            }
            val intent = Intent(this, NewOrderSeansesUserActivity::class.java)
            intent.putExtra("hours", resSketch?.working_hours)
            intent.putExtra("masterId", resMaster?.id.toString())
            choiseSessionsUserActivityLauncher.launch(intent)
        }
        chooseOtherSessions.setOnClickListener {
            if(resSketch == null || resMaster == null) {
                Log.d("sessions", (resSketch == null).toString() +" - "+ (resMaster == null).toString())
                return@setOnClickListener
            }
            val intent = Intent(this, NewOrderSeansesUserActivity::class.java)
            intent.putExtra("hours", resSketch?.working_hours)
            intent.putExtra("masterId", resMaster?.id.toString())
            choiseSessionsUserActivityLauncher.launch(intent)
        }

        createOrderButton.setOnClickListener { l ->
            if(resSketch == null || resMaster == null || resSessions == null) {
                Toast.makeText(this, "Выберите все компоненты заказа", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val consumer: SingleObserver<Response<IdResponse>> = object : SingleObserver<Response<IdResponse>> {
                override fun onSubscribe(d: Disposable) {
                    Log.d("consumer", "subscribe")
                }

                override fun onSuccess(t: Response<IdResponse>) {
                    if(t.body() != null) {
                        setResult(200, Intent().putExtra("newOrder", t.body()!!.id))
                        finish()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("consumer", "error ${e.message}")
                    ErrorDialogFragment(e.message ?: "Неизвестная ошибка", "Ошибка создания заказа")
                }

            }

            val orderRequest = CreateOrderRequest(resSketch!!.id, resMaster!!.id, getSessionsRequestList(resSessions!!))
            networkService.createOrder(orderRequest).subscribe(consumer)
        }
    }






    fun createFake() {
        Glide.with(this).load(API.images + "sketches/animals-abstraction-sample.jpg").into(imageChosenSketch)
        Glide.with(this).load("https://syndicatetattoo.ru/s/article/112/cnt_4_1.jpg").into(imageChosenMaster)

        val sessions1 = LayoutInflater.from(this).inflate(R.layout.new_order_user_session_result, sessionsList, false)
        val sessions2 = LayoutInflater.from(this).inflate(R.layout.new_order_user_session_result, sessionsList, false)

        sessions1.findViewById<TextView>(R.id.consultDate).text = "20.06.2022"
        sessions1.findViewById<TextView>(R.id.sessionFromTo).text = "с 13:00 до 17:00"
        sessions1.findViewById<TextView>(R.id.amount).text = "4800"
        sessions1.findViewById<TextView>(R.id.duration).text = "4 часа"

        sessions2.findViewById<TextView>(R.id.consultDate).text = "26.06.2022"
        sessions2.findViewById<TextView>(R.id.sessionFromTo).text = "с 13:00 до 16:00"
        sessions2.findViewById<TextView>(R.id.amount).text = "3600"
        sessions2.findViewById<TextView>(R.id.duration).text = "3 часа"
        sessions2.findViewById<TextView>(R.id.sessionNumber).text = "2"

        sessionsList.addView(sessions1)
        sessionsList.addView(sessions2)
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