package com.sucroseluvv.wenshin.Screens.UserScreens

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.MasterBusyness
import com.sucroseluvv.wenshin.models.requests.SessionRequest
import com.sucroseluvv.wenshin.models.responses.IdResponse
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_create_consultation.*
import kotlinx.android.synthetic.main.activity_new_order_user_time.*
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class CreateConsultationActivity : AppCompatActivity() {

    var busyness: Array<MasterBusyness> = arrayOf()
    var date: Date? = null
    var chosenHour: Int? = null

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_consultation)
        App.Inst.AppComponent.inject(this)
        title="Запись на консультацию"
        getBusyness()
    }

    var consumer = object: SingleObserver<Response<Array<MasterBusyness>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer","subscribed")
        }
        override fun onSuccess(t: Response<Array<MasterBusyness>>) {
            val busyness = t.body()
            if(busyness != null) {
                this@CreateConsultationActivity.busyness = busyness
                createActivity()
                create.setOnClickListener {
                    if(date == null || chosenHour == null) {
                        Toast.makeText(this@CreateConsultationActivity, "Выберите время консультации", Toast.LENGTH_SHORT).show()
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

                    val day = date!!.date
                    val month = date!!.month
                    val formatDay = if (day < 10) "0" + day else day
                    val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
                    val date = "${date!!.year}-${formatMonth}-${formatDay}"

                    networkService.createConsultation(SessionRequest(date, arrayOf(chosenHour!!))).subscribe(consumer)
                }
            }
        }
        override fun onError(e: Throwable) {
            Log.d("consumer","error: ${e.message}")
        }
    }

    fun getBusyness() {
        networkService.fetchMasterBusyness(4).subscribe(consumer)
    }

    fun createActivity() {
        val emptyBack = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
        val unavailBack = resources.getDrawable(R.drawable.background_unavailable)
        val selectedBack = resources.getDrawable(R.drawable.background_ripple_corners)

        val startDate = Date(2022,5,19)
        val view = LayoutInflater.from(this).inflate(R.layout.new_order_user_session, sessions, false) as LinearLayout
        view.findViewById<TextView>(R.id.seansNumber).text = "Консультация"
        var timeButtons: Sequence<FrameLayout> = view.findViewById<FlexboxLayout>(R.id.bottonTimeContainer).children as Sequence<FrameLayout>

        val dateText = view.findViewById<TextView>(R.id.consultDate)
        fun setDateText(tempDate: Date) {
            val day = tempDate.date
            val month = tempDate.month
            val year = tempDate.year
            val formatDay = if (day < 10) "0" + day else day
            val formatMonth = if (month+1 < 10) "0" + (month+1) else (month+1)
            dateText.text = "$formatDay.$formatMonth.$year"
        }
        setDateText(startDate)
        date = startDate

        val timeText = view.findViewById<TextView>(R.id.time)

        fun setTimeText() {
            timeText.text = "Время: не выбрано"
            if(chosenHour != null) {
                val formatstart = if(chosenHour!! < 10) "0"+chosenHour else chosenHour.toString()
                val formatend = if((chosenHour!!+1) < 10) "0"+(chosenHour!!+1) else (chosenHour!!+1).toString()

                timeText.text = "Время: с ${formatstart}:00 до ${formatend}:00"
            }
        }

        fun select() {
            for(button: FrameLayout in timeButtons) {
                val time = getButtonTime(button)
                setTimeText()
                button.background = emptyBack
                button.setOnClickListener {
                    button.background = selectedBack
                    chosenHour = time
                    select()
                }

                if(chosenHour == time) {
                    button.background = selectedBack
                }
                if(getUnavailable().contains(time)) {
                    button.background = unavailBack
                    button.setOnClickListener {
                        Toast.makeText(this, "Время уже забронировано", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        view.findViewById<Button>(R.id.chooseDate).setOnClickListener {
            DatePickerDialog(
                this,
                object: DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(picker: DatePicker?, year: Int, month: Int, day: Int) {
                        val newDate = Date(year,month,day)
                        val now = Date(2022,5,18)
                        val threeMonths = Date(2022,8,18)
                        if(newDate <= now) {
                            Toast.makeText(this@CreateConsultationActivity, "Выберите дату позже сегодняшней", Toast.LENGTH_SHORT).show()
                            return
                        } else if (newDate > threeMonths) {
                            Toast.makeText(this@CreateConsultationActivity, "Выберите дату не позже трех месяцев от сегодняшнего дня", Toast.LENGTH_SHORT).show()
                            return
                        }
                        date = newDate
                        setDateText(newDate)
                        chosenHour = null
                        select()
                    }
                },
                2022,
                5,
                19
            ).show()
        }


        select()

        consultTimeContainer.removeAllViews()
        consultTimeContainer.addView(view)
    }

    fun getUnavailable(): Array<Int> {
        if(date == null)
            return arrayOf()

        val res = mutableListOf<Int>()
        busyness?.forEach {
            if(it.date.month == date!!.month && it.date.date == date!!.date) {
                res.addAll(it.hours)
            }
        }
        return res.toIntArray().toTypedArray()
    }

    fun getButtonTime(view: FrameLayout): Int {
        val buttonTextRaw: String = (view.children.first() as TextView).text.toString()
        val buttonNum = buttonTextRaw.substring(0, buttonTextRaw.length - 3).toInt()
        return buttonNum
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