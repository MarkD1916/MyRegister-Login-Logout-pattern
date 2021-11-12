package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentRegisterBinding
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.AuthStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import java.util.*

private const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    val mBinding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        loginIfAuth()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.goToLoginBtnId.setOnClickListener {
            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
        mBinding.registerBtnId.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.register_btn_id) {

            val userName = mBinding.loginEditTextTextPersonName.text.toString()
            val userPassword = mBinding.loginEditTextTextPassword.text.toString()

            val userResponse = authViewModel.createUserResponse(userName, userPassword)

            authViewModel.registerUser(userResponse).observe(viewLifecycleOwner) {
                if (it == RegisterStatus.SUCCESS) {
                    auth(userResponse)
                }

                if (it == RegisterStatus.FAIL) {
                    Toast.makeText(
                        APP_AUTH_ACTIVITY,
                        "You are already register",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun auth(userResponse: UserResponse) {
        authViewModel.authUser(userResponse).observe(viewLifecycleOwner) {
            when (it) {
                AuthStatus.SUCCESS -> {
                    APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
                }
                AuthStatus.FAIL -> {
                    Toast.makeText(APP_AUTH_ACTIVITY, "Sorry smth go wrong!", Toast.LENGTH_SHORT)
                        .show()
                }


            }

        }

    }

    private fun loginIfAuth() {
        authViewModel.getToken().observe(viewLifecycleOwner) {
            if (it != null) {
                authViewModel.verifyToken(AccessTokenResponse(it.access_token))
                    .observe(viewLifecycleOwner) {
                        when (it) {
                            TokenVerifyStatus.SUCCESS -> {
                                APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
                            }
                        }
                    }
            }
            else{
                mBinding.progressBar.visibility = View.GONE
            }
        }
    }

}