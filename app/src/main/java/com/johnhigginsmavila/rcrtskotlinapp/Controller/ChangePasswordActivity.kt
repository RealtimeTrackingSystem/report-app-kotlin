package com.johnhigginsmavila.rcrtskotlinapp.Controller

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.johnhigginsmavila.rcrtskotlinapp.R
import com.johnhigginsmavila.rcrtskotlinapp.Services.AuthService
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    fun showProgressBar() {
        changePasswordProgressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        changePasswordProgressBar.visibility = View.INVISIBLE
    }

    fun changePassword(view: View) {
        val oldPassword = oldPasswordTxt.text.toString()
        val password = newPasswordTxt.text.toString()
        val confirmation = confirmPasswordTxt.text.toString()

        if (oldPassword == "" || password == "" || confirmation == "") {
            showToast("Please complete the form")
        } else if (password != confirmation) {
            showToast("Passwords do not match")
        } else {
            showProgressBar()
            AuthService.changePassword(oldPassword, password, confirmation)
                .subscribeOn(Schedulers.io())
                .subscribe { success ->
                    hideProgressBar()
                    when (success) {
                        true -> {
                            oldPasswordTxt.setText("")
                            newPasswordTxt.setText("")
                            confirmPasswordTxt.setText("")
                            showToast("Password Successfully Changed")
                        }
                        false -> {
                            showToast(AuthService.authResponseError!!)
                        }
                    }
                }
                .run {  }
        }
    }

    fun showToast (text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
