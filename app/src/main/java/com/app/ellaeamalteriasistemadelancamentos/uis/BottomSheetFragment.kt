package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentBottomSheetBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.app.ellaeamalteriasistemadelancamentos.utils.DateMaskTextWatcher
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private val viewModel: ViewModelLancamentos by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater).also {
            viewLifecycleOwner
        }
        binding.aniversario.addTextChangedListener(DateMaskTextWatcher(binding.aniversario))
        cadastrarCilaborador()
        observer()
        return binding.root
    }

    private fun cadastrarCilaborador() {
        binding.btnCadastrarCollaborator.setOnClickListener {
            if (verificate()){
                viewModel.novoCollaBorator(collaboratorObj())
            }
        }
    }

    private fun collaboratorObj(): Collaborators{
        return Collaborators(
            nome = binding.nome.text.toString(),
            email = binding.email.text.toString(),
            aniversario = binding.aniversario.text.toString(),
            senha =   binding.senha.text.toString()
        )
    }

    private fun verificate(): Boolean {
        var verify = false

        if (binding.nome.text.toString().isBlank() ||
            binding.email.text.toString().isBlank() ||
            binding.aniversario.text.toString().isBlank() ||
            binding.senha.text.toString().isBlank()
        ) {
            verify = false
            toast("Textos obrigatórios em branco!")
        } else {
            verify = true
        }

        return verify
    }

    private fun observer(){
        viewModel.novoCollaborator.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast("Colaborador não cadastrado pelo seguinte: ${state.error}")
                    binding.txtBtnCadastrar.show()
                    binding.progressBtnCadastrar.hide()
                }
                UiState.Loading -> {
                    binding.txtBtnCadastrar.hide()
                    binding.progressBtnCadastrar.show()
                }
                is UiState.Success -> {
                    binding.txtBtnCadastrar.show()
                    binding.progressBtnCadastrar.hide()
                    binding.nome.setText("")
                    binding.email.setText("")
                    binding.aniversario.setText("")
                    binding.senha.setText("")
                    toast("Colaborador cadastrado com sucesso!")
                }
            }

        }
    }

}