package com.johnhigginsmavila.rcrtskotlinapp.Services

import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import io.reactivex.schedulers.Schedulers

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        if (p0?.data != null) {
            Log.d("FIREBASE_MESSAGING", "DATA: ${p0.data.toString()}")
        }

        if (p0?.notification != null) {
            Log.d("FIREBASE_MESSAGING", "NOTIFICATION: ${p0.notification.toString()}")
        }
    }

    override fun onNewToken(token: String?) {
        Log.d("FIREBASE_MESSAGING", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token)
//        val deviceId = Settings.Secure.ANDROID_ID
//        if (token != null) {
//            AuthService.updateFirebaseToken(deviceId, token)
//                .subscribeOn(Schedulers.io())
//                .subscribe {
//                    if (!it) {
//                        val message = AuthService.authResponseError
//                        if (message != null) {
//                            Toast.makeText(App.prefs.context, message, Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(App.prefs.context, "Slow Internet Connection", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//                .run {}
//        }

        if (token != null) {
            App.prefs.firebaseToken = token
        }
    }

}