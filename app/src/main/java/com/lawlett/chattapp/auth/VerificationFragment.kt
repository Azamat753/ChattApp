package com.lawlett.chattapp.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.lawlett.chattapp.R
import com.lawlett.chattapp.auth.utils.SharedPrefModule
import com.lawlett.chattapp.auth.utils.hasEmptyFields
import com.lawlett.chattapp.auth.utils.toast
import com.lawlett.chattapp.auth.utils.visible
import kotlinx.android.synthetic.main.fragment_verification.*
import java.util.concurrent.TimeUnit


class VerificationFragment : Fragment(R.layout.fragment_verification), TextWatcher {

    private var token: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    var smsInet: String? = null
    private var isCodeSent = false
    var code: String = ""
    var phoneNumber: String = ""
    lateinit var sendAgainTv: TextView
    lateinit var timerTextViews: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendAgainTv = view.findViewById(R.id.tv_send_again)
        timerTextViews = view.findViewById(R.id.timerTextView)
        init()
    }

    private fun init() {

        phoneNumber = arguments?.getString("PHONE_NUMBER").toString()
        requestOTP()
        setOnClickAction()
        wrong_number.text = phoneNumber
        timerStart()
        numb1.addTextChangedListener(this)
    }

    private fun requestOTP() {
        token = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.e("Firebase verification", "onVerificationCompleted")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("Firebase verification", "onVerificationCompleted" + e.message)
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                isCodeSent = true
                smsInet = s
            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this.requireActivity(),
            token!!
        )
    }

    private fun setOnClickAction() {
        tv_send_again.setOnClickListener {
            requestOTP()
            timerStart()
        }
        btn_back3.setOnClickListener {
            findNavController().popBackStack()
        }

        btnSend.setOnClickListener {
            code =
                numb1.text.toString()
            if (code.length == 6) {
                val credential =
                    PhoneAuthProvider.getCredential(smsInet!!, code)
                signIn(credential)
            } else {
                requireActivity().toast(resources.getString(R.string.six_number_code))
            }
        }
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance()
            .signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    img_success.visible()
                    requireActivity().toast(getString(R.string.success))
                    Log.e("TAG", task.result?.user?.uid!!)

                    SharedPrefModule(this.requireContext()).TokenModule()
                        .saveToken(task.result?.user?.uid)
                    SharedPrefModule(this.requireContext()).FirstTimeModule().saveShown()
                    findNavController().navigate(
                        R.id.action_verification_fragment_to_chatFragment
                    )
                } else {
                    Toast.makeText(requireContext(), "Неправильный смс-код", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("TAG", "Ошибка авторизации")
                    Log.e("TAG", task.exception!!.message.toString())
                }
            }
    }

    private fun timerStart() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millis: Long) {
                sendAgainTv?.isClickable = false
                var seconds = (millis / 1000).toInt()
                val minutes = seconds / 620
                seconds %= 60
                timerTextViews.text = "0" + String.format("%d:%02d", minutes, seconds) + " сек"
            }

            override fun onFinish() {
                timerTextViews.text = "0 секунд"
                sendAgainTv?.isClickable = true
            }
        }.start()
    }

    override fun afterTextChanged(p0: Editable?) {
        if (hasEmptyFields(
                numb1
            )
        ) {
            btnSend.isClickable = true
            btnSend.setBackgroundResource(R.drawable.rounded_shape_green_12dp_8cc341)
        } else {
            btnSend.isClickable = false
            btnSend.setBackgroundResource(R.drawable.rounded_shape_silver_12dp_7b818c)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
}