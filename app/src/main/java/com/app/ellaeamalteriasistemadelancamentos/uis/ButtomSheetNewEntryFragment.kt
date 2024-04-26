package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentButtomSheetNewEntryBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.DateMaskTextWatcher
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import com.google.android.gms.common.Feature
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration.Companion.microseconds

@AndroidEntryPoint
class ButtomSheetNewEntryFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentButtomSheetNewEntryBinding
    private val viewModel: ViewModelLancamentos by viewModels()
    private lateinit var tipo: String

    companion object {
        const val ARG_DATA = "data"

        // Método para criar uma nova instância do fragmento com argumentos
        fun newInstance(data: String): ButtomSheetNewEntryFragment {
            val fragment = ButtomSheetNewEntryFragment()
            val args = Bundle().apply {
                putString(ARG_DATA, data)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentButtomSheetNewEntryBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        newLancamento()
        observer()
        radioEscolha {
            tipo = it
        }
        binding.dataNewEntrada.addTextChangedListener(DateMaskTextWatcher(binding.dataNewEntrada))
        return binding.root
    }

    private fun newLancamento() {
        binding.btnCadastrarCollaborator.setOnClickListener {
            if (verificEmpty()){
                viewModel.novoLance(
                    nome = requireArguments().getString(ARG_DATA).toString(),
                    lances()
                )
            }
        }
    }

    private fun lances(): Lancamentos{
        return Lancamentos(
            client = binding.nomeNovaEntrada.text.toString(),
            valor = binding.valorNewEntrada.text.toString(),
            data = binding.dataNewEntrada.text?.get(4).toString(),
            tipo = tipo,
            entryOrExit = "Entradas",
            id = Date().time.toString(),
            time = Date().time.toString()
        )
    }
    private fun observer(){
        viewModel.novoLance.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure ->{
                    binding.txtNovoLance.show()
                    binding.progressNovoLance.hide()
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtNovoLance.hide()
                    binding.progressNovoLance.show()
                }
                is UiState.Success ->{
                    binding.txtNovoLance.show()
                    binding.progressNovoLance.hide()
                    binding.nomeNovaEntrada.setText("")
                    binding.dataNewEntrada.setText("")
                    binding.valorNewEntrada.setText("")
                    toast(state.data)
                }
            }
        }
    }

    private fun radioEscolha(callback: (String) -> Unit) {
        val radioGpup = binding.radioGroup

        radioGpup.setOnCheckedChangeListener { group, checkedId ->
            val tipo = when (checkedId) {
                R.id.entra_din -> "Dinheiro"
                R.id.entra_cart -> "Cartão"
                R.id.entra_pix -> "Pix"
                else -> ""
            }
            callback(tipo)
        }
    }

    private fun verificEmpty(): Boolean{
        var verify = false

        if (binding.nomeNovaEntrada.text.toString().isBlank()||
            binding.dataNewEntrada.text.toString().isBlank()||
            binding.valorNewEntrada.text.toString().isBlank()){
            verify = false
            toast("Campos obrigatórios em branco!")
        }else{
            verify = true
        }

        return verify
    }


}