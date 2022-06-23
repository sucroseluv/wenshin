package com.sucroseluvv.wenshin.Screens.UserScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.UserInfo
import com.sucroseluvv.wenshin.models.UserType
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_sketch_info_user.*
import retrofit2.Response
import javax.inject.Inject

class SketchInfoUserActivity : AppCompatActivity() {

    var liked: Boolean = false

    @Inject()
    lateinit var networkService: NetworkService
    @Inject
    lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketch_info_user)
        App.Inst.AppComponent.inject(this)

        val id = intent.getIntExtra("id", -1)
        if(id != -1)
            loadSketch(id)
        else
            finish()

        like.setOnClickListener {
            liked = !liked
            networkService.favoriteSketch(id, liked).subscribe { x -> Log.d("fav", "fav - ${x.body()?.favorite}") }
            showLiked(liked)
        }
    }

    fun showLiked(like: Boolean){
        if(like) {
            likes.visibility = View.VISIBLE
            unlikes.visibility = View.GONE
        } else {
            likes.visibility = View.GONE
            unlikes.visibility = View.VISIBLE
        }
    }

    val favConsumer = object: SingleObserver<Response<Array<Sketch>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("favConsumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<Sketch>>) {
            val sketches = t.body()
            val id = intent.getIntExtra("id", -1)
            if(sketches != null && sketches?.size > 0) {
                if(sketches.any{s -> s.id == id}) {
                    liked = true
                    showLiked(liked)
                }
            }
        }

        override fun onError(e: Throwable) {
            Log.d("observer", "error ${e.message}")
        }

    }

    val consumer = object: SingleObserver<Response<Sketch>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("observer", "subscribed")
        }

        override fun onSuccess(t: Response<Sketch>) {
            val sketch = t.body()
            if(sketch != null) {
                tagsLayout.removeAllViews()
                title = sketch.name
                sketchname.text = sketch.name
                description.text = sketch.description
                info.text = "Ширина: ${sketch.width}см\nВысота: ${sketch.height}см\nОбъем работы:${sketch.working_hours} часов"
                Glide.with(this@SketchInfoUserActivity).load(API.images+sketch.image).into(image)
                sketch.tags?.forEach { tag ->
                    val tagView = LayoutInflater.from(this@SketchInfoUserActivity).inflate(R.layout.tag_layout, tagsLayout, false)
                    tagView.findViewById<TextView>(R.id.text).text = tag
                    tagsLayout.addView(tagView)
                }
            }
        }

        override fun onError(e: Throwable) {
            Log.d("observer", "error ${e.message}")
        }

    }

    fun loadSketch(id: Int) {
        networkService.fetchSketch(id).subscribe(consumer)
        if(userInfo.role==UserType.user){
            networkService.fetchFavSketches().subscribe(favConsumer)
        } else {
            like.visibility = View.GONE
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