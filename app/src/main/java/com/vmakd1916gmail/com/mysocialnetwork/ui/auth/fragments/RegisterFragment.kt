package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentRegisterBinding
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.local.User
import com.vmakd1916gmail.com.mysocialnetwork.models.network.AccessTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.TokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.models.network.UserResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.RegisterStatus
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

private const val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)




        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.goToLoginBtnId.setOnClickListener {
            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }
        mBinding.registerBtnId.setOnClickListener(this)
        // TODO("Отрефакторить этот пиздец")
        authViewModel.getUserFromDB().observe(viewLifecycleOwner) {
            if (!it.isEmpty()) {
                authViewModel.getCurrentActiveUser(LoginUserStatus.ACTIVE)
                    .observe(viewLifecycleOwner) {
                        if (it!=null) {
                            authViewModel.getTokenByUserId(it.id).observe(viewLifecycleOwner) {
                                if (!it[0].token.isEmpty()) {
                                    authViewModel.verifyToken(AccessTokenResponse(it[0].token[0].access_token))
                                        .observe(viewLifecycleOwner) {
                                            if (it == RegisterStatus.SUCCESS) {

                                                APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
                                            }
                                        }
                                } else {
                                    mBinding.progressBar.visibility = View.GONE
                                }
                            }
                        }
                        else {
                            mBinding.progressBar.visibility = View.GONE
                        }
                    }
            }
            else{
                mBinding.progressBar.visibility = View.GONE
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }

    private fun createUserResponse(userName: String, userPassword: String): UserResponse {
        return UserResponse(userName, userPassword)
    }

    private fun createUser(userName: String, userPassword: String): User {
        return User(UUID.randomUUID(), userName, userPassword, LoginUserStatus.NOT_ACTIVE)
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
        if (v.id == R.id.register_btn_id) {

            val userName = mBinding.loginEditTextTextPersonName.text.toString()
            val userPassword = mBinding.loginEditTextTextPassword.text.toString()

            val userResponse = createUserResponse(userName, userPassword)

            val user = createUser(userName, userPassword)
            userResponse.let {
                authViewModel.registerUser(it).observe(viewLifecycleOwner) {

                    if (it == RegisterStatus.SUCCESS) {
                        authViewModel.authUser(userResponse).observe(viewLifecycleOwner) {

                            user.userLoginStatus = LoginUserStatus.ACTIVE

                            val token = createToken(user.id, it)
                            runBlocking {
                                authViewModel.insertUser(user)
                                authViewModel.insertToken(token)
                            }


                            APP_AUTH_ACTIVITY.navController.navigate(R.id.action_registerFragment_to_dataForUser)
                        }

                    }

                    if (it == RegisterStatus.FAIL) {
                        Toast.makeText(APP_AUTH_ACTIVITY,"You are already register", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }
}