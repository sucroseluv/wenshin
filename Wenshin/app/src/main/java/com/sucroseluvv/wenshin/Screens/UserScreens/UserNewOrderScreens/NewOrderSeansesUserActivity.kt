package com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.MasterBusyness
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_new_order_user_time.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject


class NewOrderSeansesUserActivity : AppCompatActivity() {
    class Session {
        var date: Date? = null
        var from: Int? = null
        var to: Int? = null
        fun getCount(): Int? {
            if(to==null || from==null)
                return null
            return to!!-from!!+1
        }
    }

    var hours: Int? = null
    var masterId: Int? = null
    var sessionList: MutableList<Session> = mutableListOf<Session>()
    var busyList: Array<MasterBusyness>? = null
    var minDate: Date? = null

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Распределение часов"
        App.Inst.AppComponent.inject(this)
        val hoursParam = intent.getIntExtra("hours", -1)
        val masterIdParam = intent.getStringExtra("masterId")
        if(intent.hasExtra("minDate"))
            minDate = intent.getSerializableExtra("minDate") as Date

        if(hoursParam == -1 || masterIdParam == null) {
            finish()
            return
        }
        setSessonsScreen(hoursParam, masterIdParam!!.toInt())
    }

    fun addNewSession() {
        sessions.children.forEach {
            disableEnableControls(false, it as ViewGroup)
        }
        val view = createSession(sessionList.size + 1)
        sessions.addView(view)
    }

    val consumer = object : SingleObserver<Response<Array<MasterBusyness>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<MasterBusyness>>) {
            if(t.body() != null) {
                busyList = t.body()
                addSession.visibility = View.VISIBLE
                addNewSession()
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", e.message ?: "nul message")
        }

    }

    fun setSessonsScreen(hours: Int, masterId: Int) {
        setContentView(R.layout.activity_new_order_user_time)

        this.hours = hours
        this.masterId = masterId

        networkService.fetchMasterBusyness(masterId).subscribe(consumer)
        addSession.setOnClickListener { v->
            addNewSession()
        }
        distributed.text = "Распределено ${0} из ${hours} часов"
        nextButton.setOnClickListener { l ->
            nextHandler()
        }
//        var view: LinearLayout = createSession(1, arrayOf(9,10,12))
//        var view2: LinearLayout = createSession(2, arrayOf(8,9,10,16,17))
//        disableEnableControls(true,view)
//        sessions.addView(view)
//        sessions.addView(view2)
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    fun getUnavailable(date: Date): Array<Int> {
        val res = mutableListOf<Int>()
        Log.d("getUnavailable", "starts")
        busyList?.forEach {
            Log.d("getUnavailable", "month ${it.date.month == date.month}")
            Log.d("getUnavailable", "date ${it.date.date == date.date} date1: ${it.date.date} date2: ${date.date}")
            if(it.date.month == date.month && it.date.date == date.date) {
                Log.d("getUnavailable", "adding ${it.hours.joinToString(", ")}")
                res.addAll(it.hours)
            }
        }
        Log.d("getUnavailable", "ends ${res.size}")

        return res.toIntArray().toTypedArray()
    }

    fun setDestributed() {
        var count = 0
        sessionList.forEach {
            if(it.from != null && it.to != null) {
                count += (it!!.to)!! - (it!!.from)!! + 1
            }
        }
        distributed.text = "Распределено ${count} из ${hours} часов"
        if(count >= hours!!) addSession.visibility = View.GONE
    }
    fun stringifyHours(hours: Int): String {
        val count = hours
        var hourText = "час"
        if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
        if(arrayOf(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20).any { n -> n==count }) hourText = "часов"
//        if(hours % 100>=10 && hours % 100 <= 20) hourText = "часов"
        return "${hours} ${hourText}"
    }
    fun nextHandler() {
        var count = 0
        sessionList.forEach {
            if(it.from != null && it.to != null) {
                count += (it!!.to)!! - (it!!.from)!! + 1
            }
        }
        if(hours != null && count > hours!!) {
            Toast.makeText(this, "Распределено больше часов, чем необходимо. Уберите ${stringifyHours(count-hours!!)}.", Toast.LENGTH_SHORT).show()
            return
        }
        else if (hours != null && count < hours!!) {
            Toast.makeText(this, "Распределено меньше часов, чем необходимо. Распределите ещё ${stringifyHours(hours!!-count)}.", Toast.LENGTH_SHORT).show()
            return
        }

        val sessions: MutableList<com.sucroseluvv.wenshin.models.Session> = mutableListOf()
        sessionList.forEach {
            if(it.from != null && it.to != null) {
                val arrayHours = mutableListOf<Int>()
                for(i in it.from!! until it.to!!+1) {
                    arrayHours.add(i)
                }
                val session: com.sucroseluvv.wenshin.models.Session = com.sucroseluvv.wenshin.models.Session(null, it.date!!, arrayHours.toTypedArray(), null,null)
                sessions.add(session)
            }
        }

        val resIntent = Intent().putExtra("master", Gson().toJson(sessions.toTypedArray()))
        setResult(200, resIntent)
        finish()
    }
    fun stringifyDate(tempDate: Date): String {
        val day = tempDate.date
        val month = tempDate.month
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
        return "$formatDay.$formatMonth.$year"
    }
    fun createSession(number: Int): LinearLayout{
        if(sessionList.size < number) {
            sessionList.add(number-1, Session())
        }
        var sessionObject: Session = sessionList[number-1]
        var newSession: LinearLayout = LayoutInflater.from(this).inflate(R.layout.new_order_user_session, sessions, false) as LinearLayout
        newSession.findViewById<TextView>(R.id.seansNumber).text = "Сеанс №${number}"

        val dateAndTime = Calendar.getInstance()

        var tempDate = if(number >= 2 && sessionList[number-2].date != null)
            Date(2022, sessionList[number-2].date!!.month,sessionList[number-2].date!!.date + 3)
            else
            if(minDate != null)
                Date(2022,minDate!!.month,minDate!!.date+3) else
                Date(2022,5,19)

        var unavailable: Array<Int> = getUnavailable(tempDate)
        var timeButtons: Sequence<FrameLayout> = newSession.findViewById<FlexboxLayout>(R.id.bottonTimeContainer).children as Sequence<FrameLayout>

        val emptyBack = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
        val unavailBack = resources.getDrawable(R.drawable.background_unavailable)
        val selectedBack = resources.getDrawable(R.drawable.background_ripple_corners)

        fun getButtonTime(view: FrameLayout): Int {
            val buttonTextRaw: String = (view.children.first() as TextView).text.toString()
            val buttonNum = buttonTextRaw.substring(0, buttonTextRaw.length - 3).toInt()
            return buttonNum
        }
        val dateText = newSession.findViewById<TextView>(R.id.consultDate)
        val day = tempDate.date
        val month = tempDate.month
        val year = tempDate.year
        val formatDay = if (day < 10) "0" + day else day
        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
        dateText.text = "$formatDay.$formatMonth.$year"
        sessionObject.date = tempDate
        addSession.visibility = View.GONE

        val timeText = newSession.findViewById<TextView>(R.id.time)
        fun select() {
            for(button: FrameLayout in timeButtons) {
                val num = getButtonTime(button)
                if(sessionObject.from != null && sessionObject.to != null) {
                    addSession.visibility = View.VISIBLE
                    if(sessionObject.from!! <= num && sessionObject.to!! >= num) {
                        button.background = selectedBack
                    } else {
                        button.background = emptyBack
                    }
                } else {
                    addSession.visibility = View.GONE
                }
                if(unavailable.any { n->n==num }) {
                    button.background = unavailBack
                }
            }
            setDestributed()
        }
        fun createButtons() {
            for(button: FrameLayout in timeButtons) {
                val buttonTextRaw: String = (button.children.first() as TextView).text.toString()
                val buttonTime: String = buttonTextRaw.substring(0, buttonTextRaw.length-3)
                if(unavailable.any { number -> number == buttonTime.toInt() }) {
                    button.background = unavailBack
                } else {
                    button.background = emptyBack
                }

                button.setOnClickListener {
                    it as FrameLayout
                    val buttonNum: Int = getButtonTime(it)
                    if(unavailable.any { number -> number == buttonTime.toInt() }) {
                        Toast.makeText(this,"Это время занято",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (sessionObject.from == null && sessionObject.to == null || sessionObject.from != sessionObject.to) {
                        sessionObject.from = buttonNum
                        sessionObject.to = buttonNum
                    } else if (sessionObject.from == sessionObject.to) {
                        if (buttonNum < sessionObject.from!!) {
                            if (unavailable.any { num -> num > buttonNum && num < sessionObject.to!! }) {
                                sessionObject.from = buttonNum
                                sessionObject.to = buttonNum
                            } else {
                                sessionObject.from = buttonNum
                            }
                        } else {
                            if (unavailable.any { num -> num > sessionObject.from!! && num < buttonNum }) {
                                sessionObject.from = buttonNum
                                sessionObject.to = buttonNum
                            } else {
                                sessionObject.to = buttonNum
                            }
                        }
                    }
                    select()
                    if(sessionObject.from != null && sessionObject.to != null) {
                        val count = sessionObject.getCount()
                        var hourText = "час"
                        if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
                        if(arrayOf(5,6,7,8,9,10).any { n -> n==count }) hourText = "часов"
                        timeText.text = "Время: $count $hourText, с ${sessionObject.from!!.toString()+":00"} до ${(sessionObject.to!!+1)!!.toString()+":00"}"
                    } else {
                        timeText.text = "Время: не выбрано"
                    }
                }

            }
        }
        newSession.findViewById<Button>(R.id.chooseDate).setOnClickListener {
            DatePickerDialog(
                this,
                object: DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
                        val newDate = Date(year,month,day)
                        val now = if(minDate!=null) Date(2022,minDate!!.month,minDate!!.date) else Date(2022,5,18) //here
                        val threeMonths = if(minDate!=null) Date(2022,minDate!!.month+3,minDate!!.date)
                            else Date(2022,8,18)

                        if(sessionList.size >= 2 && sessionList[number-2]?.date != null && sessionList[number-2].date!! >= newDate) {
                            Toast.makeText(this@NewOrderSeansesUserActivity, "Выберите дату позже предыдущего сеанса", Toast.LENGTH_SHORT).show()
                            return
                        } else if (newDate <= now) {
                            Toast.makeText(this@NewOrderSeansesUserActivity, "Выберите дату позже ${if(minDate!=null) stringifyDate(now) else "сегодняшнего дня"}", Toast.LENGTH_SHORT).show()
                            return
                        } else if (newDate > threeMonths) {
                            Toast.makeText(this@NewOrderSeansesUserActivity, "Выберите дату не позже ${if(minDate!=null) stringifyDate(threeMonths) else "трех месяцев от сегодняшнего дня"}", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val formatDay = if (day < 10) "0" + day else day
                        val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
                        dateText.text = "$formatDay.$formatMonth.$year"
                        sessionObject.date = newDate
                        sessionObject.from = null
                        sessionObject.to = null
                        addSession.visibility = View.GONE
                        timeText.text = "Время: не выбрано"
                        select()
                        unavailable = getUnavailable(Date(year,month,day))
                        createButtons()
                    }
                },
                2022,
                5,
                19
            ).show()
        }
        createButtons()


        return newSession
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