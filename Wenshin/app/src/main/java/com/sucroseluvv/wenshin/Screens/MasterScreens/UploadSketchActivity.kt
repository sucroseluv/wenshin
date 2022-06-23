package com.sucroseluvv.wenshin.Screens.MasterScreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.sucroseluvv.wenshin.App
import com.sucroseluvv.wenshin.R
import com.sucroseluvv.wenshin.common.dialogs.ErrorDialogFragment
import com.sucroseluvv.wenshin.models.requests.CreateSketchRequest
import com.sucroseluvv.wenshin.models.responses.ImageUploadResponse
import com.sucroseluvv.wenshin.network.API
import com.sucroseluvv.wenshin.network.NetworkService
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_sketch_info_user.*
import kotlinx.android.synthetic.main.activity_upload_sketch.*
import kotlinx.android.synthetic.main.activity_upload_sketch.image
import kotlinx.android.synthetic.main.activity_upload_sketch.tagsLayout
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.InputStream
import javax.inject.Inject

class UploadSketchActivity : AppCompatActivity() {

    var imagePath : String? = null
    var tags: MutableList<String> = mutableListOf<String>()
    var tagList: Array<String> = arrayOf()

    @Inject
    lateinit var networkService: NetworkService

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val stream = contentResolver.openInputStream(it)
                if(stream != null)
                    upload(stream)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_sketch)
        App.Inst.AppComponent.inject(this)
        title = "Загрузка эскиза"
        createActivity()
    }

    fun createActivity() {
        networkService.fetchTags().subscribe(tagsConsumer)
        uploadFile.setOnClickListener {
            pickImagesLauncher.launch("image/*")
        }
        create.setOnClickListener {
            if(validateForm()) {
                val request: CreateSketchRequest = CreateSketchRequest(
                    imagePath!!,
                    nameEdit.text.toString()!!,
                    descEdit.text.toString()!!,
                    (widthEdit.text.toString()).toFloat(),
                    (heightEdit.text.toString()).toFloat(),
                    (workingEdit.text.toString()).toInt(),
                    formTags()
                )
                //Log.d("create", "${request.image} ${request.name} ${request.description} ${request.width} ${request.height} ${request.workingHours} ${request.tags.size}")

                networkService.createSketch(request).subscribe(createConsumer)
            }

        }
    }

    fun formTags(): Array<Int> {
        return tags.map { t -> tagList.indexOf(t)+1 }.toTypedArray()
    }

    val tagsConsumer = object: SingleObserver<Response<Array<String>>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }
        override fun onSuccess(t: Response<Array<String>>) {
            val tagList = t.body()
            Log.d("consumer", "fetched ${t.body()?.size}")
            if(tagList != null && tagList?.size > 0) {
                this@UploadSketchActivity.tagList = tagList
                val inflater = LayoutInflater.from(this@UploadSketchActivity)
                tagList.forEach {
                    val view = inflater.inflate(R.layout.tag_layout, tagsLayout, false)
                    view.findViewById<TextView>(R.id.text).text = it
                    view.background = resources.getDrawable(R.drawable.background_stroke_ripple_corners)
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
    val uploadConsumer = object: SingleObserver<Response<ImageUploadResponse>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<ImageUploadResponse>) {
            val resp = t.body()
            if(resp != null) {
                Glide.with(this@UploadSketchActivity).load(API.images+resp.path).into(image)
                uploadFile.text = "Загрузить другое"
                loadingContainer.visibility = View.GONE
                image.visibility = View.VISIBLE
                uploadFile.visibility = View.VISIBLE
                imagePath = resp.path
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "subscribed")
            ErrorDialogFragment("Ошибка загрузки изображения\n${e.message}")
            loadingContainer.visibility = View.GONE
            image.visibility = View.VISIBLE
            uploadFile.visibility = View.VISIBLE
        }

    }

    val createConsumer = object: SingleObserver<Response<ResponseBody>> {
        override fun onSubscribe(d: Disposable) {
            Log.d("consumer", "subscribed")
        }

        override fun onSuccess(t: Response<ResponseBody>) {
            val body = t.body()
            if(body != null) {
                Toast.makeText(this@UploadSketchActivity, "Эскиз добавлен", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        override fun onError(e: Throwable) {
            Log.d("consumer", "error: ${e.message}")
        }

    }

    fun validateForm(): Boolean {
        if(imagePath == null) {
            Toast.makeText(this, "Загрузите изображение", Toast.LENGTH_SHORT).show()
            return false
        }
        if(nameEdit.text.toString() == null || nameEdit.text.toString() == "") {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
            return false
        }
        if(descEdit.text.toString() == null || descEdit.text.toString() == "") {
            Toast.makeText(this, "Введите описание", Toast.LENGTH_SHORT).show()
            return false
        }
        if(widthEdit.text.toString() == null || widthEdit.text.toString() == "") {
            Toast.makeText(this, "Введите ширину", Toast.LENGTH_SHORT).show()
            return false
        }
        if(widthEdit.text.toString().toFloatOrNull() == null) {
            Toast.makeText(this, "Введите корректную ширину", Toast.LENGTH_SHORT).show()
            return false
        }
        if(heightEdit.text.toString() == null || heightEdit.text.toString() == "") {
            Toast.makeText(this, "Введите высоту", Toast.LENGTH_SHORT).show()
            return false
        }
        if(heightEdit.text.toString().toFloatOrNull() == null) {
            Toast.makeText(this, "Введите корректную высоту", Toast.LENGTH_SHORT).show()
            return false
        }
        if(workingEdit.text.toString() == null || workingEdit.text.toString() == "") {
            Toast.makeText(this, "Введите объем работы", Toast.LENGTH_SHORT).show()
            return false
        }
        if(workingEdit.text.toString().toIntOrNull() == null) {
            Toast.makeText(this, "Введите корректный объем работы", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun upload(inputStream: InputStream) {
        val part = MultipartBody.Part.createFormData(
            "image", "myPic", RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                inputStream.readBytes()
            )
        )
        networkService.uploadSketchImage(part).subscribe(uploadConsumer)
        loadingContainer.visibility = View.VISIBLE
        image.visibility = View.GONE
        uploadFile.visibility = View.GONE
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