package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.HostFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.ProfileFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.SendReportFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.ViewReportsFragment
import com.johnhigginsmavila.rcrtskotlinapp.Model.User
import com.johnhigginsmavila.rcrtskotlinapp.R
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.lang.NullPointerException

class MenuActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var profileNameTxt: TextView
    lateinit var profileImg: ImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        displaySelectedScreen(R.id.menuSendReport)

        if (App.prefs.authToken == "") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loadProfile()

    }


    override fun onResume() {
        super.onResume()
        if (App.prefs.authToken == "") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.itemId)
        return true
    }

    fun displaySelectedScreen (id: Int) {
        var fragment: Fragment

        when (id) {
            R.id.menuSendReport ->  {
                fragment = SendReportFragment()
            }
            R.id.menuViewReports -> {
                fragment = ViewReportsFragment()
            }
            R.id.menuHost -> {
                fragment = HostFragment()
            }
            R.id.menuProfile ->  {
                fragment = ProfileFragment()
            }
            else -> {
                fragment = ViewReportsFragment()
            }
        }

        if (fragment != null) {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_menu, fragment)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    fun loadProfile () {

        try {
            val v = nav_view.getHeaderView(0)
            profileNameTxt = v.findViewById(R.id.menuProfileTxt)
            profileImg = v.findViewById(R.id.menuProfilePicImg)
            val userData = JSONObject(App.prefs.userData)

            val user = User(userData)
            val image = user.profilePicture.getJSONObject("metaData")
            val imgUrl = image.getString("secure_url")

            Glide.with(this).load(Uri.parse(imgUrl)).into(profileImg)



            profileNameTxt.text = "${user.fname} ${user.lname}"
        } catch (e: JSONException) {
            Log.d("LOAD_PROFILE_ERROR", e.localizedMessage)
        } catch (e: GlideException) {
            Log.d("LOAD_PROFILE_ERROR", e.localizedMessage)
        } catch (e: NegativeArraySizeException) {
            Log.d("LOAD_PROFILE_ERROR", e.localizedMessage)
        } catch (e: NullPointerException) {
            Log.d("LOAD_PROFILE_ERROR", e.localizedMessage)
        } catch (e: Exception) {
            Log.d("LOAD_PROFILE_ERROR", e.localizedMessage)
        }
    }
}
