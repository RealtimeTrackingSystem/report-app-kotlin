package com.johnhigginsmavila.rcrtskotlinapp.Utilities

import com.johnhigginsmavila.rcrtskotlinapp.Controller.App

// URLS

const val BASE_URL = "https://report-api-client.herokuapp.com"

const val LOGIN_URL = "${BASE_URL}/api/auth/signin"

const val SIGNUP_URL = "${BASE_URL}/api/auth/signup"

const val REFRESH_USER_URL = "$BASE_URL/api/auth/rehydrate"

const val FORGOT_PASSWORD_URL = "$BASE_URL/api/auth/password"

const val UPDATE_USER_URL = "$BASE_URL/api/auth/user"

const val UPDATE_PROFILE_PIC_URL = "$BASE_URL/api/auth/profilepic"

const val HOST_URL = "${BASE_URL}/api/hosts"

const val HOST_REQUEST_URL = "${HOST_URL}/requests"

const val REPORT_URL = "${BASE_URL}/api/reports"

const val UPDATE_FIREBASE_TOKEN_URL = "${BASE_URL}/api/reporters/firebase"

// EXTRAS

const val EXTRA_MAP_CALLED_FROM = "EXTRA_MAP_CALLED_FROM"

const val EXTRA_LOCATION = "EXTRA_LOCATION"

const val EXTRA_REPORT_DETAIL_JSON = "EXTRA_REPORT_DETAIL_JSON"

// VALUES

const val NEW_REPORT = "NEW_REPORT"