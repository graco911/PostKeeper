package com.gracodev.postkeeper.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import com.gracodev.postkeeper.Utils.snackbarError
import com.gracodev.postkeeper.Utils.snackbarSuccess
import com.gracodev.postkeeper.Utils.toEditable
import com.gracodev.postkeeper.data.models.BlogPostData
import com.gracodev.postkeeper.databinding.FragmentEntryFormBinding
import com.gracodev.postkeeper.ui.activities.MainActivity
import com.gracodev.postkeeper.ui.states.UIStates
import com.gracodev.postkeeper.ui.viewmodels.BlogViewModel

class EntryFormFragment : BaseFragment() {

    override var TAG: String = this.javaClass.name

    private val viewModel: BlogViewModel by activityViewModels()

    private val binding: FragmentEntryFormBinding by lazy {
        FragmentEntryFormBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTextFields()
        setUpOnClickListeners()
        viewModelInitialData()
        setUpObservables()
    }

    override fun onResume() {
        super.onResume()
        hideFAB()
    }

    override fun onDestroy() {
        super.onDestroy()
        revealFAB()
    }

    private fun revealFAB() {
        (requireActivity() as MainActivity).revealFAB()
    }

    private fun hideFAB() {
        (requireActivity() as MainActivity).hideFAB()
    }

    private fun setUpObservables() {
        viewModel.apply {
            textChanged.observe(viewLifecycleOwner) {
                validateInputs()
            }
            enabledButton.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSave.isEnabled = isEnabled
            }
            addPostResultLiveData.observe(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is UIStates.Error -> handleError(uiState)
                    UIStates.Init -> {}
                    UIStates.Loading -> dialog.show(childFragmentManager, TAG)
                    is UIStates.Success -> handleSuccess(uiState)
                }
            }
        }
    }

    private fun handleSuccess(uiState: UIStates.Success<Unit>) {
        dialog.dismiss()
        clearForm()
        requireView().snackbarSuccess("Entrada registrada correctamente.")
    }

    private fun clearForm() {
        viewModel.setBlogPostData(BlogPostData())
        binding.apply {
            etTitle.text = "".toEditable()
            etAuthor.text = "".toEditable()
            etBody.text = "".toEditable()

            tilTitle.isErrorEnabled = false
            tilAuthor.isErrorEnabled = false
            tilBody.isErrorEnabled = false
        }
    }

    private fun handleError(error: UIStates.Error) {
        dialog.dismiss()
        requireView().snackbarError(error.message)
    }

    private fun viewModelInitialData() {
        // Unicamente para pruebas.
        /*viewModel.setBlogPostData(
            BlogPostData(
                "Hola Mundo desde Android",
                "Carlos",
                0,
                "Esta entrada esta hecha desde el dispositivo android para probar el correcto funcionamiento de la aplicacion en un dispositivo.",
                ""
            )
        )*/
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                viewModel?.addBlogPost()
            }
        }
    }

    private fun validateInputs() {
        viewModel.enableSaveButton(
            binding.etTitle.text.toString().isNotEmpty() &&
                    binding.etAuthor.text.toString().isNotEmpty() &&
                    binding.etBody.text.toString().isNotEmpty()
        )
    }

    private fun setUpTextFields() {
        binding.apply {
            etTitle.doOnTextChanged { text, _, _, _ ->
                handleTextFieldValidation(text.toString(), tilTitle)
            }
            etAuthor.doOnTextChanged { text, _, _, _ ->
                handleTextFieldValidation(text.toString(), tilAuthor)
            }
            etBody.doOnTextChanged { text, _, _, _ ->
                handleTextFieldValidation(text.toString(), tilBody)
            }
        }
    }

    private fun handleTextFieldValidation(text: String, textInputLayout: TextInputLayout) {
        if (text.isEmpty() || text.isBlank()) {
            textInputLayout.error = "Este campo no puede estar vacío."
        } else {
            textInputLayout.isErrorEnabled = false
        }

        viewModel.notifyTextChanged()
    }
}