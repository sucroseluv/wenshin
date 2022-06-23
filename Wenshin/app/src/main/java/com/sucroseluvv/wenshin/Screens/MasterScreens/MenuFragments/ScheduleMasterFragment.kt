package com.sucroseluvv.wenshin.Screens.MasterScreens.MenuFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.adapters.ScheduleAdapter
import com.sucroseluvv.wenshin.models.responses.ScheduleOrderResponse
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Response
import javax.inject.Inject

class ScheduleMasterFragment : Fragment() {

    @Inject
    lateinit var networkService: NetworkService
    var recycler: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.Inst.AppComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_master, container, false)
        createScreen(view)
        return view
    }

    val consumer = object: SingleObserver<Response<Array<ScheduleOrderResponse>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<Array<ScheduleOrderResponse>>) {
            val schedule = t.body()
            if(schedule != null && recycler!=null) {
                recycler!!.layoutManager = LinearLayoutManager(activity)
                recycler!!.itemAnimator = DefaultItemAnimator()
                recycler!!.adapter = ScheduleAdapter(schedule)
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
        }

    }

    fun createScreen(view: View) {
        recycler = view.findViewById<RecyclerView>(R.id.recycle)
        networkService.fetchMasterSchedule().subscribe(consumer)
    }
}