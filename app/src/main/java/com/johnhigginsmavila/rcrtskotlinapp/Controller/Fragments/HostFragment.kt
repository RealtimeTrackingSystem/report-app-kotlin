package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Controller.HostListActivity
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.Model.HostMember

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import com.johnhigginsmavila.rcrtskotlinapp.Services.UserService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_host.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HostFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HostFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var nhRBtn: View? = null
    var nhTiTxt: View? = null
    var whHDTxt: View? = null
    var whHImg: View? = null
    var whHNTxt: View? = null
    var whSLbl: View? = null
    var whSTxt: View? = null
    var whMBtn: View? = null


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
        val v = inflater.inflate(R.layout.fragment_host, container, false)

        loadView(v)
        loadUserData()

        nhRBtn?.setOnClickListener { view: View ->
            val hostListIntent = Intent(App.prefs.context, HostListActivity::class.java)

            startActivity(hostListIntent)
        }

        return v
    }

    private fun showToast (text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun loadView (v: View) {
        nhRBtn = v.findViewById(R.id.nhRequestBtn)
        nhTiTxt = v.findViewById(R.id.nhTitleTxt)

        whHDTxt = v.findViewById(R.id.whHostDescTxt)
        whHImg = v.findViewById<ImageView>(R.id.whHostImg)
        whHNTxt = v.findViewById(R.id.whHostNameTxt)
        whSLbl = v.findViewById(R.id.whStatusLbl)
        whSTxt = v.findViewById(R.id.whStatusTxt)
        whMBtn = v.findViewById(R.id.whMapBtn)

        setImage(v)
    }

    private fun populateHostData () {
        try {
            val userData = JSONObject(App.prefs.userData)
            Log.d("USER_DATA", userData.toString())
            val hostData = JSONObject(App.prefs.userHost)
            val hostMember: HostMember = HostMember(userData.getJSONArray("hosts").getJSONObject(0), hostData)
            Log.d("USER_DATA", hostMember._id)
            whHostNameTxt.text = hostMember.hostDetails?.name
            whHostDescTxt.text = hostMember.hostDetails?.description
            if (hostMember.isBlocked != null && hostMember?.isBlocked == true) {
                whStatusTxt.text = "Pending Request"
            } else {
                whStatusTxt.text = "Member"
            }

            whHostDescTxt.text = hostMember.hostDetails?.description
            Log.d("USER_DATA", hostMember.hostDetails?.description)
        }
        catch (e: JSONException) {
            Log.d("ERROR", e.localizedMessage)
        }
        catch(e: Exception) {
            Log.d("ERROR", e.localizedMessage)
        }
    }

    private fun loadUserData () {
        AuthService.refreshUserDataAndLoadHost()
            .subscribeOn(Schedulers.io())
            .subscribe {
                val userHost = App.prefs.userHost
                Log.d("USER_HOST", userHost)
                if (!it) {
                    showToast("An Error Occured While loading Host Details")
                } else if (userHost == "") {
                    showNoHostView()
                } else {
                    populateHostData()
                    showWithHostView()
                }
            }
            .run {}
    }

    fun showNoHostView() {
        nhRBtn?.visibility = View.VISIBLE
        nhTiTxt?.visibility = View.VISIBLE

        whHDTxt?.visibility = View.GONE
        whHImg?.visibility = View.GONE
        whHNTxt?.visibility = View.GONE
        whSLbl?.visibility = View.GONE
        whSTxt?.visibility = View.GONE
        whMBtn?.visibility = View.GONE
    }

    fun showWithHostView () {
        nhRBtn?.visibility = View.GONE
        nhTiTxt?.visibility = View.GONE

        whHDTxt?.visibility = View.VISIBLE
        whHImg?.visibility = View.VISIBLE
        whHNTxt?.visibility = View.VISIBLE
        whSLbl?.visibility = View.VISIBLE
        whSTxt?.visibility = View.VISIBLE
        whMBtn?.visibility = View.VISIBLE

    }

    fun setImage (v:View) {
        try {
            var userAvatar = "profileDefault"
            var avatarColor = "[0.5, 0.5, 0.5, 1]"
            val random = Random()
            val color = random.nextInt(2)
            val avatar = random.nextInt(28)

            if (color == 0) {
                userAvatar = "light$avatar"
            } else {
                userAvatar = "dark$avatar"
            }

            val r = random.nextInt(255)
            val g = random.nextInt(255)
            val b = random.nextInt(255)


            val resourceId = App.prefs.context.resources.getIdentifier(userAvatar, "drawable", App.prefs.context.packageName)
            val img = v.findViewById<ImageView>(R.id.whHostImg)
            img?.setImageResource(resourceId)
            img?.setBackgroundColor(Color.rgb(r,g,b))
        }
        catch (e: Exception) {
            Log.d("SET_IMAGE_ERROR", e.localizedMessage)
        }
    }

}
