package com.example.store.presentation.screen.forgot_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentForgotPasswordBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.forgot_password.ForgotPasswordEvents
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.screen.log_in.LogInFragmentDirections
import com.example.store.presentation.screen.log_in.LogInViewModel
import com.example.store.presentation.state.forgot_password.ForgotPasswordState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPassword @Inject constructor() :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun bind() {
    }

    override fun bindViewActionListeners() {
        binding.send.setOnClickListener {
            viewModel.onEvent(
                ForgotPasswordEvents.SendEmail(
                    email = binding.email.text.toString()
                )
            )

            viewModel.navigateToLogin()
        }
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forgotPasswordState.collect {
                    handleForgotPasswordState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    handleNavigationEvents(event = it)
                }
            }
        }
    }

    private fun handleForgotPasswordState(forgotPasswordState: ForgotPasswordState) {
        forgotPasswordState.errorMessage?.let {
            binding.root.showSnackBar(it)
            viewModel.onEvent(ForgotPasswordEvents.ResetErrorMessage)
        }
    }

    private fun handleNavigationEvents(event: ForgotPasswordViewModel.ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordViewModel.ForgotPasswordUiEvent.NavigateToLogin -> {
                findNavController().navigate(ForgotPasswordDirections.actionForgotPasswordToLogInFragment())
            }
        }
    }
}