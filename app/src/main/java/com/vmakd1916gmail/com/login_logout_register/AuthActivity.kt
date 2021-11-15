package com.vmakd1916gmail.com.login_logout_register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.vmakd1916gmail.com.login_logout_register.databinding.ActivityAuthBinding
import com.vmakd1916gmail.com.login_logout_register.other.APP_AUTH_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "AuthActivity"

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private var _binding: ActivityAuthBinding? = null
    val mBinding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        APP_AUTH_ACTIVITY=this
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}