package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_favorites_user.*
import retrofit2.Response
import javax.inject.Inject

class FavoritesUserActivity : AppCompatActivity() {

    val choiseSessionsUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        loadActivity()
    }

    val favConsumer = object: SingleObserver<Response<Array<Sketch>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("favConsumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<Sketch>>) {
            val sketches = t.body()
            if(sketches != null && sketches?.size > 0) {
                val inflater = LayoutInflater.from(this@FavoritesUserActivity)
                sketches.forEach { s ->
                    val view1 = inflater.inflate(R.layout.favorite_item, list, false)
                    Glide.with(this@FavoritesUserActivity)
                        .load(API.images + s.image)
                        .into(view1.findViewById<ImageView>(R.id.image))
                    view1.findViewById<TextView>(R.id.text).text = s.name
                    view1.findViewById<TextView>(R.id.description).text = s.description
                    var liked = true
                    val favoriteIcon = view1.findViewById<ImageView>(R.id.favorite)
                    favoriteIcon.setOnClickListener {
                        liked = !liked
                        networkService.favoriteSketch(s.id, liked).subscribe { x -> Log.d("fav", "fav - ${x.body()?.favorite}") }
                        if(liked) {
                            favoriteIcon.background = resources.getDrawable(R.drawable.ic_favorite_fill)
                        } else {
                            favoriteIcon.background = resources.getDrawable(R.drawable.ic_favorite)
                        }
                    }
                    view1.setOnClickListener {
                        val intent = Intent(this@FavoritesUserActivity, SketchInfoUserActivity::class.java)
                        intent.putExtra("id", s.id)
                        choiseSessionsUserActivityLauncher.launch(intent)
                    }
                    list.addView(view1)
                }
            } else {
                emptyText.visibility = View.VISIBLE
            }
        }

        override fun onError(e: Throwable) {
            Log.d("observer", "error ${e.message}")
        }

    }

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_user)
        App.Inst.AppComponent.inject(this)
        title="Избранное"
        loadActivity()
    }

    fun loadActivity() {
        list.removeAllViews()
        networkService.fetchFavSketches().subscribe(favConsumer)
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