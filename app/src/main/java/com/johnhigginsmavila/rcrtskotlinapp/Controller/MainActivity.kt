package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SharedPrefs
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = SharedPrefs(this)

        val token: String = prefs.authToken
        println(token)
        if (token != "") {
            val menuIntent = Intent(this, MenuActivity::class.java)
            startActivity(menuIntent)
            finish()
        }
    }

    fun onSignupBtnClicked (view: View) {
        val signupIntent = Intent(this, SignupActivity::class.java)

        startActivity(signupIntent)
    }

    fun onSigninClicked (view: View) {
        hideKeyboard()
        val loginName = loginNameInput.text.toString()
        val password = passwordInput.text.toString()
        if (loginName != "" && password != "") {
            AuthService.loginUser(loginName, password)
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
                        else -> showToast("Error")
                    }
                }, { error ->
                    Log.d("API-ERROR", error.localizedMessage)
                })
                .run{ Log.d("API", "Hello") }
        } else {
            showToast("Please Complete the form")
        }
    }

    fun showToast (text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
