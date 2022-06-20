package com.inc.vr.corps.storymaps.view.ui.nologin.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentWelcomeBinding
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome){
    private lateinit var mBinding: FragmentWelcomeBinding
    val userModel : UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setupAction()
        playAnimation()
    }

    private fun checkUserStatus() {
        userModel.getUserPreferences().observe(requireActivity()) {
            if (it.token.trim() != "") {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun setupAction() {
        mBinding.loginButton.setOnClickListener {
           // startActivity(Intent(this, LoginActivity::class.java))
            //move navtigation to login
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment("siap")
            findNavController().navigate(action)
        }

        mBinding.signupButton.setOnClickListener {
            //move navtigation to signup
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToSignupFragment()
            findNavController().navigate(action)
        }
    }

    private fun playAnimation() {

        val login = ObjectAnimator.ofFloat(mBinding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(mBinding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(mBinding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(mBinding.descTextView, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }
}