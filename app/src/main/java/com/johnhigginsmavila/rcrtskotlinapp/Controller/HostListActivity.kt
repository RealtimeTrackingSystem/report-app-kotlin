package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.Adapters.HostListAdapter
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.HostService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_host_list.*
import org.json.JSONArray
import org.json.JSONException

class HostListActivity : AppCompatActivity() {

    lateinit var adapter: HostListAdapter

    var btnPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_list)

        loadHosts { hosts ->
            adapter = HostListAdapter(this, hosts) { host ->
                if (!btnPressed) {
                    sendRequest(host)
                    Log.d("SELECT_HOST", host._id)
                }
            }

            hostListView.adapter = adapter
            val layoutManager = LinearLayoutManager(this)

            hostListView.layoutManager = layoutManager
            hostListView.setHasFixedSize(true)
        }

    }


    fun loadHosts (cb: (ArrayList<Host>) -> Unit) {
        HostService.getHosts()
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d("HOSTS_LIST", it.toString())
                val hosts = ArrayList<Host>()

                for (i in 0..(it.length() - 1)) {
                    try {
                        val host = Host(it.getJSONObject(i))

                        hosts.add(host)
                    }
                    catch (e: JSONException) {
                        Log.d("LOAD_HOST", e.localizedMessage)
                    }
                }

                cb(hosts)
            }
            .run {}
    }

    fun sendRequest (host: Host) {

        btnPressed = true
        hostlistProgressBar.visibility = View.VISIBLE

        HostService.sendHostRequest(host)
            .subscribeOn(Schedulers.io())
            .subscribe {
                hostlistProgressBar.visibility = View.INVISIBLE
                if (it) {
                    Toast.makeText(this, "Successfully Sent Request", Toast.LENGTH_SHORT).show()
                    done()
                } else {
                    Toast.makeText(this, "Successfully Sent Request", Toast.LENGTH_SHORT).show()
                    btnPressed = false
                }
            }
            .run {}
    }

    fun done () {

        val menu = Intent(this, MenuActivity::class.java)
        Thread.sleep(2000)
        startActivity(menu)
        finish()
    }
}
