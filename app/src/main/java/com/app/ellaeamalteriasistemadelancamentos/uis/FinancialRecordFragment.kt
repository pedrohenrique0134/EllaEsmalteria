package com.app.ellaeamalteriasistemadelancamentos.uis

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ellaeamalteriasistemadelancamentos.R
import com.app.ellaeamalteriasistemadelancamentos.adapters.AdapterLances
import com.app.ellaeamalteriasistemadelancamentos.databinding.FragmentFinancialRecordBinding
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.app.ellaeamalteriasistemadelancamentos.utils.hide
import com.app.ellaeamalteriasistemadelancamentos.utils.lists
import com.app.ellaeamalteriasistemadelancamentos.utils.numberCurrency
import com.app.ellaeamalteriasistemadelancamentos.utils.show
import com.app.ellaeamalteriasistemadelancamentos.utils.spinnerFunText
import com.app.ellaeamalteriasistemadelancamentos.utils.toast
import com.app.ellaeamalteriasistemadelancamentos.viewModels.ViewModelLancamentos
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinancialRecordFragment : Fragment() {
    private lateinit var binding: FragmentFinancialRecordBinding
    private val viewModel: ViewModelLancamentos by viewModels()
    private var arrayList: ArrayList<Lancamentos> = arrayListOf()
    private lateinit var adapter: AdapterLances
    private var isExpanded = false
    private lateinit var nome: String
    private lateinit var foto: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinancialRecordBinding.inflate(layoutInflater).apply {
            viewLifecycleOwner
        }
        spinnerInitialize()
        getInfo()
        binding.mainFab.setOnClickListener {
            onMainFabClick()
        }

        binding.btnNewEntry.setOnClickListener {
            bottomSheetEntryView()
        }
        binding.btnNewExit.setOnClickListener {
            bottomSheetExitView()
        }

        binding.btnBuscar.setOnClickListener {
            getList()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                getList()
            }

        }, 1000)
        observer()
    }

    private fun getList() {
        viewModel.getLances(
            nome = nome,
            entryOrExit = manipulateSpinnerMovimentation(),
            data = manipulateSpinnerMes()
        )
    }

    private fun observer() {
        viewModel.getLances.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    binding.progressLancamentos.hide()
                    binding.rvLancamentos.hide()
                    binding.llEntreSaida.hide()
                    binding.aviso.show()
                    toast(state.error)
                }

                UiState.Loading -> {
                    binding.progressLancamentos.show()
                }

                is UiState.Success -> {

                    var valrInicial: Any
                    var valorEntradas: Double = 0.0
                    var valorSaidas: Double = 0.0
                    state.data.forEach {
                        valrInicial = it.valor!!
                        if (it.entryOrExit == "Entradas") {
                            valorEntradas += it.valor!!.toDouble()
                        } else {
                            valorSaidas += it.valor!!.toDouble()
                        }


                        binding.run {
                            with(receiveEntrdas) {
                                setText(
                                    context.getString(
                                        R.string.r,
                                        valorEntradas.let { numberCurrency(it.toString()) }
                                    )
                                )
                            }
                        }
                        binding.imgEntradas.setOnClickListener {
                            if (binding.receiveEntrdas.text == "Entradas: R$ ****") {
                                binding.run {
                                    with(receiveEntrdas) {
                                        setText(
                                            context.getString(
                                                R.string.r,
                                                valorEntradas.let { numberCurrency(it.toString()) }
                                            )
                                        )
                                    }
                                }
                                binding.imgEntradas.setImageResource(R.drawable.invisivel)

                            } else {
                                binding.run {
                                    with(receiveEntrdas) {
                                        setText(
                                            context.getString(
                                                R.string.r,
                                                "****"
                                            )
                                        )
                                    }
                                }
                                binding.imgEntradas.setImageResource(R.drawable.visivel)
                            }
                        }


                        binding.imgSaidas.setOnClickListener {
                            if (binding.receiveSaidas.text == "Saidas: R$ ****") {
                                binding.run {
                                    with(receiveSaidas) {
                                        setText(
                                            context.getString(
                                                R.string.rS,
                                                valorSaidas.let { numberCurrency(it.toString()) })
                                        )
                                    }
                                }
                                binding.imgSaidas.setImageResource(R.drawable.invisivel)
                            } else {
                                binding.run {
                                    with(receiveSaidas) {
                                        setText(
                                            context.getString(
                                                R.string.rS,
                                                "****"
                                            )
                                        )
                                    }
                                }
                                binding.imgSaidas.setImageResource(R.drawable.visivel)
                            }
                        }

                        binding.run {
                            with(receiveSaidas) {
                                setText(
                                    context.getString(
                                        R.string.rS,
                                        valorSaidas.let { numberCurrency(it.toString()) })
                                )
                            }
                        }

                    }



                    arrayList.clear()
                    binding.progressLancamentos.hide()
                    binding.rvLancamentos.show()
                    binding.llEntreSaida.show()
                    binding.aviso.hide()
                    adapter = AdapterLances(requireContext(), arrayList)
                    binding.rvLancamentos.layoutManager = LinearLayoutManager(requireContext())
                    arrayList.addAll(state.data)
                    binding.rvLancamentos.adapter = adapter
                }
            }
        }
    }

    private fun bottomSheetEntryView() {
        val buttomSheetNewEntryFragment = ButtomSheetNewEntryFragment.newInstance(nome)

        buttomSheetNewEntryFragment.show(
            requireActivity().supportFragmentManager,
            buttomSheetNewEntryFragment.tag
        )
    }

    private fun bottomSheetExitView() {
        val buttomSheetNewExitFragment = ButtomSheetNewExitFragment.newInstance(nome)
        buttomSheetNewExitFragment.show(
            requireActivity().supportFragmentManager,
            buttomSheetNewExitFragment.tag
        )
    }

    private fun spinnerInitialize() {
        spinnerFunText(requireContext(), binding.spinner1, lists.listMovimentaces)
        spinnerFunText(requireContext(), binding.spinner2, lists.listMeses)
    }

    private fun onMainFabClick() {
        if (!isExpanded) {
            // Mostrar opções secundárias
            binding.subOptionsLayout.visibility = View.VISIBLE
        } else {
            // Ocultar opções secundárias
            binding.subOptionsLayout.visibility = View.GONE
        }
        isExpanded = !isExpanded
    }

    private fun getInfo() {
        nome = requireArguments().getString("nome").toString()
        foto = requireArguments().getString("foto").toString()
        //Teste de image
        Log.d("TAG", "Photo: $foto")
        Glide.with(requireContext()).load(foto).error(R.drawable.avatar).into(binding.imageRegistro)
        //Picasso.get().load(foto).into(binding.imageRegistro)
        binding.txtRegistroFinanceiro.text = "Registro Finaceiro $nome"
    }

    private fun manipulateSpinnerMovimentation(): String {
        return when (binding.spinner1.text.toString()) {
            "Todas movimentações" -> "Todas movimentacoes"
            "Entradas" -> "Entradas"
            "Saídas" -> "Saídas"
            else -> "Entradas"
        }
    }

    private fun manipulateSpinnerMes(): String {
        return when (binding.spinner2.text.toString()) {
            "janeiro" -> "1"
            "Fevereiro" -> "2"
            "Março" -> "3"
            "Abril" -> "4"
            "Maio" -> "5"
            "Junho" -> "6"
            "Julho" -> "7"
            "Agosto" -> "8"
            "Setembro" -> "9"
            "Outubro" -> "10"
            "Novembro" -> "11"
            "Dezembro" -> "12"
            else -> "1"
        }
    }

}
