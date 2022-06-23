package com.sucroseluvv.wenshin.Screens.Common

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var context: Context
    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.Inst.AppComponent.inject(this)

//        var serviceProvider: ServiceProvider = ServiceProvider()
//        var networkService: NetworkService = NetworkService(serviceProvider)

        var observer: Observer<ResponseBody> = object : Observer<ResponseBody> {
            override fun onNext(t: ResponseBody) {
                Log.d("observer", "value is " + t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("observer", "subscribed")
            }

            override fun onError(e: Throwable) {
                Log.d("observer", "subscribed")
            }

            override fun onComplete() {
                Log.d("observer", "completed")
            }
        }
        var consumer = object: SingleObserver<Response<Array<Sketch>>> {
            override fun onSubscribe(d: Disposable) {
                Log.d("observer", "subscribed")
            }

            override fun onSuccess(t: Response<Array<Sketch>>) {
                Log.d("observer", "value is " + t.code())
            }

            override fun onError(e: Throwable) {
                Log.d("observer", "error ")
            }

        }

        button.setOnClickListener() {
//            Log.d("context", "context is " + context.hashCode())
            networkService.fetchSketches().subscribe(consumer)
        }

    }
}