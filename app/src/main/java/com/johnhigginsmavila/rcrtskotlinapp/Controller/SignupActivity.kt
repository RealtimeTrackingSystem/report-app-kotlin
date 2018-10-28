package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewUser
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    var newUser: NewUser = NewUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        newUser.gender = "M"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            Log.d("view", view.getId().toString())
            Log.d("view", R.id.maleRB.toString())
            when (view.getId()) {
                R.id.maleRB ->
                    if (checked) {
                        newUser.gender = "M"
                    }
                else ->
                    if (checked) {
                        newUser.gender = "M"
                    }
            }
        }
    }

    fun onSignupClicked (view: View) {
        newUser.username = usernameTxt.text.toString()
        newUser.email = emailTxt.text.toString()
        newUser.lname = lnameTxt.text.toString()
        newUser.fname = fnameTxt.text.toString()
        newUser.alias = aliasTxt.text.toString()
        newUser.street = streetTxt.text.toString()
        newUser.barangay = brgyTxt.text.toString()
        newUser.city = cityTxt.text.toString()
        newUser.region = rgnTxt.text.toString()
        newUser.country = countryTxt.text.toString()
        newUser.zip = zipTxt.text.toString()
        newUser.password = passwordTxt.text.toString()
        newUser.passwordConfirmation = passwordConfirmation.text.toString()
        if (!newUser.isValid()) {
            toast("Please Complete the form")
        } else if (newUser.passwordConfirmation != newUser.password) {
            toast("Passwords do not match")
        } else {
            AuthService.registerUser(newUser)
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    Log.d("API", result.toString())
                    when (result) {
                        true -> {
                            // enableSpinner(false)
                            Log.d("USER", App.prefs.userData)
                            // showToast("You are logged in")
                            val intent = Intent(this, MenuActivity::class.java)

                            startActivity(intent)
                            finish()
                        }
                        else -> toast("Error")
                    }
                }, { error ->
                    Log.d("API-ERROR", error.localizedMessage)
                })
                .run{ Log.d("API", "Hello") }
        }
    }

    fun toast (text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
