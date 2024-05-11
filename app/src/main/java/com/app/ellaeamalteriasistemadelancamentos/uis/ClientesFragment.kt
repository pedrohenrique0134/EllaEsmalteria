package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.adapters.AdapterClientes
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentClientesBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientesFragment : Fragment() {
    private lateinit var binding: FragmentClientesBinding
    private val viewmodel: ViewModelLancamentos by viewModels()
    private var list: ArrayList<Cliente> = arrayListOf()
    private val adapter by lazy {
        AdapterClientes(requireContext(), list)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientesBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        getCliente()
        return binding.root
    }

    private fun getCliente(){
        viewmodel.getCliente()
        viewmodel.getClientes.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    toast(state.error)
                }
                UiState.Loading -> {
                    toast("Carregando Dados")
                }
                is UiState.Success -> {
                    list.addAll(state.data)
                    binding.rvClientes.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvClientes.adapter = adapter
                }
            }
        }
    }

}