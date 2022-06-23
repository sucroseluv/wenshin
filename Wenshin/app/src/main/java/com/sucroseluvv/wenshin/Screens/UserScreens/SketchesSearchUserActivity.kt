package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.view.children
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_sketch_info_user.*
import kotlinx.android.synthetic.main.activity_sketch_info_user.tagsLayout
import kotlinx.android.synthetic.main.activity_sketches_search_user.*
import retrofit2.Response
import javax.inject.Inject

class SketchesSearchUserActivity : AppCompatActivity() {

    @Inject
    lateinit var networkService: NetworkService

    var tags: MutableList<String> = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketches_search_user)
        App.Inst.AppComponent.inject(this)
        title = "Поиск по тегам"
        val tempTags = intent.getStringArrayListExtra("tags")
        if(tempTags != null)
            tags = tempTags?.toMutableList()
        keyword.setText(if(intent.hasExtra("keyword")) intent.getStringExtra("keyword") else "")

        search.setOnClickListener {
            val intent = Intent().putStringArrayListExtra("tags", tags.toTypedArray().toCollection(ArrayList()))
            if(keyword.text.toString().length > 0)
                intent.putExtra("keyword", keyword.text.toString())
            setResult(200,intent)
            finish()
        }
        clear.setOnClickListener {
            tagsLayout.children.forEach {
                it.background = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
            }
            tags = mutableListOf()
            keyword.setText("")
        }

        createActivity()
    }

    val consumer = object: SingleObserver<Response<Array<String>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }
        override fun onSuccess(t: Response<Array<String>>) {
            val tagList = t.body()
            Log.d("consumer", "fetched ${t.body()?.size}")
            if(tagList != null && tagList?.size > 0) {
                val inflater = LayoutInflater.from(this@SketchesSearchUserActivity)
                tagList.forEach {
                    val view = inflater.inflate(R.layout.tag_layout, tagsLayout, false)
                    view.findViewById<TextView>(R.id.text).text = it
                    if(tags.any{t -> t.equals(it)}) {
                        view.background = resources.getDrawable(R.drawable.background_ripple_corners)
                    } else {
                        view.background = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
                    }
                    view.setOnClickListener { vw ->
                        if(tags.any { s -> s.equals(it) }) {
                            view.background = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
                            tags.removeAt(tags.indexOfFirst { t -> t.equals(it) })
                        } else {
                            view.background = resources.getDrawable(R.drawable.background_ripple_corners)
                            tags.add(it)
                        }
                    }
                    tagsLayout.addView(view)
                }
            }
        }
        override fun onError(e: Throwable) {
            Log.d("observer", "error ${e.message}")
        }

    }

    fun createActivity() {
        networkService.fetchTags().subscribe(consumer)
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