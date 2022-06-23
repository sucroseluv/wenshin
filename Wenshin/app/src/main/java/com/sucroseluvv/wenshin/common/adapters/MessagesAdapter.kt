package com.sucroseluvv.wenshin.common.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.SketchInfoUserActivity
import com.sucroseluvv.wenshin.models.Message
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.UserInfo
import com.sucroseluvv.wenshin.models.UserType
import java.util.*

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    var messageList = mutableListOf<Message>()
    var userInfo: UserInfo? = null
    constructor()
    constructor(userInfo: UserInfo) {
        this.userInfo = userInfo
    }

    fun addMessages(messages: Array<Message>) {
        messageList.addAll(messages)
        notifyItemInserted(messageList.size - messages.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = if(viewType == 1) inflater.inflate(R.layout.my_message, parent, false)
            else inflater.inflate(R.layout.alien_message, parent, false)
        return MessagesViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(messageList.get(position))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val messageAuthorUser = messageList.get(position).author=="user"
        return if (userInfo!!.role==UserType.user) if(messageAuthorUser) 1 else 2 else if(messageAuthorUser) 2 else 1
    }



    class MessagesViewHolder : RecyclerView.ViewHolder {
        var background: View
        var context: Context
        var time: TextView
        var messageTv: TextView

        constructor(itemView: View, context: Context) : super(itemView) {
            background = itemView
            time = itemView.findViewById<TextView>(R.id.time)
            messageTv = itemView.findViewById<TextView>(R.id.message)
            this.context = context
        }
        fun stringifyDate(tempDate: Date): String {
            val day = tempDate.date
            val month = tempDate.month + 1
            val year = tempDate.year
            val formatDay = if (day < 10) "0" + day else day
            val formatMonth = if (month < 10) "0" + (month) else (month)
            return "$formatDay.$formatMonth.2022"
        }

        fun bind(message: Message) {
            val hours = message.datetime.hours
            val minutes =  message.datetime.minutes

            Log.d("message", "message: zxc ${hours}:${minutes}")

            var formatHours = hours.toString()
            if(hours < 10) formatHours = "0${hours}"
            var formatMinutes = minutes.toString()
            if(minutes < 10) formatMinutes = "0${minutes}"

            val dateAndTime = Calendar.getInstance()
            val now = Date(
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
            val sessionMonth = message.datetime.month+1
            val sessionDate = message.datetime.date
            val sessionSameDay = (now.month+1==sessionMonth) && (now.date==sessionDate)

            messageTv.text = message.message
            time.text = if(sessionSameDay)
                "${formatHours}:${formatMinutes}" else
                "${stringifyDate(message.datetime)} ${formatHours}:${formatMinutes}"
        }

    }

}