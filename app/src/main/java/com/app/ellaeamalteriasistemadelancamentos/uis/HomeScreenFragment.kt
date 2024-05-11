package com.app.ellaeamalteriasistemadelancamentos.uis

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.adapters.AdapterCollaborator
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentHomeScreenBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.createDialog
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment(), AdapterCollaborator.ClickUser {

    private lateinit var binding: FragmentHomeScreenBinding
    private var list: ArrayList<Collaborators> = arrayListOf()
    private lateinit var dialog: Dialog
    private lateinit var nomeDialog: TextView
    private lateinit var senhaDialog: TextInputEditText
    private lateinit var btnDialog: LinearLayout
    private val viewModel : ViewModelLancamentos by viewModels()
    private val adapterCollaborator by lazy {
        AdapterCollaborator(requireContext(), list, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        observer()
        initView()
        binding.settings.setOnClickListener {
            menuPopup()
        }
        binding.btnGetBottonSheet.setOnClickListener {
            bottomSheetView()
        }
        return binding.root
    }

    private fun menuPopup() {
        val menu = PopupMenu(requireContext(), binding.settings)
        menu.inflate(R.menu.menu_home)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    bottomSheetView()
                    true
                }
                R.id.action_cadastro_cliente->{
                    findNavController().navigate(
                        R.id.action_homeScreenFragment_to_cadastroClienteFragment
                    )
                    true
                }
                R.id.action_cliente ->{
                    findNavController().navigate(
                        R.id.action_homeScreenFragment_to_clientesFragment
                    )
                    true
                }

                else -> true
            }

        }
        menu.show()
    }

    private fun bottomSheetView() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(
            requireActivity().supportFragmentManager,
            bottomSheetFragment.tag
        )
    }

    private fun initView() {
        viewModel.getCollaborator()
    }

    private fun observer(){
        viewModel.getCollaborators.observe(viewLifecycleOwner){state->
            when(state){
                is UiState.Failure -> {
                    binding.progressColaborators.hide()
                    binding.txtMsgSemColaborador.show()
                    binding.btnGetBottonSheet.show()
                }
                UiState.Loading -> {
                    binding.progressColaborators.show()
                }
                is UiState.Success -> {
                    list.clear()
                    binding.progressColaborators.hide()
                    binding.txtMsgSemColaborador.hide()
                    binding.btnGetBottonSheet.hide()
                    binding.rvCollaborators.show()
                    list.addAll(state.data)
                    binding.rvCollaborators.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvCollaborators.adapter = adapterCollaborator
                }
            }
        }
    }

    private fun showDialog(collaborators: Collaborators) {
        dialog = requireContext().createDialog(R.layout.dialog_login_collaborator, true)


        nomeDialog = dialog.findViewById(R.id.receive_nome_dialog)
        senhaDialog = dialog.findViewById(R.id.senha_dialog)
        btnDialog = dialog.findViewById(R.id.btn_dialog)

        btnDialog.setOnClickListener {
            if (verifyBoxEmpty()) {
                if (senhaDialog.text.toString() == collaborators.senha) {
                    val bundle = bundleOf(
                        "nome" to collaborators.nome,
                        "senha" to collaborators.senha,
                        "email" to collaborators.email,
                        "foto" to collaborators.foto,
                        "telefone" to collaborators.telefone,
                        "aniversario" to collaborators.aniversario,
                    )
                    findNavController().navigate(
                        R.id.action_homeScreenFragment_to_financialRecordFragment,
                        bundle
                    ).apply {
                        dialog.dismiss()
                    }
                }else{
                    toast("Senha incorreta, tente novamente!")
                }
            }
        }
        nomeDialog.text = collaborators.nome
        dialog.show()
    }

    private fun verifyBoxEmpty(): Boolean {
        var verify = false
        if (senhaDialog.text.toString().isNotBlank()) {
            verify = true
        } else {
            toast("Digite sua senha antes de continuar!")
            verify = false
        }
        return verify
    }

    override fun clickUser(collaborators: Collaborators) {
        showDialog(collaborators)
    }


}