package com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.johnhigginsmavila.rcrtskotlinapp.Controller.ChangePasswordActivity
import com.johnhigginsmavila.rcrtskotlinapp.Controller.LoginActivity

import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.UserService

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

        val btnLogout = v.findViewById<View>(R.id.profileFragmentLogoutBtn)
        val changePassBtn = v.findViewById<Button>(R.id.changePasswordBtn)

        btnLogout.setOnClickListener(this)
        changePassBtn.setOnClickListener(this)

        // Inflate the layout for this fragment
        return v
    }

    override fun onClick (view: View) {
        println(view.id)
        println(R.id.profileFragmentLogoutBtn)
        when (view.id) {
            R.id.profileFragmentLogoutBtn -> {
                UserService.logout()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.changePasswordBtn -> {
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
