package com.inc.vr.corps.storymaps.view.ui.nologin.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentSignUpBinding
import com.inc.vr.corps.storymaps.model.RegisterUser
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment :  Fragment(R.layout.fragment_sign_up) {
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
               binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        playAnimation()
        setupAction()
        userViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun passwordValidate(password: String): Boolean {
        return password.length >= 6
    }
    private fun setupAction() {
        binding.apply {
            binding.masukText.setOnClickListener {
                //move navtigation to login
                val action = SignUpFragmentDirections.actionSignupFragmentToLoginFragment()
                findNavController().navigate(action)
            }

            binding.signupButton.setOnClickListener{
                userRegister()
            }
        }
    }


    private fun userRegister() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when{
            name.isEmpty() -> {
                binding.nameEditText.error = "Field Tidak Boleh Kosong"
            }
            email.isEmpty() -> {
                binding.emailEditText.error = "Field Tidak Boleh Kosong"
            }
            password.isEmpty() -> {
                binding.passwordEditText.error = "Field Tidak Boleh Kosong"
            }
            else -> {
                if(checkEmail(email) &&  password.length >= 6) {
                    userViewModel.userRegister(RegisterUser(name, email, password), requireActivity())
                } else {
                    Toast.makeText(requireActivity(), "Silakan Isi Field Dengan Benar", Toast.LENGTH_SHORT).show()
                }

                userViewModel.loginResult.observe(requireActivity()) {
                    if(it.token != "") {
                        userViewModel.saveUserPreference(it)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }

        binding.apply {
            binding.signupButton.isEnabled = !isLoading
            binding.masukText.isEnabled = !isLoading
            binding.nameEditText.isEnabled = !isLoading
            binding.emailEditText.isEnabled = !isLoading
            binding.passwordEditText.isEnabled =!isLoading
        }
    }
    private fun checkEmail(email : String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val masukMessage = ObjectAnimator.ofFloat(binding.masukMessage, View.ALPHA, 1f).setDuration(500)
        val masukText = ObjectAnimator.ofFloat(binding.masukText, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                passwordEditText,
                signup,
                masukMessage,
                masukText
            )
            startDelay = 500
        }.start()
    }
}