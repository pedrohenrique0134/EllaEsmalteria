package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentCadastroClienteBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.utils.DateMaskTextWatcher
import com.app.ellaeamalteriasistemadelancamentos.utils.TelefoneTextWatcher
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.createDialog
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CadastroClienteFragment : Fragment() {
    private lateinit var binding: FragmentCadastroClienteBinding
    private val viewModel: ViewModelLancamentos by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCadastroClienteBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        binding.telefoneCadastro.addTextChangedListener(TelefoneTextWatcher())
        binding.aniversarioCadastro.addTextChangedListener(DateMaskTextWatcher(binding.aniversarioCadastro))
        back()
        cadastarCliente()
        observer()
        return binding.root
    }

    private fun cadastarCliente() {
        binding.btnCadastrarCliente.setOnClickListener {
            if (verific()) {
                viewModel.novoCliente(
                    binding.nomeCadastro.text.toString(),
                    clienteObj()
                )
            }

        }
    }

    private fun observer() {
        viewModel.novoCliente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    binding.progressBtnCadastrarCliente.hide()
                    binding.txtBtnCadastrarCliente.show()
                    toast(state.error)
                }

                UiState.Loading -> {
                    binding.progressBtnCadastrarCliente.show()
                    binding.txtBtnCadastrarCliente.hide()
                }

                is UiState.Success -> {
                    binding.progressBtnCadastrarCliente.hide()
                    binding.txtBtnCadastrarCliente.show()
                    binding.nomeCadastro.setText("")
                    binding.emailCadastro.setText("")
                    binding.telefoneCadastro.setText("")
                    binding.aniversarioCadastro.setText("")
                    sucessCadastro()

                }
            }
        }
    }

    private fun sucessCadastro() {
        val dialog = requireContext().createDialog(
            R.layout.dialog_succes_cadastro_client,
            true
        )
        val btnClose = dialog.findViewById<ImageView>(R.id.close_dialog)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun clienteObj(): Cliente {
        return Cliente(
            nome = binding.nomeCadastro.text.toString(),
            email = binding.emailCadastro.text.toString(),
            telefone = binding.telefoneCadastro.text.toString(),
            aniversario = binding.aniversarioCadastro.text.toString(),
        )
    }

    private fun verific(): Boolean {
        var veriify = false

        if (binding.nomeCadastro.text.toString().isBlank() ||
            binding.emailCadastro.text.toString().isBlank() ||
            binding.telefoneCadastro.text.toString().isBlank() ||
            binding.aniversarioCadastro.text.toString().isBlank()
        ) {
            veriify = false
            toast("Campos obrigátorios em branco!")

        } else {
            veriify = true
        }

        return veriify
    }

    private fun back() {
        binding.back.setOnClickListener {
            findNavController().navigate(
                R.id.action_cadastroClienteFragment_to_homeScreenFragment
            )
        }
    }

}