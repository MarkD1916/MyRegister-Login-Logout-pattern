package com.vmakd1916gmail.com.mysocialnetwork

import android.app.Application
import com.vmakd1916gmail.com.mysocialnetwork.services.NetworkMonitor
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
