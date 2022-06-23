package com.sucroseluvv.wenshin.common.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.MasterScreens.OrderInfoMasterActivity
import com.sucroseluvv.wenshin.models.responses.ScheduleOrderResponse
import com.sucroseluvv.wenshin.network.API
import java.util.*

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>  {
    var list: Array<ScheduleOrderResponse>

    constructor(schedule: Array<ScheduleOrderResponse>) : super() {
        list = schedule
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleAdapter.ScheduleViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.master_schedule_item, parent, false)
        return ScheduleAdapter.ScheduleViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ScheduleAdapter.ScheduleViewHolder, position: Int) {
        var newDay: Boolean = true
        if(position>0) {
            val now = list.get(position).date
            val past = list.get(position-1).date
            newDay = now.date > past.date
        }
        holder.bind(list.get(position), newDay)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ScheduleViewHolder : RecyclerView.ViewHolder {
        var background: View
        var clickable: View
        var dayTitle: TextView
        var image: ImageView
        var orderNumber: TextView
        var sketchLabel: TextView
        var sketchName: TextView
        var userName: TextView
        var startTime: TextView
        var endTime: TextView
        var duration: TextView
        var context: Context

        constructor(itemView: View, context: Context) : super(itemView) {
            background = itemView
            clickable = itemView.findViewById<ConstraintLayout>(R.id.clickable)
            dayTitle = itemView.findViewById<TextView>(R.id.dayTitle)
            image = itemView.findViewById<ImageView>(R.id.image)
            orderNumber = itemView.findViewById<TextView>(R.id.orderNumber)
            sketchName = itemView.findViewById<TextView>(R.id.sketchName)
            sketchLabel = itemView.findViewById<TextView>(R.id.sketchLabel)
            userName = itemView.findViewById<TextView>(R.id.userName)
            startTime = itemView.findViewById<TextView>(R.id.startTimeConsult)
            endTime = itemView.findViewById<TextView>(R.id.endTimeConsult)
            duration = itemView.findViewById<TextView>(R.id.duration)
            this.context = context
        }

        fun stringifyHours(hours: Int): String {
            val count = hours
            var hourText = "час"
            if(arrayOf(2,3,4,22,23,24).any { n -> n==count }) hourText = "часа"
            if(arrayOf(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,25,26,27,28,29,30).any { n -> n==count }) hourText = "часов"
            return "${hours} ${hourText}"
        }

        fun bind(item: ScheduleOrderResponse, newDay : Boolean = false) {
            Glide.with(context).load(API.images + item.image).into(image)
            val isConsult = item.masterId != null && item.masterId == 4
            orderNumber.text = item.orderId.toString()
            sketchName.visibility = View.VISIBLE
            sketchLabel.text = "Эскиз: "
            sketchName.text = item.sketchname.toString()
            userName.text = item.username.toString()
            startTime.text = item.hours.first().toString()
            endTime.text = (item.hours.last()+1).toString()
            val hours = item.hours.size
            duration.text = stringifyHours(hours)

            dayTitle.visibility = if(newDay) View.VISIBLE else View.GONE
            val dateAndTime = Calendar.getInstance()
            val now = Date(
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))

            val itemMonth = item.date.month+1
            val itemDate = item.date.date
            val formatDay = if (itemDate < 10) "0" + (itemDate) else (itemDate)
            val formatMonth = if (itemMonth < 10) "0" + (itemMonth) else (itemMonth)
            var title = "${formatDay}.${formatMonth}.2022"

//            Log.d("now date", now.toString())
//            Log.d("item date", item.date.toString())
//            Log.d("now month", now.month.toString() + " - " +  itemMonth.toString() )
//            Log.d("now date", now.date.toString() + " - " +  itemDate.toString() )

            if(now.month+1 == itemMonth) {
                if(now.date == itemDate) title = "Сегодня"
                else if (now.date+1 == itemDate) title = "Завтра"
            }
            dayTitle.text = title

            clickable.setOnClickListener {
                val intent = Intent(context, OrderInfoMasterActivity::class.java)
                intent.putExtra("id", item.orderId)
                ContextCompat.startActivity(context, intent, null)
            }

            if(isConsult) {
                sketchName.visibility = View.INVISIBLE
                sketchLabel.text = "Консультация"
            }
        }

    }
}