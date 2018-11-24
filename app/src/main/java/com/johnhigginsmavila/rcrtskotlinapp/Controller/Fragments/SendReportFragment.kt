package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.VolleyError
import com.google.android.gms.maps.model.LatLng
import com.johnhigginsmavila.rcrtskotlinapp.Controller.AddPersonActivity
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Controller.MapsActivity
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.Model.MediaUpload
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewReport
import com.johnhigginsmavila.rcrtskotlinapp.Model.Report

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.HostService
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportService
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.EXTRA_MAP_CALLED_FROM
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.NEW_REPORT
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_send_report.*
import org.json.JSONException
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
class SendReportFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var long: Double? = 0.0
    var lat: Double? = 0.0

    private val GALLERY = 1
    private val CAMERA = 2
    private var btnClicked = 0

    lateinit var txtAddPeople: TextView
    lateinit var progressBar: ProgressBar
    lateinit var btnSendReport: Button
    var sending = false

    var pos: LatLng? = null

    val urgencyArray = arrayListOf<String>("LOW", "MEDIUM", "EMERGENCY")
    val urgencyArrayView = arrayListOf<String>("Urgency: LOW", "Urgency: MEDIUM", "Urgency: EMERGENCY")

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

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(view?.windowToken, 0)


        progressBar = v.findViewById<ProgressBar>(R.id.progressBar)
        showProgress()
        loadHosts { hosts ->
            if (hosts?.count() == 0) {
                Toast.makeText(context, "Slow Internet connection", Toast.LENGTH_SHORT).show()
            } else {
                val scrollView = v.findViewById<ScrollView>(R.id.scrollView3)
                scrollView.visibility = View.VISIBLE
                btnSendReport = v.findViewById<Button>(R.id.sendReportBtn)
                val btnPinMap = v.findViewById<View>(R.id.mapPinBtn)
                val btnImg1 = v.findViewById<View>(R.id.img1Btn)
                val btnImg2 = v.findViewById<View>(R.id.img2Btn)
                val btnImg3 = v.findViewById<View>(R.id.img3Btn)
                val btnImg4 = v.findViewById<View>(R.id.img4Btn)
                val spinner = v.findViewById<Spinner>(R.id.hostListSinner)
                val urgencySpinner = v.findViewById<Spinner>(R.id.urgencySpinner)

                val btnAddPeople = v.findViewById<ImageButton>(R.id.peopleAddBtn)
                txtAddPeople = v.findViewById<TextView>(R.id.peopleTxt)
                val btnSubPeople = v.findViewById<ImageButton>(R.id.peopleSubBtn)

                loadPeople(txtAddPeople)


                btnSendReport.setOnClickListener(this)
                btnPinMap.setOnClickListener(this)
                btnImg1.setOnClickListener(this)
                btnImg2.setOnClickListener(this)
                btnImg3.setOnClickListener(this)
                btnImg4.setOnClickListener(this)
                spinner!!.setOnItemSelectedListener(this)
                urgencySpinner!!.setOnItemSelectedListener(this)

                btnAddPeople.setOnClickListener(this)
                btnSubPeople.setOnClickListener(this)


                if (hosts != null && hosts.count() > 0) {
                    hideProgress()
                    val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, hosts)
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    spinner.adapter = aa
                    try {
                        spinner.setSelection(ReportForm.hostsIndex)
                    }
                    catch (e: Exception) {
                        spinner.setSelection(0)
                    }
                }

                val uaa = ArrayAdapter(context, android.R.layout.simple_spinner_item, urgencyArrayView)
                uaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                urgencySpinner.adapter = uaa
                urgencySpinner.setSelection(ReportForm.urgencyIndex)
            }
        }


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
        when (view?.id) {
            sendReportBtn.id -> {
                val text = titleInputTxt.text
                sendReport()
            }
            mapPinBtn.id -> {
                Log.d("VIEW_ID", "View ID: ${view?.id}")
                setFormValues {
                    if (it && !sending) {
                        showMap()
                    }
                }
            }
            img1Btn.id -> showPictureDialog(view)
            img2Btn.id -> showPictureDialog(view)
            img3Btn.id -> showPictureDialog(view)
            img4Btn.id -> showPictureDialog(view)
            peopleAddBtn.id -> {
                Log.d("VIEW_ID", "View ID: ${view?.id}")
                setFormValues {
                    if (it) {
                        addPeopleDialog(view)
                    }
                }
            }
            peopleSubBtn.id -> {
                Log.d("VIEW_ID", "View ID: ${view?.id}")
                setFormValues {
                    if (it) {
                        removePeoplDialog(view)
                    }
                }
            }
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        when(adapterView.id) {
            R.id.urgencySpinner -> {
                ReportForm.urgencyIndex = position
                val urgency = urgencyArray[position]
                Log.d("SELECTED_URGENCY", urgency)
                ReportForm.urgency = urgency
            }
           R.id.hostListSinner -> {
                ReportForm.hostsIndex = position
                val id = ReportForm.hostListJson?.getJSONObject(position)?.getString("_id")
                Log.d("SELECTED_HOST", ReportForm.hostListJson?.getJSONObject(position)?.toString())
                ReportForm.hostId = id
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

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
        ReportForm.category = categoryInputTxt.text.toString()

        cb(true)
    }

    fun loadFormValues () {
        try {
            titleInputTxt.setText(ReportForm.title)
            descriptionInputTxt.setText(ReportForm.description)
            locationInputTxt.setText(ReportForm.location)
            tagsInputTxt.setText(ReportForm.tags)
            categoryInputTxt.setText(ReportForm.category)
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
        catch (e: Exception) {
            Log.d("LOAD_FORM_VALUES_ERROR", e.localizedMessage)
        }
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
        Log.d("SENDING_VALUE", sending.toString())
        Log.d("FORM_VALIDITY", ReportForm.isValid().toString())
        sendReportBtn.isEnabled = false
        setFormValues{
            var report: NewReport? = null
            if (ReportForm.isValid() && sending == false) {
                showProgress()
                sending = true
                report = NewReport(ReportForm.title, ReportForm.description, ReportForm.location, ReportForm.long, ReportForm.lat, ReportForm.tags, ReportForm.hostId, ReportForm.category, ReportForm.urgency)
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

                report.people = ReportForm.peopleList


                ReportService.sendReport(report)
                    .subscribeOn(Schedulers.io())
                    .subscribe{
                        hideProgress()
                        sendReportBtn.isEnabled = true
                        sending = false
                        if (it) {
                            clearForm()
                            Toast.makeText(context, "Report was successfully sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, ReportService.requestError, Toast.LENGTH_SHORT).show()
                        }
                    }
                    .run{

                    }
            } else {
                hideProgress()
                Toast.makeText(context, "Please complete the form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun clearForm () {
        ReportForm.clear()
        val imgDrawable = resources.getDrawable(R.drawable.ic_menu_camera)
        peopleTxt.setText("People Involved: ")
        img2Btn.setImageDrawable(imgDrawable)
        img1Btn.setImageDrawable(imgDrawable)
        img3Btn.setImageDrawable(imgDrawable)
        img4Btn.setImageDrawable(imgDrawable)
        loadFormValues()
        coordinatesTxt.setText("Coordinates: (x, y)")
        ReportForm.hostsIndex = 0
        ReportForm.urgencyIndex = 0
    }

    fun loadHosts (cb: (hosts: ArrayList<String>?) -> Unit) {
        HostService.getHosts()
            .subscribeOn(Schedulers.io())
            .subscribe{hostArray ->
                val hosts = ArrayList<String>()
                try {
                    for(i in 0..(hostArray.length() - 1)) {
                        val host = Host(hostArray.getJSONObject(i))
                        ReportForm.hostListJson = hostArray
                        hosts.add("HOST: ${host.name!!}")
                    }
                    Log.d("HOSTS", hostArray.toString())
                    cb(hosts)
                }
                catch (e: JSONException) {
                    cb(ArrayList())
                    Log.d("LOAD_HOST_ERROR", e.localizedMessage)
                }
                catch (e: VolleyError) {
                    cb(ArrayList())
                    Toast.makeText(context, "Slow Internet connection", Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception) {
                    cb(ArrayList())
                    Log.d("LOAD_HOST_ERROR", e.localizedMessage)
                }
            }
            .run {}
    }

    // add people

    fun addPeopleDialog(view: View) {
        val addPeopleIntent = Intent(context, AddPersonActivity::class.java)

        startActivity(addPeopleIntent)
    }

    fun loadPeople(textView: TextView) {
        var txt = "People Involved: "
        var first = true
        var last = false
        var people = ReportForm.peopleList
        for (i in 0..(people.length() - 1)) {
            val person = people.getJSONObject(i)
            txt += "${person.getString("fname")}, "

        }

        textView.text = txt
    }

    fun removePeoplDialog(view: View) {
        val people = ReportForm.peopleList
        if (people.length() > 0) {
            val removeDialog = AlertDialog.Builder(context)
            var setPeople = ArrayList<String>()
            removeDialog.setTitle("Remove People")


            for (i in 0..(people.length() - 1)) {
                val person = people.getJSONObject(i)
                val name = person.getString("fname")
                setPeople.add(name)
            }
            val array = arrayOfNulls<String>(setPeople.size)
            var peopleItem = setPeople.toArray(array)
            removeDialog.setItems(peopleItem
            ) { dialog, which ->
                Log.d("REMOVE_PEOPLE", "INDEX: $which")

                ReportForm.peopleList.remove(which)

                loadPeople(txtAddPeople)
            }
            removeDialog.show()
        }
    }

    // progress bbar
    fun showProgress () {
        progressBar.visibility = View.VISIBLE
        sending = true
    }
    fun hideProgress () {
        progressBar.visibility = View.INVISIBLE
        sending = false
    }

}
