package com.vmakd1916gmail.com.login_logout_register.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.login_logout_register.R
import com.vmakd1916gmail.com.login_logout_register.databinding.FragmentDataForUserBinding
import com.vmakd1916gmail.com.login_logout_register.models.Token
import com.vmakd1916gmail.com.login_logout_register.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.login_logout_register.other.EventObserver
import com.vmakd1916gmail.com.login_logout_register.ui.auth.VM.DataViewModel
import com.vmakd1916gmail.com.login_logout_register.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DataForUserFragment"

@AndroidEntryPoint
class DataForUserFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDataForUserBinding? = null
    val mBinding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModels()
    private var token: Token? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataForUserBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.getToken().observe(viewLifecycleOwner) {
            token = it
            token?.let {
                dataViewModel.getDataForLoginUser(it.access_token)
            }

        }

        dataViewModel.dataForLogginUserResponse.observe(viewLifecycleOwner, EventObserver(
            onError = {
                snackbar(it)
            },
            onLoading = {}
        ) {
            mBinding.answerFromServerAuthId.text = it
            mBinding.logoutButtonId.visibility = View.VISIBLE
        })

        dataViewModel.getDataForAllUser().observe(viewLifecycleOwner)
        {
            mBinding.answerFromServerAllId.text = it

        }
        mBinding.logoutButtonId.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.logout_button_id) {
            token?.let{
                dataViewModel.deleteToken(it){
                    APP_AUTH_ACTIVITY.navController.navigate(R.id.action_dataForUser_to_loginFragment)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}