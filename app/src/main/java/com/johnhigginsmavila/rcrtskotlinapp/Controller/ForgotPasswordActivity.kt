package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    fun showProgressBar() {
        resetPasswordProgressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        resetPasswordProgressBar.visibility = View.INVISIBLE
    }

    fun resetPassword(view: View) {
        hideKeyboard()
        val email = editText2.text.toString()
        if (email != "") {
            Log.d("EMAIL_VALUE", email)
            showProgressBar()
            AuthService.resetPassword(email)
                .subscribeOn(Schedulers.io())
                .subscribe{ success ->
                    hideProgressBar()
                    when(success) {
                        true -> {
                            editText2.setText("")
                            showToast("Please check your email for the temporary password")
                        }
                        else -> showToast(AuthService.authResponseError!!)
                    }
                }
                .run{}
        } else {
            showToast("Please Enter Your Email")
        }

    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
