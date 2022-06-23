package com.sucroseluvv.wenshin.Screens.UserScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.adapters.SketchListAdapter
import com.sucroseluvv.wenshin.models.Session
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_sketches_user.*
import retrofit2.Response
import javax.inject.Inject

class SketchesUserActivity : AppCompatActivity() {
    private var sketches: Array<Sketch>? = null

    var tags = arrayOf<String>()
    var keyword = ""

    val choiseTagsUserActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if(res.resultCode == 200) {
            tags = res.data?.getStringArrayListExtra("tags")?.toTypedArray() ?: arrayOf()
            keyword = if(res.data?.hasExtra("keyword") ?: false) res.data?.getStringExtra("keyword").toString() else ""
            if(tags.size > 0) {
                searchText.text = "Выбрано тегов: ${tags.size}"
            } else {
                searchText.text = "Выбраны все теги"
            }
            Log.d("keyword", keyword)
            network.fetchSketches(tags, keyword).subscribe(consumer)
        }
    }

    @Inject
    lateinit var network: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sketches_user)
        App.Inst.AppComponent.inject(this)
        title="Эскизы"
        search_settings.setOnClickListener {
            val intent = Intent(this, SketchesSearchUserActivity::class.java)
            intent.putStringArrayListExtra("tags", tags.toCollection(ArrayList()))
            intent.putExtra("keyword", keyword)
            choiseTagsUserActivityLauncher.launch(intent)
        }

        network.fetchSketches().subscribe(consumer)
    }

    var consumer = object: SingleObserver<Response<Array<Sketch>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("observer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<Sketch>>) {
            Log.d("observer", "value is " + t.body()?.size)
            val body = t.body()
            if(body != null) setAdapter(body)
        }

        override fun onError(e: Throwable) {
            Log.d("observer", "error ${e.message}")
        }

    }

    fun setAdapter(sketches: Array<Sketch>) {
        val sketchesAdapter: SketchListAdapter = SketchListAdapter(sketches)
        var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 2)
        recycler_view.layoutManager = layoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = sketchesAdapter
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