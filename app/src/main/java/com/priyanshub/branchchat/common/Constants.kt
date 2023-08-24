package com.priyanshub.branchchat.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Constants {
    companion object {
        const val BASE_URL = "YOUR BASE URL"
        const val SHARED_PREF_NAME = "sp_branch"
        const val TOKEN = "token"

        fun snackBarTemplate(view: View, text: String): Snackbar {
            return Snackbar.make(view,text, Snackbar.LENGTH_INDEFINITE)
        }
    }
}