package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentRegisterBinding
import com.vmakd1916gmail.com.mysocialnetwork.models.network.RefreshTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.other.EventObserver
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.Variables
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.AuthViewModel
import com.vmakd1916gmail.com.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

private const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    val mBinding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private var userResponse: UserResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.goToLoginBtnId.setOnClickListener {
            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
        mBinding.registerBtnId.setOnClickListener(this)
        mBinding.noLoginButtonId.setOnClickListener(this)

        authViewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                mBinding.progressBar.visibility = View.GONE
                snackbar(it)
            },
            onLoading = {
                mBinding.progressBar.visibility = View.VISIBLE
            }
        ) {
            mBinding.progressBar.visibility = View.GONE
            authViewModel.authUser(userResponse!!)

        })

        loginIfAuth()


        authViewModel.authStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                snackbar(it)
            },
            onLoading = {}
        ) {})
    }

    override fun onClick(v: View) {
        if (v.id == R.id.register_btn_id) {

            val userName = mBinding.loginEditTextTextPersonName.text.toString()
            val userPassword = mBinding.loginEditTextTextPassword.text.toString()

            userResponse = authViewModel.createUserResponse(userName, userPassword)

            authViewModel.registerUser(userResponse!!)


        }
        if (v.id == R.id.no_login_button_id) {
            if (APP_AUTH_ACTIVITY.navController.previousBackStackEntry != null) {
                APP_AUTH_ACTIVITY.navController.popBackStack()
            } else {
                APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
            }
        }
    }

    private fun loginIfAuth() {
        authViewModel.getToken().observe(viewLifecycleOwner) {
            it?.let { token ->
                it
                authViewModel.verifyToken(VerifyTokenResponse(token.access_token))
                authViewModel.verifyTokenResponse
                    .observe(viewLifecycleOwner, EventObserver(
                        onError = {
                            authViewModel.refreshToken(RefreshTokenResponse(token.refresh_token))
                        },
                        onLoading = {
                            mBinding.progressBar.visibility = View.VISIBLE
                        }
                    ) {
                        APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
                    })
            }
        }
        authViewModel.accessTokenResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                mBinding.progressBar.visibility = View.GONE
                snackbar(it)
            },
            onLoading = {}
        ) {

            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
        })
        mBinding.progressBar.visibility = View.GONE
    }
}