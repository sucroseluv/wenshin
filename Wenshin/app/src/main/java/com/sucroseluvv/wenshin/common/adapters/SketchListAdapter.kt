package com.sucroseluvv.wenshin.common.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.SketchInfoUserActivity
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.API

class SketchListAdapter : RecyclerView.Adapter<SketchListAdapter.SketchListViewHolder> {
    var sketchList: Array<Sketch>

    constructor(sketches: Array<Sketch>) : super() {
        sketchList = sketches
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SketchListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.sketch_item, parent, false)
        return SketchListViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: SketchListViewHolder, position: Int) {
        holder.bind(sketchList.get(position))
    }

    override fun getItemCount(): Int {
        return sketchList.size
    }

    class SketchListViewHolder : RecyclerView.ViewHolder {
        var background: View
        var image: ImageView
        var name: TextView
        var context: Context

        constructor(itemView: View, context: Context) : super(itemView) {
            background = itemView
            image = itemView.findViewById<ImageView>(R.id.image)
            name = itemView.findViewById<TextView>(R.id.sketchname)
            this.context = context
        }

        fun bind(sketch: Sketch) {
            Glide.with(context).load(API.images + sketch.image).into(image)
            name.text = sketch.name
            background.setOnClickListener {
                val intent = Intent(context, SketchInfoUserActivity::class.java)
                intent.putExtra("id", sketch.id)
                startActivity(context, intent, null)
            }
        }

    }
}

