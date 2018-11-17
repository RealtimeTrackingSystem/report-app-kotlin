package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.johnhigginsmavila.rcrtskotlinapp.Model.User
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_profile_picture.*
import kotlinx.android.synthetic.main.fragment_send_report.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class ChangeProfilePictureActivity : AppCompatActivity() {

    lateinit var user: User
    var profilePhoto: Bitmap? = null

    private val GALLERY = 1
    private val CAMERA = 2
    var sending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile_picture)

        val userData = JSONObject(App.prefs.userData)

        user = User(userData)

        loadPhoto()
    }

    fun loadPhoto () {
        try {
            val image = user.profilePicture.getJSONObject("metaData")
            val imageUri = image.getString("secure_url")
            Glide.with(this).load(imageUri).into(changeProfilePicImg)
        }
        catch (e: JSONException) {
            loadFromResource()
        }
    }

    fun loadFromResource () {
        var resourceId =App.prefs.context.resources.getIdentifier("light11", "drawable", App.prefs.context.packageName)
        changeProfilePicImg.setImageResource(resourceId)
    }

    fun showPictureDialog (view: View) {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Photo Source")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when(which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI) as Bitmap

                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    setImage(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            // imageview!!.setImageBitmap(thumbnail)
            setImage(thumbnail)
            // saveImage(thumbnail)
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setImage (img: Bitmap) {
        try {
            profilePhoto = img
            changeProfilePicImg.setImageBitmap(img)
        } catch (e: Exception) {
            Log.d("SET_IMAGE", e.localizedMessage)
        }
    }

    fun saveImage (view: View) {
        if (!sending) {
            sending = true
            if (profilePhoto != null) {
                showProgress()
                AuthService.updateProfilePicture(profilePhoto!!)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        when(it) {
                            true -> AuthService.refreshUserDataAndLoadHost()
                            false -> Observable.just(it)
                        }
                    }
                    .subscribe {
                        sending = false
                        hideProgress()
                        when (it) {
                            true -> showToast("Successfully Updated Profile Picture")
                            false -> showToast(AuthService.authResponseError!!)
                        }
                    }
                    .run {  }
            } else {
                sending = false
                showToast("Please select a photo")
            }
        }
    }

    fun showToast (text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        changeProfileProgressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        changeProfileProgressBar.visibility = View.INVISIBLE
    }
}
