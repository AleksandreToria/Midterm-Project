package com.example.store.presentation.screen.log_in

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentLogInBinding
import com.example.store.presentation.base.BaseFragment
import com.example.store.presentation.event.auth.AuthEvent
import com.example.store.presentation.extension.showSnackBar
import com.example.store.presentation.state.auth.AuthViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()

    override fun bind() {
        arguments?.let {
            val safeArgs = LogInFragmentArgs.fromBundle(it)
            binding.email.setText(safeArgs.email)
            binding.password.setText(safeArgs.password)
        }
    }

    override fun bindViewActionListeners() {
        listeners()
    }

    override fun bindObserves() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logInState.collect {
                    handleLogInState(logInState = it)
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
                viewModel.onEvent(AuthEvent.LogIn(email, password))
            }
            createNewAcc.setOnClickListener {
                viewModel.navigateToRegister()
            }
        }
    }

    private fun handleLogInState(logInState: AuthViewState) {
        with(binding) {
            progressBar.visibility =
                if (logInState.isLoading) View.VISIBLE else View.GONE

            logInState.errorMessage?.let {
                root.showSnackBar(message = it)
                viewModel.onEvent(AuthEvent.ResetErrorMessage)
            }
        }
    }

    private fun handleNavigationEvents(event: LogInViewModel.LogInUiEvent) {
        when (event) {
            is LogInViewModel.LogInUiEvent.NavigateToHome -> {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
            }

            is LogInViewModel.LogInUiEvent.NavigateToRegister -> {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToRegisterFragment())
            }
        }
    }
}