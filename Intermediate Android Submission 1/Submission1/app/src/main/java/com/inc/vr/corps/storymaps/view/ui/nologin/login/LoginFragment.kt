package com.inc.vr.corps.storymaps.view.ui.nologin.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentLoginBinding
import com.inc.vr.corps.storymaps.model.LoginUser
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity_GeneratedInjector
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree


@AndroidEntryPoint
class LoginFragment :  Fragment(R.layout.fragment_login) {
    private val userModel by viewModels<UserViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        checkUserStatus()
        checkWelcome()
        playAnimation()
        setupAction()
        userModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

    }
    private fun setupAction() {
        binding.apply {
            binding.loginButton.setOnClickListener {
                userLogin()
                checkUserStatus()
            }

            binding.daftarText.setOnClickListener {
                //move navtigation to signup
                val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
                findNavController().navigate(action)
            }

        }
    }
    private fun userLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when {
            email.isEmpty() -> {
                binding.emailEditText.error = "Field Tidak Boleh Kosong"
            }
            password.isEmpty() -> {
                binding.passwordEditText.error = "Field Tidak Boleh Kosong"
            }
            else -> {
                if(checkEmail(email) &&  password.length >= 6) {
                    userModel.userLogin(LoginUser(email, password),requireContext())
                } else {
                    Toast.makeText(activity, "Silakan Isi Field Dengan Benar", Toast.LENGTH_SHORT).show()
                }

                userModel.loginResult.observe(requireActivity()) {
                    userModel.saveUserPreference(it)
                }
            }
        }

    }
    private fun checkWelcome(){
        val siapLogin = arguments?.get("siapLogin").toString()
        if(siapLogin.equals("siap")){
            //move with navigation to welcome

        }else{
            val action = LoginFragmentDirections.actionLoginFragmentToWelcomeFragment()
            findNavController().navigate(action)
        }
    }
    private fun checkUserStatus() {
        userModel.getUserPreferences().observe(requireActivity()) {
            if (it.token.trim() != "") {
                val intent = Intent(requireContext(), MainActivity::class.java)
                val activityNavigator = ActivityNavigator( requireContext())
                activityNavigator.navigate(
                    activityNavigator.createDestination().setIntent(
                        Intent(
                            intent
                        )
                    ), null, null, null
                )
            }
        }

    }
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            loginButton.isEnabled = !isLoading
            daftarText.isEnabled = !isLoading
            emailEditText.isEnabled = !isLoading
            passwordEditText.isEnabled = !isLoading
        }
    }
    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val daftarMessage =
            ObjectAnimator.ofFloat(binding.daftarMessage, View.ALPHA, 1f).setDuration(500)
        val daftarText = ObjectAnimator.ofFloat(binding.daftarText, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(title, message, emailTextView, emailEditTextLayout, passwordTextView,
                passwordEditText, passwordEditTextLayout, login, daftarMessage, daftarText)
            startDelay = 500
        }.start()
    }

}