package com.sucroseluvv.wenshin.Screens.GuestScreens.MenuFragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.Screens.UserScreens.InfoAboutActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.InfoReccomendationsActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.SketchInfoUserActivity
import com.sucroseluvv.wenshin.Screens.UserScreens.SketchesUserActivity
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class MainGuestFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Inject
    lateinit var appContext: Context
    @Inject
    lateinit var networkService: NetworkService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        App.Inst.AppComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val result: View = inflater.inflate(R.layout.fragment_main_user, container, false)
        createMain(result)
        return result
    }

    fun createMain(result: View) {
        val image = result.findViewById<ImageView>(R.id.image)
        try {
            val ims: InputStream? = activity?.getAssets()?.open("salon/salon1.jpg")
            val d = Drawable.createFromStream(ims, null)
            image.setImageDrawable(d)
            ims?.close()
        } catch (ex: IOException) {
            return
        }

        val allSketches = result.findViewById<ConstraintLayout>(R.id.allSketches)
        allSketches.setOnClickListener { view ->
            val intent = Intent(activity, SketchesUserActivity::class.java)
            startActivity(intent)
        }

        val img1 = result.findViewById<ImageView>(R.id.sketchImage1)
        val img2 = result.findViewById<ImageView>(R.id.sketchImage2)
        Glide.with(this).load(API.images+"sketches/animals-abstraction-sample.jpg").into(img1)
        Glide.with(this).load(API.images+"sketches/books-sample.jpg").into(img2)

        result.findViewById<ConstraintLayout>(R.id.about).setOnClickListener {
            startActivity(Intent(requireActivity(), InfoAboutActivity::class.java))
        }
        result.findViewById<ConstraintLayout>(R.id.recs).setOnClickListener {
            startActivity(Intent(requireActivity(), InfoReccomendationsActivity::class.java))
        }

        result.findViewById<ConstraintLayout>(R.id.firstsketch).setOnClickListener {
            startActivity(Intent(requireActivity(), SketchInfoUserActivity::class.java).putExtra("id", 1))
        }
        result.findViewById<ConstraintLayout>(R.id.secondsketch).setOnClickListener {
            startActivity(Intent(requireActivity(), SketchInfoUserActivity::class.java).putExtra("id", 2))
        }
    }
}