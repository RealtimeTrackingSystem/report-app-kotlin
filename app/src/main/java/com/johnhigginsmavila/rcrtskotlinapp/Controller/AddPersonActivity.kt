package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.ReportForm
import kotlinx.android.synthetic.main.activity_add_person.*
import org.json.JSONObject

class AddPersonActivity : AppCompatActivity() {

    val person = JSONObject()
    var isCulprit = true
    var isCasualty = false
    var fname = ""
    var lname = ""
    var alias = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)
    }

    fun onIsCulpritRadioBtnClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when(view.getId()) {
                R.id.isCulpritYesBtn ->
                    if (checked) {
                        isCulprit = true
                        isCasualty = false
                    }
                else ->
                    if (checked) {
                        isCulprit = false
                        isCasualty = true
                    }
            }
        }
    }

    fun onAddPersonClicked(view: View) {
        fname = personFnameTxt.text.toString()
        lname = personLnameTxt.text.toString()
        alias = personAliasTxt.text.toString()
        if (fname != "") {
            if (alias == "") {
                alias = fname
            }

            person.put("fname", fname)
            person.put("lname", lname)
            person.put("alias", alias)
            person.put("isCulprit", isCulprit)
            person.put("isCasualty", isCasualty)

            Log.d("ADD_PERSON", person.toString())

            ReportForm.peopleList.put(person)

            val reportActivity = Intent(this, MenuActivity::class.java)

            startActivity(reportActivity)
            finish()

        } else {
            Toast.makeText(this, "Please fill up atleast firstname", Toast.LENGTH_SHORT).show()
        }
    }
}
