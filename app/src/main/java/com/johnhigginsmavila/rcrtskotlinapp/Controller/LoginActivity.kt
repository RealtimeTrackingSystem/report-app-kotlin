package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import com.johnhigginsmavila.rcrtskotlinapp.Services.HostService
import com.johnhigginsmavila.rcrtskotlinapp.Utilities.SharedPrefs
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefs = SharedPrefs(this)

        progressBar = loginProgresBar

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
            showProgressBar()
            AuthService.loginUser(loginName, password)
                .subscribeOn(Schedulers.io())
                .flatMap { result ->
                    when(result) {
                        false -> Observable.just(false)
                        true -> {
                            try {
                                val userData = JSONObject(App.prefs.userData)
                                val hostArray = userData.getJSONArray("hosts")
                                if (hostArray.length() > 0) {
                                    val host = hostArray.getJSONObject(0)
                                    val _id = host.getString("_id")
                                    HostService.loadUserhost(_id)
                                } else {
                                    Observable.just(true)
                                }
                            } catch (e: JSONException) {
                                Observable.just(false)
                            }
                        }
                    }
                }
                .subscribe({ result ->
                    Log.d("API", result.toString())
                    hideProgressBar()
                    when (result) {
                        true -> {
                            // enableSpinner(false)
                            Log.d("USER", App.prefs.userData)
                            // showToast("You are logged in")
                            val intent = Intent(this, MenuActivity::class.java)

                            startActivity(intent)
                            finish()
                        }
                        else -> showToast(AuthService.authResponseError!!)
                    }
                }, { error ->
                    try {
                        Log.d("API-ERROR", error.localizedMessage)
                    }
                    catch (e: NullPointerException) {
                        Log.d("API-ERROR", error.localizedMessage)
                    }
                    catch (e: Exception) {
                        Log.d("API-ERROR", error.localizedMessage)
                    }
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

    fun showProgressBar () {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar () {
        progressBar.visibility = View.INVISIBLE
    }

    fun forgotPassword (view: View) {
        val forgotPasswordIntent = Intent(this, ForgotPasswordActivity::class.java)

        startActivity(forgotPasswordIntent)
    }


}
