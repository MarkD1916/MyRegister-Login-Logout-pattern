package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentDataForUserBinding
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.LoginUserStatus
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DataForUserFragment"

@AndroidEntryPoint
class DataForUserFragment:Fragment(), View.OnClickListener {
    private var _binding: FragmentDataForUserBinding? = null
    val mBinding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModels()

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
        mBinding.logoutButtonId.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        dataViewModel.getCurrentActiveUser(LoginUserStatus.ACTIVE).observe(viewLifecycleOwner){
            if(it!=null) {
                dataViewModel.getTokenByUserId(it.id).observe(viewLifecycleOwner) {
                    dataViewModel.getDataForLoginUser(it[0].token[0].access_token)
                        .observe(viewLifecycleOwner) {
                            mBinding.answerFromServerAuthId.text = it
                            mBinding.logoutButtonId.visibility = View.VISIBLE
                        }
                }
            }
        }

        dataViewModel.getDataForAllUser().observe(viewLifecycleOwner){
            mBinding.answerFromServerAllId.text = it
        }
    }

    override fun onClick(v: View) {
        if (v.id==R.id.logout_button_id){
            dataViewModel.getCurrentActiveUser(LoginUserStatus.ACTIVE).observe(viewLifecycleOwner){
                if (it!=null) {
                    dataViewModel.updateUserStatus(LoginUserStatus.NOT_ACTIVE, it.id)

                    dataViewModel.getTokenByUserId(it.id).observe(viewLifecycleOwner) {
                        dataViewModel.logoutUser(it[0].token[0])
                        APP_AUTH_ACTIVITY.navController.navigate(R.id.action_dataForUser_to_loginFragment)

                    }
                }
            }
        }
    }
}