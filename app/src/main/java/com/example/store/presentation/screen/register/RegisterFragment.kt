package com.example.store.presentation.screen.register

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentRegisterBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.auth.AuthEvent
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.auth.AuthViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun bind() {

    }

    override fun bindViewActionListeners() {
        listeners()
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerState.collect {
                    handleRegisterState(registerState = it)
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

    private fun listeners() {
        with(binding) {
            logInBtn.setOnClickListener {
                val email = email.text.toString()
                val password = password.text.toString()
                val confirmPassword = confirmPassword.text.toString()

                viewModel.onEvent(AuthEvent.Register(email, password, confirmPassword))
            }
            haveAcc.setOnClickListener {
                viewModel.onAlreadyHaveAccountClicked()
            }
        }
    }

    private fun handleRegisterState(registerState: AuthViewState) {
        with(binding) {
            binding.progressBar.visibility =
                if (registerState.isLoading) View.VISIBLE else View.GONE

            registerState.errorMessage?.let {
                root.showSnackBar(message = it)
                viewModel.onEvent(AuthEvent.ResetErrorMessage)
            }
        }
    }

    private fun handleNavigationEvents(event: RegisterViewModel.RegisterUiEvent) {
        when (event) {
            is RegisterViewModel.RegisterUiEvent.NavigateToLogin -> {
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLogInFragment(
                        email = event.email,
                        password = event.password
                    )
                )
            }

            is RegisterViewModel.RegisterUiEvent.AlreadyHaveAccountNavigation -> {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLogInFragment())
            }
        }
    }
}