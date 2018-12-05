package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.Model.NewUser
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    var newUser: NewUser = NewUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        newUser.gender = "M"

        birthdayTxt.isEnabled = false
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
        hideKeyboard()
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
            signupScrollView.visibility = View.VISIBLE
            AuthService.registerUser(newUser)
                .subscribeOn(Schedulers.io())
                .flatMap { result ->
                    when (result) {
                        false -> Observable.just(false)
                        true -> {
                            try {
                                val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                                val token = App.prefs.firebaseToken
                                AuthService.updateFirebaseToken(deviceId, token)
                            } catch (e: Exception) {
                                Observable.just(false)
                            }
                        }
                    }
                }
                .subscribe{ result ->
                    Log.d("API", result.toString())
                    when (result) {
                        true -> {
                            // enableSpinner(false)
                            Log.d("USER", App.prefs.userData)
                            // showToast("You are logged in")
                            val intent = Intent(this, LoginActivity::class.java)

                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            toast(AuthService.authResponseError!!)
                            signupScrollView.visibility = View.INVISIBLE
                        }
                    }
                }
                .run{  }
        }
    }

    fun toast (text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun selectDate (view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
           newUser.setBirthday(year, monthOfYear, dayOfMonth)
            birthdayTxt.setText(newUser.birthday)
        }, year, month, day)

        dpd.datePicker.maxDate = System.currentTimeMillis()

        dpd.show()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
