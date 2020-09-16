package com.lawlett.chattapp.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lawlett.chattapp.R
import com.lawlett.chattapp.auth.utils.SharedPrefModule
import com.lawlett.chattapp.auth.utils.toast
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment(R.layout.fragment_auth), TextWatcher {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkedShow()
        et_reg_info.addTextChangedListener(this)
        listener()
    }
    private fun checkedShow() {
        var isShown: Boolean = SharedPrefModule(requireContext()).FirstTimeModule().isShown()
        if (isShown){
            findNavController().navigate(
                R.id.action_auth_fragment_to_chat_fragment
            )
        }
    }
    private fun listener() {
        btn_reg_info.setOnClickListener {
            if (et_reg_info.text.toString().isEmpty()) {
                requireActivity().toast(getString(R.string.enter_number_phone))
                return@setOnClickListener
            } else {
                val bundle = bundleOf("PHONE_NUMBER" to et_reg_info.text.toString())
                findNavController().navigate(
                    R.id.action_auth_fragment_to_verificationFragment, bundle
                )
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (et_reg_info.text.toString().count() == 18) {
            btn_reg_info.isClickable = true
            btn_reg_info.setBackgroundResource(R.drawable.rounded_shape_green_12dp_8cc341)
        } else {
            btn_reg_info.isClickable = false
            btn_reg_info.setBackgroundResource(R.drawable.rounded_shape_silver_12dp_7b818c)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}
