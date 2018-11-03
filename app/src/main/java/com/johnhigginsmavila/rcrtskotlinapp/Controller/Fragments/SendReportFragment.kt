package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.johnhigginsmavila.rcrtskotlinapp.Controller.MapsActivity
import com.johnhigginsmavila.rcrtskotlinapp.Model.MediaUpload
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewReport
import com.johnhigginsmavila.rcrtskotlinapp.Model.Report

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportService
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.EXTRA_MAP_CALLED_FROM
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.NEW_REPORT
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_send_report.*
import java.io.IOException
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SendReportFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SendReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SendReportFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var long: Double? = 0.0
    var lat: Double? = 0.0

    private val GALLERY = 1
    private val CAMERA = 2
    private var btnClicked = 0

    var pos: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_send_report, container, false)

        val btnLogout = v.findViewById<View>(R.id.sendReportBtn)
        val btnPinMap = v.findViewById<View>(R.id.mapPinBtn)
        val btnImg1 = v.findViewById<View>(R.id.img1Btn)
        val btnImg2 = v.findViewById<View>(R.id.img2Btn)
        val btnImg3 = v.findViewById<View>(R.id.img3Btn)
        val btnImg4 = v.findViewById<View>(R.id.img4Btn)
        btnLogout.setOnClickListener(this)
        btnPinMap.setOnClickListener(this)
        btnImg1.setOnClickListener(this)
        btnImg2.setOnClickListener(this)
        btnImg3.setOnClickListener(this)
        btnImg4.setOnClickListener(this)


        // Inflate the layout for this fragment
        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        setFormValues{}


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        loadFormValues()
    }

    override fun onClick(view: View?) {
        if (view?.id == sendReportBtn.id) {
            val text = titleInputTxt.text
            sendReport()
        } else if (view?.id == mapPinBtn.id) {
            Log.d("VIEW_ID", "View ID: ${view?.id}")
            setFormValues {
                if (it) {
                    showMap()
                }
            }
        } else  if (view?.id == img1Btn.id
            || view?.id == img2Btn.id
            || view?.id == img3Btn.id
            || view?.id == img4Btn.id) {
            showPictureDialog(view)
        }
    }

    fun showMap () {
        Toast.makeText(context, "You Clicked Map Pin Button", Toast.LENGTH_SHORT).show()

        val mapIntent = Intent(context, MapsActivity::class.java)

        mapIntent.putExtra(EXTRA_MAP_CALLED_FROM, NEW_REPORT)

        startActivity(mapIntent)
    }

    fun setFormValues (cb: (Boolean) -> Unit) {
        ReportForm.title = titleInputTxt.text.toString()
        ReportForm.description = descriptionInputTxt.text.toString()
        ReportForm.location = locationInputTxt.text.toString()
        ReportForm.long = long
        ReportForm.lat = lat
        ReportForm.tags = tagsInputTxt.text.toString()
        cb(true)
    }

    fun loadFormValues () {
        titleInputTxt.setText(ReportForm.title)
        descriptionInputTxt.setText(ReportForm.description)
        locationInputTxt.setText(ReportForm.location)
        tagsInputTxt.setText(ReportForm.tags)
        long = ReportForm.long
        lat = ReportForm.lat
        if (long != null && lat != null) {
            coordinatesTxt.text = "Coordinates: (${long}, ${lat})"
        }
        if (ReportForm.img1 != null) img1Btn.setImageBitmap(ReportForm.img1)
        if (ReportForm.img2 != null) img2Btn.setImageBitmap(ReportForm.img2)
        if (ReportForm.img3 != null) img3Btn.setImageBitmap(ReportForm.img3)
        if (ReportForm.img4 != null) img4Btn.setImageBitmap(ReportForm.img4)
    }

    fun showPictureDialog(view: View) {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        when (view.id) {
            img1Btn.id -> btnClicked = 0
            img2Btn.id -> btnClicked = 1
            img3Btn.id -> btnClicked = 2
            img4Btn.id -> btnClicked = 3
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

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
                    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI) as Bitmap

                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
                    setImage(bitmap, btnClicked)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            // imageview!!.setImageBitmap(thumbnail)
            setImage(thumbnail, btnClicked)
            // saveImage(thumbnail)
            Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setImage (img: Bitmap, imgIndex: Int) {
        try {
            when (imgIndex) {
                0 ->  {
                    img1Btn.setImageBitmap(img)
                    ReportForm.img1 = img
                }
                1 -> {
                    img2Btn.setImageBitmap(img)
                    ReportForm.img2 = img
                }
                2 -> {
                    img3Btn.setImageBitmap(img)
                    ReportForm.img3 = img
                }
                3 -> {
                    img4Btn.setImageBitmap(img)
                    ReportForm.img4 = img
                }
            }
        } catch (e: Exception) {
            Log.d("SET_IMAGE", e.localizedMessage)
        }
    }

    fun sendReport () {
        setFormValues{
            var report: NewReport? = null
            if (ReportForm.isValid()) {
                report = NewReport(ReportForm.title, ReportForm.description, ReportForm.location, ReportForm.long, ReportForm.lat, ReportForm.tags)
                if (ReportForm.img1 !== null) {
                    report.medias?.add(ReportForm.img1!!)
                }

                if (ReportForm.img2 !== null) {
                    report.medias?.add(ReportForm.img2!!)
                }

                if (ReportForm.img3 !== null) {
                    report.medias?.add(ReportForm.img3!!)
                }

                if (ReportForm.img4 !== null) {
                    report.medias?.add(ReportForm.img4!!)
                }


                ReportService.sendReport(report)
                    .subscribeOn(Schedulers.io())
                    .subscribe{
                        if (it) {
                            clearForm()
                            Toast.makeText(context, "Report was successfully sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "An error occur while sending report", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .run{

                    }
            } else {
                Toast.makeText(context, "Please complete the form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clearForm () {
        ReportForm.clear()
        val imgDrawable = resources.getDrawable(R.drawable.ic_menu_camera)
        img2Btn.setImageDrawable(imgDrawable)
        img1Btn.setImageDrawable(imgDrawable)
        img3Btn.setImageDrawable(imgDrawable)
        img4Btn.setImageDrawable(imgDrawable)
        loadFormValues()
        coordinatesTxt.setText("Coordinates: (x, y)")
    }

}
