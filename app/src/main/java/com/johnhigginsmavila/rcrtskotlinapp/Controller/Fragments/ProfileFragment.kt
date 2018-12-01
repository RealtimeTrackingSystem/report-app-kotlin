package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Controller.ChangePasswordActivity
import com.johnhigginsmavila.rcrtskotlinapp.Controller.ChangeProfilePictureActivity
import com.johnhigginsmavila.rcrtskotlinapp.Controller.LoginActivity
import com.johnhigginsmavila.rcrtskotlinapp.Model.User

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import com.johnhigginsmavila.rcrtskotlinapp.Services.UserService
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SharedPrefs
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.math.ln

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfileFragment : Fragment(), View.OnClickListener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var user: User
    lateinit var fnameTxt: TextView
    lateinit var lnameTxt: TextView
    lateinit var usernameTxt: TextView
    lateinit var aliasTxt: TextView
    lateinit var birthdayTxt: TextView
    lateinit var emailTxt: TextView
    lateinit var streetTxt: TextView
    lateinit var barangayTxt: TextView
    lateinit var cityTxt: TextView
    lateinit var regionTxt: TextView
    lateinit var countryTxt: TextView
    lateinit var zipTxt: TextView
    lateinit var maleRb: RadioButton
    lateinit var femaleRb: RadioButton
    lateinit var logoutBtn: ImageView
    lateinit var profileImg: ImageView
    lateinit var changePasswordTxt: TextView
    lateinit var profileTxt: TextView
    lateinit var setDateBtn: ImageButton


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

        val v = inflater.inflate(R.layout.fragment_profile, container, false)

//        val changePassBtn = v.findViewById<Button>(R.id.changePasswordBtn)
//
//        changePassBtn.setOnClickListener(this)
        val saveChangesBtn = v.findViewById<Button>(R.id.saveChangesBtn)
        changePasswordTxt = v.findViewById(R.id.changePasswordBtn)
        profileTxt = v.findViewById(R.id.profileTxt)
        saveChangesBtn.setOnClickListener(this)

        loadProfile(v)

        logoutBtn = v.findViewById(R.id.logoutBtn)
        setDateBtn = v.findViewById(R.id.getDateBtn)

        logoutBtn.setOnClickListener(this)

        changePasswordTxt.setOnClickListener(this)
        profileTxt.setOnClickListener(this)
        setDateBtn.setOnClickListener(this)

        // Inflate the layout for this fragment
        return v
    }

    override fun onClick (view: View) {
        println(view.id)
        when (view.id) {
            R.id.femaleRB -> {
                user.gender = "F"
                Log.d("GENDER", "you clicked Female")
            }
            R.id.maleRB -> {
                user.gender = "M"
                Log.d("GENDER", "you clicked Male")
            }
            R.id.saveChangesBtn -> {
                updateUserDetails()
            }
            R.id.logoutBtn -> {
                UserService.logout()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.changePasswordBtn -> {
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.profileTxt -> {
                val intent = Intent(context, ChangeProfilePictureActivity::class.java)
                startActivity(intent)
            }
            R.id.getDateBtn -> {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    user.setBirthday(year, monthOfYear, dayOfMonth)
                    birthdayTxt.setText(user.birthday)
                }, year, month, day)

                dpd.datePicker.maxDate = System.currentTimeMillis()

                dpd.show()
            }
        }
    }

    fun loadProfile (v: View) {
        fnameTxt = v.findViewById<TextView>(R.id.fnameTxt)
        lnameTxt = v.findViewById<TextView>(R.id.lnameTxt)
        usernameTxt = v.findViewById<TextView>(R.id.usernameTxt)
        aliasTxt = v.findViewById<TextView>(R.id.aliasTxt)
        birthdayTxt = v.findViewById<TextView>(R.id.birthDayTxt)
        emailTxt = v.findViewById<TextView>(R.id.emailTxt)
        streetTxt = v.findViewById<TextView>(R.id.streetTxt)
        barangayTxt = v.findViewById<TextView>(R.id.barangayTxt)
        cityTxt = v.findViewById<TextView>(R.id.cityTxt)
        regionTxt = v.findViewById<TextView>(R.id.regionTxt)
        countryTxt = v.findViewById<TextView>(R.id.countryTxt)
        zipTxt = v.findViewById<TextView>(R.id.zipTxt)
        maleRb = v.findViewById<RadioButton>(R.id.maleRB)
        femaleRb = v.findViewById<RadioButton>(R.id.femaleRB)

        birthdayTxt.isEnabled = false

        val userData = JSONObject(App.prefs.userData)

        Log.d("user_data", userData.toString())
        user = User(userData)

        fnameTxt.text = user.fname
        lnameTxt.text = user.lname
        usernameTxt.text = user.username
        aliasTxt.text = user.alias
        birthdayTxt.text = user.birthday
        emailTxt.text = user.email
        streetTxt.text = user.street
        barangayTxt.text = user.barangay
        cityTxt.text = user.city
        regionTxt.text = user.region.capitalize()
        countryTxt.text = user.country
        zipTxt.text = user.zip


        if (user.gender == "M") {
            maleRb.isChecked = true
            femaleRb.isChecked = false
        } else {
            maleRb.isChecked = false
            femaleRb.isChecked = true
        }
        loadProfilePic(v)
        maleRb.setOnClickListener(this)
        femaleRb.setOnClickListener(this)
    }

    fun updateUserDetails () {
        user.fname = fnameTxt.text.toString()
        user.lname = lnameTxt.text.toString()
        user.username = usernameTxt.text.toString()
        user.email = emailTxt.text.toString()
        user.alias = aliasTxt.text.toString()
        user.street = streetTxt.text.toString()
        user.barangay = barangayTxt.text.toString()
        user.city = cityTxt.text.toString()
        user.region = regionTxt.text.toString()
        user.country = countryTxt.text.toString()
        user.zip = zipTxt.text.toString()
        user.birthday = birthdayTxt.text.toString()

        val updatedUser = user.toJson()
        println(updatedUser.toString())
        AuthService.updateProfileDetails(user)
            .flatMap {
                when (it) {
                    true -> AuthService.refreshUserDataAndLoadHost()
                    else -> Observable.just(it)
                }

            }
            .subscribeOn(Schedulers.io())
            .subscribe {
                when (it) {
                    true -> {
                        loadToast("Successfully Updated Profile")
                    }
                    else -> {
                        loadToast(AuthService.authResponseError!!)
                    }
                }
            }
            .run {  }
    }

    fun loadProfilePic (v: View) {

        try {
            profileImg = v.findViewById(R.id.profileImg)

            val image = user.profilePicture.getJSONObject("metaData")
            val imgUrl = image.getString("secure_url")

            Glide.with(context).load(Uri.parse(imgUrl)).into(profileImg)
        } catch (e: JSONException) {
            Log.d("LOAD_PIC_ERROR_JSON", e.localizedMessage)
        } catch (e: GlideException) {
            Log.d("LOAD_PIC_ERROR_GLIDE", e.localizedMessage)
        } catch (e: Exception) {
            Log.d("LOAD_PIC_ERROR", e.localizedMessage)
        }
    }

    fun loadToast (text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
