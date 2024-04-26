package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentButtomSheetNewExitBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.DateMaskTextWatcher
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ButtomSheetNewExitFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentButtomSheetNewExitBinding
    val viewModel : ViewModelLancamentos by viewModels()
    private lateinit var tipo: String

    companion object {
        const val ARG_DATA = "data"

        // Método para criar uma nova instância do fragmento com argumentos
        fun newInstance(data: String): ButtomSheetNewExitFragment {
            val fragment = ButtomSheetNewExitFragment()
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
        binding = FragmentButtomSheetNewExitBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }

        lancamentoSaida()
        observer()
        radioEscolha {
            tipo = it
        }
        binding.dataNewSaida.addTextChangedListener(DateMaskTextWatcher(binding.dataNewSaida))
        return binding.root
    }

    private fun lancamentoSaida() {
        binding.btnSaidaSalvar.setOnClickListener {
            if (verificEmpty()) {
                viewModel.novoLance(
                    nome = requireArguments().getString(ARG_DATA).toString(),
                    lancesSaida()
                )
            }
        }
    }

    private fun lancesSaida(): Lancamentos {
        return Lancamentos(
            valor = binding.valorNewSaida.text.toString(),
            data = binding.dataNewSaida.text?.get(4).toString(),
            tipo = tipo,
            entryOrExit = "Saídas",
            id = Date().time.toString(),
            time = Date().time.toString()
        )
    }

    private fun observer(){
        viewModel.novoLance.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.txtBtnSaida.show()
                    binding.progressBtnSaida.hide()
                    toast(state.error)
                }
                UiState.Loading -> {
                    binding.txtBtnSaida.hide()
                    binding.progressBtnSaida.show()
                }
                is UiState.Success -> {
                    binding.txtBtnSaida.show()
                    binding.progressBtnSaida.hide()
                    toast(state.data)
                }
            }
        }
    }

    private fun radioEscolha(callback: (String) -> Unit) {
        val radioGpup = binding.radioSaida

        radioGpup.setOnCheckedChangeListener { group, checkedId ->
            val tipo = when (checkedId) {
                R.id.din_saida -> "Dinheiro"
                R.id.pix_saida -> "Pix"
                else -> ""
            }
            callback(tipo)
        }
    }


    private fun verificEmpty(): Boolean{
        var verify = false

        if (binding.valorNewSaida.text.toString().isBlank()||
            binding.dataNewSaida.text.toString().isBlank()){
            verify = false
            toast("Campos obrigatórios em branco!")
        }else{
            verify = true
        }

        return verify
    }


}