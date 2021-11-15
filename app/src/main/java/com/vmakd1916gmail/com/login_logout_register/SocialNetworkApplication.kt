package com.vmakd1916gmail.com.login_logout_register

import android.app.Application
import com.vmakd1916gmail.com.login_logout_register.services.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SocialNetworkApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        NetworkMonitor(this).startNetworkCallback()
    }

    override fun onTerminate(){
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }
}
