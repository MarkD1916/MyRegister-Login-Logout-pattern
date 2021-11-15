package com.vmakd1916gmail.com.login_logout_register.ui

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Fragment.snackbar(text: String) {
    Snackbar.make(
        requireView(),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}