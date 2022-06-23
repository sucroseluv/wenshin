package com.sucroseluvv.wenshin.Screens.UserScreens.UserNewOrderScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.SketchesUserActivity
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_new_order_choise_sketch_user.*
import retrofit2.Response
import javax.inject.Inject

class NewOrderChoiseSketchUserActivity : AppCompatActivity() {

    val sketchesUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        createActivity()

    }

    @Inject
    lateinit var networkService: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order_choise_sketch_user)
        App.Inst.AppComponent.inject(this)
        title="Избранное"
        choise.setOnClickListener {
            sketchesUserActivityLauncher.launch(Intent(this,SketchesUserActivity::class.java))
        }
        createActivity()
    }

    val consumer: SingleObserver<Response<Array<Sketch>>> = object: SingleObserver<Response<Array<Sketch>>>{
        override fun onSubscribe(d: Disposable) {
            //txt.text = "subscribed"
        }

        override fun onSuccess(t: Response<Array<Sketch>>) {
            //txt.text = "size: " + t.body()?.size

            sketchesList.removeAllViews()
            if(t.body() != null && t.body()!!.size > 0){
                empty.visibility = View.GONE
                t.body()!!.forEach { sketch ->
                    val master = LayoutInflater.from(this@NewOrderChoiseSketchUserActivity)
                        .inflate(R.layout.new_order_sketch_item_choise, sketchesList, false)
                    val img = master.findViewById<ImageView>(R.id.image)
                    Glide.with(this@NewOrderChoiseSketchUserActivity)
                        .load(API.images + sketch.image)
                        .into(img)
                    master.findViewById<TextView>(R.id.sketchname).text = sketch.name
                    val count = sketch.working_hours
                    var hourText = "час"
                    if(arrayOf(2,3,4).any { n -> n==count }) hourText = "часа"
                    if(arrayOf(5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20).any { n -> n==count }) hourText = "часов"
                    master.findViewById<TextView>(R.id.hours).text = sketch.working_hours.toString() + " " + hourText
                    master.setOnClickListener{ v ->
                        setResult(200, Intent().putExtra("next", intent.getBooleanExtra("next", false))
                            .putExtra("sketch", Gson().toJson(sketch)))
                        finish()
                    }
                    sketchesList.addView(master)
                }
            }
            else {
                empty.visibility = View.VISIBLE
            }
        }

        override fun onError(e: Throwable) {
            //txt.text = e.message
        }
    }

    fun createActivity() {
        networkService.fetchFavSketches().subscribe(consumer)

//        val master1 = LayoutInflater.from(this).inflate(R.layout.new_order_sketch_item_choise, sketchesList, false)
//        val master2 = LayoutInflater.from(this).inflate(R.layout.new_order_sketch_item_choise, sketchesList, false)
//
//        val img1 = master1.findViewById<ImageView>(R.id.image)
//        val img2 = master2.findViewById<ImageView>(R.id.image)
//        Glide.with(this).load("http://192.168.0.164:1337/images/sketches/animals-abstraction-sample.jpg").into(img1)
//        Glide.with(this).load("http://192.168.0.164:1337/images/sketches/books-sample.jpg").into(img2)
//
//        master1.findViewById<TextView>(R.id.name).text = "Рысь"
//        master2.findViewById<TextView>(R.id.name).text = "Книги"
//
//        master1.findViewById<TextView>(R.id.hours).text = "7 часов"
//        master2.findViewById<TextView>(R.id.hours).text = "5 часов"
//
//        sketchesList.addView(master1)
//        sketchesList.addView(master2)
    }

    override fun onNavigateUp(): Boolean {
//        setResult(200, Intent().putExtra("next", true))
        finish()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}