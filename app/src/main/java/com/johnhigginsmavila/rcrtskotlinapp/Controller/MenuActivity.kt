package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.HostFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.ProfileFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.SendReportFragment
import com.johnhigginsmavila.rcrtskotlinapp.Controller.Fragments.ViewReportsFragment
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.UserService
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*

class MenuActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (App.prefs.authToken == "") {
            val intent = Intent(this, MainActivity::class.java)
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
}
