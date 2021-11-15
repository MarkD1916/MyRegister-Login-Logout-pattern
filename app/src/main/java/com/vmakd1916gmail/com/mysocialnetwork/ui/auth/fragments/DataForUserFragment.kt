package com.vmakd1916gmail.com.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vmakd1916gmail.com.mysocialnetwork.R
import com.vmakd1916gmail.com.mysocialnetwork.databinding.FragmentDataForUserBinding
import com.vmakd1916gmail.com.mysocialnetwork.models.Token
import com.vmakd1916gmail.com.mysocialnetwork.models.network.VerifyTokenResponse
import com.vmakd1916gmail.com.mysocialnetwork.other.APP_AUTH_ACTIVITY
import com.vmakd1916gmail.com.mysocialnetwork.repositories.auth.TokenVerifyStatus
import com.vmakd1916gmail.com.mysocialnetwork.ui.auth.VM.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DataForUserFragment"

@AndroidEntryPoint
class DataForUserFragment : Fragment(), View.OnClickListener {
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

//        observerToken = Observer {
//            token = it ?: Token()
//            dataViewModel.verifyToken(VerifyTokenResponse(token.access_token))
//                .observe(viewLifecycleOwner) {
//                    when (it) {
//                        TokenVerifyStatus.SUCCESS -> {
//                            dataViewModel.getDataForLoginUser(token.access_token)
//                                .observe(viewLifecycleOwner) {
//                                    mBinding.answerFromServerAuthId.text = it
//                                }
//                            mBinding.logoutButtonId.visibility = View.VISIBLE
//                        }
//                        TokenVerifyStatus.FAIL ->{
//
//                        }
//                    }
//                }
//        }

//        dataViewModel.getToken().observe(viewLifecycleOwner, observerToken)

        dataViewModel.getDataForAllUser().observe(viewLifecycleOwner)
        {
            mBinding.answerFromServerAllId.text = it
        }
        mBinding.logoutButtonId.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.logout_button_id) {



        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}