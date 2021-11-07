package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentLoginBinding
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentLoginBinding? = null
    val mBinding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mBinding.goToRegisterBtnId.setOnClickListener {
            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        mBinding.loginBtnId.setOnClickListener(this)
    }




    private fun createToken(user_id: UUID, tokenResponse: TokenResponse): Token {
        return Token(
            UUID.randomUUID(),
            user_id,
            tokenResponse.refresh_token,
            tokenResponse.access_token
        )
    }

    override fun onClick(v: View) {
        if (v.id == R.id.login_btn_id) {

            val userName = mBinding.registerEditTextTextPersonName.text.toString()
            val userPassword = mBinding.registerEditTextTextPassword.text.toString()

            authViewModel.getUserFromDB().observe(viewLifecycleOwner){
                it.forEach {
                    if (it.name==userName && it.password==userPassword){
                        val user = it
                        authViewModel.authUser(UserResponse(userName,userPassword)).observe(viewLifecycleOwner){
                            val token = Token(UUID.randomUUID(),user.id,it.refresh_token,it.access_token)
                            authViewModel.insertToken(token)
                            authViewModel.updateUserStatus(LoginUserStatus.ACTIVE,user.id)

                            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_loginFragment_to_dataForUser)



                        }
                    }
                }
            }

        }

    }
}